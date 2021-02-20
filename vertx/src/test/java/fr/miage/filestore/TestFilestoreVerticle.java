package fr.miage.filestore;

import tech.becoming.frameworks.filestore.vertx.FilestoreVerticle;
import io.vertx.core.Vertx;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
public class TestFilestoreVerticle {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new FilestoreVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  @DisplayName("Should start a Web Server on port 8083")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void start_http_server(Vertx vertx, VertxTestContext testContext) throws Throwable {
    vertx.createHttpClient().getNow(8083, "localhost", "/", response -> testContext.verify(() -> {
      assertTrue(response.statusCode() == 200);
      response.handler(body -> {
        assertTrue(body.toString().contains("Hello World!"));
        testContext.completeNow();
      });
    }));
  }

}
