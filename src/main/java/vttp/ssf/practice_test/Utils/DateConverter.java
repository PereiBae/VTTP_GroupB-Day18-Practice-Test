package vttp.ssf.practice_test.Utils;

import jakarta.json.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    public static Date stringToDate(String jsonDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM/dd/yyyy");
            return sdf.parse(jsonDate);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static long dateTolong(Date date) {
        try{
            return date.getTime();
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static Date longToDate(JsonObject jsonObject, String dateFormat) {
        long longDate = jsonObject.getJsonNumber(dateFormat).longValue();
        return new Date(longDate);
    }

}
