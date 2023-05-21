package org.dissan.restaurant.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ModelUtil {

    private ModelUtil(){}

    public static boolean goodDate(String dateTime) {
        String toParse = "dd-MM-yyyy::HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(toParse);
        Calendar calendar = Calendar.getInstance();
        Date controlDate = calendar.getTime();
        Date date;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            //This is thrown when is passed a bad date format
            return false;
        }

        assert date != null;
        if (date.before(controlDate)){
            //this will be thrown when is passed a date before today
            return false;
        }

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        controlDate = calendar.getTime();

        //this will be thrown when is passed a day after a week
        return !date.after(controlDate);
    }
}
