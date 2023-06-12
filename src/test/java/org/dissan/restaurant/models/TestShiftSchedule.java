package org.dissan.restaurant.models;

import org.dissan.restaurant.controllers.exceptions.ShiftDateException;
import org.dissan.restaurant.models.exceptions.UserRoleException;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertTrue;


public class TestShiftSchedule {

    @Test
    public void shiftDateTest() throws UserRoleException {
        final String test = "TEST";
        Shift shift = new Shift(test);
        Employee employee = new Attendant(MockUser.getUser());

        Calendar calendar = Calendar.getInstance();
        //assigning a good date for the model
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy::HH:mm");
        String goodDate = dateFormat.format(date);

        boolean firstAssertion;
        ShiftSchedule schedule = null;
        try {
            //Expecting date the date is good
            schedule = new ShiftSchedule(shift, employee, goodDate);
            firstAssertion = true;

        } catch (ShiftDateException e) {
            //
            firstAssertion = false;
        }
        boolean secondAssertion = false;
        calendar = Calendar.getInstance();
        //assigning a bad date for the model
        date = calendar.getTime();
        if (schedule != null) {
            try {
                schedule.setShiftDate(dateFormat.format(date));
            } catch (ShiftDateException e) {
                //Expecting that today is not a good date
                secondAssertion = true;
            }
        }
        assertTrue(firstAssertion & secondAssertion);

    }
}
