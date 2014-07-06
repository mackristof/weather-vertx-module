package fr.mackristof.weather

import groovy.transform.CompileStatic
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.json.JsonObject
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
/**
 * Created by christophe mourette on 29/06/14 for Geomatys.
 */
@CompileStatic
class SendDataVerticle extends Verticle {
    final String broker = "tcp://iot.eclipse.org:1883";
    final String clientId     = "DavisTest";
    final String topic        = "weather";
    final int qos             = 2;
    final MemoryPersistence persistence = new MemoryPersistence();

    def start(){
        vertx.eventBus.registerHandler('publish-current-data') { Message<JsonObject> message ->
            sendToMQTTBroker((JsonObject)message.body())
        }
    }

    def sendToMQTTBroker(JsonObject jsonObject) {
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence)
            MqttConnectOptions connOpts = new MqttConnectOptions()
            connOpts.setCleanSession(true)
            container.logger.info("Connecting to broker: $broker")
            sampleClient.connect(connOpts)
            container.logger.info('Connected')
            container.logger.info("Publishing message: $jsonObject")
            MqttMessage message = new MqttMessage(jsonObject.encodePrettily().getBytes())
            message.setQos(qos)
            sampleClient.publish(topic, message)
            container.logger.info('Message published')
            sampleClient.disconnect()
            container.logger.info('Disconnected')
        } catch(MqttException me) {
            container.logger.error("excep $me")
        }

    }
}
