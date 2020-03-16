package utilities.logger;

import utilities.ConfigurationData;

import java.text.SimpleDateFormat;
import java.util.Date;
class TacitusHelper {

    static String getDateAndTime() {
        return new SimpleDateFormat("yyyy/MM/dd || HH:mm:ss: ").format(new Date());
    }

    static String getDateAndTimeTitle() {
        return new SimpleDateFormat("_yyyy_MM_dd_HH_mm").format(new Date());
    }
}
