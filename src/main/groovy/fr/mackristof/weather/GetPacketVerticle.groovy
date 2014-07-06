package fr.mackristof.weather

import groovy.transform.CompileStatic
import org.vertx.groovy.platform.Verticle
import jssc.SerialPortList
import jssc.SerialPort
import jssc.SerialPortException

/**
 * Created by christophe mourette on 28/06/14 for Geomatys.
 */
@CompileStatic
class GetPacketVerticle extends Verticle {

    def start() {
        vertx.eventBus.registerHandler('get-current-data') { message ->
            String[] portNames = SerialPortList.getPortNames()
            connect(portNames.findAll({ ((String)it).contains('USB') }))
        }


    }


    def connect(Collection<String> ttys) {
        for (String tty : ttys) {
            try {
                container.logger.info("try to connect $tty")
                SerialPort currentPort = new SerialPort(tty)
                currentPort.openPort()
                currentPort.setParams(SerialPort.BAUDRATE_19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)
                currentPort.writeBytes('\n'.getBytes());
                Thread.sleep(1000)
                currentPort.readBytes()
                Thread.sleep(1000)
                currentPort.writeBytes('LOOP 1\n'.getBytes());
                Thread.sleep(1000)
                byte[] packet = currentPort.readBytes()
                container.logger.info("receiving data from $tty")
                vertx.eventBus.send('decode-current-data', packet)
                currentPort.closePort()
                container.logger.info("close connection for $tty")
            } catch (SerialPortException ex) {
                container.logger.info("problem while retreaving data from $tty")
            }
        }
    }
}
