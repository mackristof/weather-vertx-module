package fr.mackristof.weather

import fr.mackristof.weather.utils.LEDataInputStream
import fr.mackristof.weather.utils.WeatherCalc
import groovy.transform.CompileStatic
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.json.JsonObject

/**
 * Created by christophe mourette on 29/06/14 for Geomatys.
 */
@CompileStatic
class DecodePacketStreamVerticle extends Verticle {

    def start() {

        vertx.eventBus.registerHandler('decode-current-data') { Message<byte[]> message ->
            JsonObject jsonObject = decodePacket((byte[])message.body())
            container.logger.info("decoded data : $jsonObject")
            vertx.eventBus.send('publish-current-data', jsonObject)
        }
    }

    def JsonObject decodePacket(byte[] packet){
        final response = new JsonObject()
        def stream = new LEDataInputStream(new ByteArrayInputStream(packet))
        byte first = stream.readByte()
//        response.putBinary('first',stream.readByte())
        assert 'L'.equalsIgnoreCase(new String(stream.readByte()))
        assert 'O'.equalsIgnoreCase(new String(stream.readByte()))
        assert 'O'.equalsIgnoreCase(new String(stream.readByte()))
        response.putNumber('barTrend', new Integer(stream.readByte()))
        response.putNumber('typePacket', new Integer(stream.readByte()))
        response.putNumber('nextRecord', stream.readShort())
        response.putNumber('barometer',  WeatherCalc.calcPressionInhPa(stream.readShort()))

        response.putNumber('insideTemp', WeatherCalc.calcTempCelcuis(stream.readShort()))
        response.putNumber('insideHumidity',new Integer(stream.readByte()))
        response.putNumber('outsideTemp',WeatherCalc.calcTempCelcuis(stream.readShort()))
        response.putNumber('windSpeed',WeatherCalc.calcSpeed(new Integer(stream.readUnsignedByte())))
        response.putNumber('10MinAvgWindSpeed',WeatherCalc.calcSpeed(new Integer(stream.readUnsignedByte())))
        response.putNumber('windDirection',stream.readUnsignedShort())
        stream.skipBytes(15)
        response.putNumber('outsideHumidity',new Integer(stream.readByte()))
        stream.skipBytes(7)
        response.putNumber('rainRate',WeatherCalc.calcRainRate(stream.readShort()))
        //response.putNumber('UV',new Integer(stream.readByte()))
        //response.putNumber('solarRadiation',new Integer(stream.readShort()))
        return response
    }
}
