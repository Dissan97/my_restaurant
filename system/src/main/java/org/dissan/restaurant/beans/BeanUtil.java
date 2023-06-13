package org.dissan.restaurant.beans;
import org.jetbrains.annotations.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeanUtil {

    private static final String DAY_FORMAT = "dd-MM-yyyy";
    private static final String TIME_FORMAT = "::HH:mm";

    private BeanUtil(){}

    public static void handleCommon(String entry) throws BadCommandEntryException {
        if (entry == null){
            throw new BadCommandEntryException("Entry cannot be null");
        }

        if (entry.isEmpty()){
            throw new BadCommandEntryException("Entry cannot be empty");
        }
    }


    public static void handleUserName(String entry) throws BadCommandEntryException {
        handleUserName(entry, "username");
    }

    public static void handleUserName(String entry, String field) throws BadCommandEntryException {
        BeanUtil.handleCommon(entry);
        if (entry.length() < 6){
            throw new BadCommandEntryException("field " + field + " must contains at least 6 letters");
        }
    }

    public static void handlePassword(String entry) throws BadCommandEntryException {
        BeanUtil.handleUserName(entry, "password");
        final String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!.?_]).*$";

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(entry);

        if (!matcher.matches()) {
            throw new BadCommandEntryException(
                    """
                    field Password must contains upper letter and has num and special character example:
                    This.isG00d - thisIsNot
                    """
            );
        }
    }

    public static @Nullable String goodDate(String dateTime, boolean dateControl) {
        if (dateTime == null){
            return null;
        }
        Date date;
        String ret;
        String pattern = DAY_FORMAT;
        try {
            date = getDate(dateTime, dateControl);
            if (dateControl){
                pattern += TIME_FORMAT;
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            ret = format.format(date);
        } catch (ParseException e) {
            //This is thrown when is passed a bad date format
            return null;
        }

        if (!dateControl){
            return ret;
        }
        Calendar calendar = Calendar.getInstance();
        Date controlDate = calendar.getTime();
        //this will be thrown when is passed a date before today
        boolean control = date.before(controlDate);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        controlDate = calendar.getTime();
        //this will be thrown when is passed a day after a week
        control |= date.after(controlDate);
        if (control){
            return null;
        }
        return ret;
    }

    private static Date getDate(String dateTime, boolean task) throws ParseException{
        String date = DAY_FORMAT;
        String time = "";
        if (task){
            time = TIME_FORMAT;
        }
        String toParse = date + time;
        SimpleDateFormat dateFormat = new SimpleDateFormat(toParse);
        Date ret;
        try {
            ret = dateFormat.parse(dateTime);
        }catch (ParseException e){
            date = "dd/MM/yyyy";
            toParse = date + time;
            try {
                dateFormat = new SimpleDateFormat(toParse);
                ret = dateFormat.parse(dateTime);
            }catch (ParseException ex){
                date = "yyyy-MM-dd";
                toParse = date + time;
                try {
                    dateFormat = new SimpleDateFormat(toParse);
                    ret = dateFormat.parse(dateTime);
                }catch (ParseException exx){
                    date = "yyyy/MM/dd";
                    toParse = date + time;
                    try {
                        dateFormat = new SimpleDateFormat(toParse);
                        ret = dateFormat.parse(dateTime);
                    }catch (ParseException err){
                        throw new ParseException("bad date", 1);
                    }
                }
            }
        }
        return ret;
    }
}
