package org.dissan.restaurant.models;

import org.dissan.restaurant.controllers.exceptions.ShiftDateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShiftSchedule{
    private Shift shift;
    private AbstractUser user;
    private String dateTime;
    private boolean updateRequest;

    public ShiftSchedule(Shift shift, AbstractUser employee) {
        this.shift = shift;
        this.user = employee;
    }

    public ShiftSchedule(String sCode, String eCode, String dTime) {
        this.shift = new Shift(sCode);
        this.user = new ConcreteUser(eCode);
        this.dateTime = dTime;
    }

    public String getDateTime() {
        return dateTime;
    }



    /**
     * This method setUp correctly the date due to internal policy.
     * @param dateTime String date parse it and set the variable this.dateTime correctly
     * @throws ShiftDateException This exception is thrown when some date is incorrect semantic
     * when date is before today and when is after a week from today
     */

    public void setDateTime(String dateTime) throws ShiftDateException {
        String toParse = "dd-MM-yyyy::HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(toParse);
        Calendar calendar = Calendar.getInstance();
        Date controlDate = calendar.getTime();
        Date date;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            //This is thrown when is passed a bad date format
            throw new ShiftDateException("bad date: " + dateTime + " should be: " + toParse);
        }

        if (date.before(controlDate)){
            //this will be thrown when is passed a date before today
            throw new ShiftDateException("Day Before today");
        }

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        controlDate = calendar.getTime();

        if (date.after(controlDate)){
            //this will be thrown when is passed a day after a week
            throw new ShiftDateException("Day After a week");
        }
        this.dateTime = date.toString();
    }

    @Override
    public String toString(){
        return this.shift.getCode() + ";" + this.user.getUsername() + ";" + this.getDateTime();
    }

    public void setUpdate(boolean update) {
        this.updateRequest = update;
    }
}
