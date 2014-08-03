package integration_tests.groovy

import fr.mackristof.weather.GetDataVerticle

/**
 * Created by christophe mourette on 06/07/14 for Geomatys.
 */
/**
 * Simple integration test which shows tests deploying other verticles, using the Vert.x API etc
 */


import static org.vertx.testtools.VertxAssert.*

// And import static the VertxTests script
import org.vertx.groovy.testtools.VertxTests

// The test methods must being with "test"

// This demonstrates using the Vert.x API from within a test.
//def testHTTP() {
//    // Create an HTTP server which just sends back OK response immediately
//    vertx.createHttpServer().requestHandler({ req ->
//        req.response.end()
//    }).listen(8181, { asyncResult ->
//        assertTrue(asyncResult.succeeded)
//        // The server is listening so send an HTTP request
//        vertx.createHttpClient().setPort(8181).getNow("/", { resp ->
//            assertEquals(200, resp.statusCode)
//            /*
//            If we get here, the test is complete
//            You must always call `testComplete()` at the end. Remember that testing is *asynchronous* so
//            we cannot assume the test is complete by the time the test method has finished executing like
//            in standard synchronous tests
//            */
//            testComplete()
//        })
//    })
//}

/*
  This test deploys some arbitrary verticle - note that the call to testComplete() is inside the Verticle `GroovySomeVerticle`
  GroovySomeVerticle is a test verticle written in Groovy
*/
def testGetDataFromMQTTVerticle() {
    container.deployVerticle("groovy:" + GetDataVerticle.class.getName())
    vertx.eventBus.registerHandler("data-from-mqtt-broker") { message ->
        container.logger.info(message)
        assertNotNull(message);
        testComplete()
    }
}

//def testCompleteOnTimer() {
//    vertx.setTimer(1000, { timerID ->
//        assertNotNull(timerID)
//
//        // This demonstrates how tests are asynchronous - the timer does not fire until 1 second later -
//        // which is almost certainly after the test method has completed.
//        testComplete()
//    })
//}

VertxTest.initialize(this)
container.logger.info('start test')
VertxTests.startTests(this)
container.logger.info('started test')
