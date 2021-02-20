package fr.miage.filestore.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import org.slf4j.LoggerFactory;

public class FilestoreVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        Router router = Router.router(vertx);

        router.route(HttpMethod.GET, "/get").handler(routingContext -> {
            routingContext
                    .response()
                    .putHeader("content-type", "text/html")
                    .end("Get route triggered!");
        });

        router.route().handler(routingContext -> {
            routingContext
                    .response()
                    .putHeader("content-type", "text/html")
                    .end("Hello World!");
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)

                .listen(8083, http -> {
                    if (http.succeeded()) {
                        startFuture.complete();
                        LoggerFactory.getLogger(getClass()).info("HTTP server started on http://localhost:8083");
                    } else {
                        startFuture.fail(http.cause());
                    }
                });
    }

}
