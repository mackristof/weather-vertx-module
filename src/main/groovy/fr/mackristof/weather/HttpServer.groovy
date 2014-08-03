package fr.mackristof.weather

import groovy.transform.CompileStatic
import org.vertx.groovy.core.http.HttpServerRequest
import org.vertx.groovy.platform.Verticle

/**
 * Created by christophe mourette on 02/08/14 for Geomatys.
 */
@CompileStatic
class HttpServer extends Verticle{
    def start(){
        vertx.createHttpServer().requestHandler { HttpServerRequest req ->
//            container.logger.info("request http "+req.absoluteURI)
            def file = req.uri == "/" ? "index.html" : req.uri
            container.logger.info("request "+new File("web/$file").getAbsolutePath())
            req.response.sendFile "web/$file"
        }.listen(8080)
    }
}
