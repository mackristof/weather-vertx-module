package fr.mackristof.weather

import groovy.transform.CompileStatic
import org.vertx.groovy.platform.Verticle

/**
 * Created by christophe mourette on 29/06/14 for Geomatys.
 */
@CompileStatic
class MainVerticle extends Verticle {
    def start(){
        container.deployWorkerVerticle("groovy:" + GetPacketVerticle.class.getName())
        container.deployWorkerVerticle("groovy:" + SendDataVerticle.class.getName())
        container.deployVerticle("groovy:" + DecodePacketStreamVerticle.class.getName())
        vertx.setPeriodic(10_000) { timerID ->
            vertx.eventBus.send('get-current-data',null)
        }
    }

}
