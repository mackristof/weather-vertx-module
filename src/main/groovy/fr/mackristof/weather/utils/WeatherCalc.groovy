package fr.mackristof.weather.utils

import groovy.transform.CompileStatic

/**
 * Created by christophe mourette on 29/06/14 for Geomatys.
 */
@CompileStatic
class WeatherCalc {


    static Number calcPressionInhPa(short inchHg){
        Number mmHg = (inchHg * 25.4)
        Number hPa = mmHg * 1.33322
        return (hPa / 1000)
    }

    static Number calcTempCelcuis(short tempF){
        Number val2 = tempF/10
        return ((val2- 32) * 5) / 9
    }

    static Number calcSpeed(int mph) {
        return mph * 1.609344
    }

    static Number calcRainRate(short clickPerHour) {
        return clickPerHour * 0.2
    }
}
