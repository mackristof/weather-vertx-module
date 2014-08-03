package fr.mackristof.weather

import groovy.transform.CompileStatic
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.vertx.groovy.core.Vertx
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.platform.Container
import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.json.JsonObject

/**
 * Created by christophe mourette on 29/06/14 for Geomatys.
 */
@CompileStatic
class GetDataVerticle extends Verticle {
    final String broker = "tcp://iot.eclipse.org:1883";
    final String clientId     = "RaspberryPiTest";
    final String topic        = "weather";
    final int qos             = 2;
    final MemoryPersistence persistence = new MemoryPersistence();

    def start() {
        GetFromMQTTBroker()
    }

    def GetFromMQTTBroker() {
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence)
            MqttConnectOptions connOpts = new MqttConnectOptions()
            connOpts.setCleanSession(true)
            container.logger.info("Connecting to broker: $broker")
            sampleClient.connect(connOpts)
            container.logger.info('Connected')
            sampleClient.callback = new MyMQTTCallback(vertx : vertx, container : container)
            sampleClient.subscribe(topic)

            container.logger.info('Message published')
            sampleClient.disconnect()
            container.logger.info('Disconnected')
        } catch(MqttException me) {
            container.logger.error("excep $me")
        }

    }

    private class MyMQTTCallback implements MqttCallback {
        private Vertx vertx
        private Container container


        @Override
        void connectionLost(Throwable throwable) {

        }

        @Override
        void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            container.logger.info("YES YES YES YES  : message from MQTT broker : $mqttMessage.payload")
            vertx.eventBus.publish("data-from-mqtt-broker", new String(mqttMessage.payload))
        }

        @Override
        void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        }
    }
}
