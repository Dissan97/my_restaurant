package org.dissan.restaurant.fxml.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GuiDatePickerUtil {
    private GuiDatePickerUtil(){}
    public static String parseLocalToSystemDate(String shiftDate){
        String dateFormatted;
        SimpleDateFormat localFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat systemFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date parseDate = localFormat.parse(shiftDate);
            dateFormatted = systemFormat.format(parseDate);
        } catch (ParseException e) {
            dateFormatted = "";
        }
        return dateFormatted;
    }
}
