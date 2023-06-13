package org.dissan.restaurant.models;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Logger;

public abstract class AbstractUser {

    private String username;
    private String password;
    private String name;
    private String surname;
    private String dateOfBirth;
    private String cityOfBirth;
    private UserRole role;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
             format.parse(date);
        } catch (ParseException e) {
            format = new SimpleDateFormat("dd-MM-yyyy");
            try {
                format.parse(date);
            } catch (ParseException ex) {
                format = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    format.parse(date);
                } catch (ParseException exc) {
                    throw new ParseException("Bad date: " + date, exc.getErrorOffset());
                }
            }
        }
        this.dateOfBirth = date;
    }

    public String getCityOfBirth() {
        return cityOfBirth;
    }

    public void setCityOfBirth(String cityOfBirth) {
        this.cityOfBirth = cityOfBirth;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(@NotNull String role){
        this.setRole(UserRole.valueOf(role.toUpperCase(Locale.ROOT)));
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setUpVariables(@NotNull AbstractUser usr){
        this.setUsername(usr.getUsername());
        this.setPassword(usr.getPassword());
        this.setName(usr.getName());
        this.setSurname(usr.getSurname());
        this.setCityOfBirth(usr.getCityOfBirth());
        try {
            this.setDateOfBirth(usr.getDateOfBirth());
        } catch (ParseException e) {
            Logger.getLogger(this.getClass().getSimpleName()).warning(e.getMessage());
        }
        this.role = usr.getRole();
    }

}
