package fr.miage.filestore.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="https://becoming.lu">Rodislav MOLDOVAN</a>
 *
 * https://github.com/vert-x3/vertx-examples/blob/master/web-examples/src/main/java/io/vertx/example/util/Runner.java
 */
class Runner {

    public static void main(String[] args) {
        runInternal(System.getProperty("user.dir"),
                FilestoreVerticle.class.getName(),
                new VertxOptions().setClustered(false));
    }

    private static void runInternal(String classDir, String verticleID, VertxOptions options) {
        if (options == null) {
            // Default parameter
            options = new VertxOptions();
        }
        // Smart cwd detection

        // Based on the current directory (.) and the desired directory (classDir), we try to compute the vertx.cwd
        // directory:
        try {
            // We need to use the canonical file. Without the file name is .
            File current = new File(".").getCanonicalFile();
            if (classDir.startsWith(current.getName()) && !classDir.equals(current.getName())) {
                classDir = classDir.substring(current.getName().length() + 1);
            }
        } catch (IOException e) {
            // Ignore it.
        }

        System.setProperty("vertx.cwd", classDir);
        Consumer<Vertx> runner = vertx -> {
            try {
                vertx.deployVerticle(verticleID);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };

        if (options.isClustered()) {
            Vertx.clusteredVertx(options, res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    runner.accept(vertx);
                } else {
                    res.cause().printStackTrace();
                }
            });
        } else {
            Vertx vertx = Vertx.vertx(options);
            runner.accept(vertx);
        }
    }
}