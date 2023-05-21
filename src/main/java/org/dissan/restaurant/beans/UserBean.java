package org.dissan.restaurant.beans;

import org.dissan.restaurant.beans.api.UserBeanApi;
import org.dissan.restaurant.models.AbstractUser;
import org.dissan.restaurant.models.UserRole;
import org.jetbrains.annotations.NotNull;
import java.util.EnumMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserBean implements UserBeanApi {
    private AbstractUser user;
    private final EnumMap<EnumCommand, String> viewEntries = new EnumMap<>(EnumCommand.class);

    public AbstractUser getUser() {
        return user;
    }

    public void setUser(AbstractUser user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public String getName() {
        return user.getName();
    }
    @Override
    public String getSurname() {
        return user.getSurname();
    }

    @Override
    public String getCityOfBirth() {
        return user.getCityOfBirth();
    }
    @Override
    public String getDateOfBirth() {
        return user.getDateOfBirth();
    }
    @Override
    public UserRole getRole() {
        return user.getRole();
    }
    @Override
    public void insertCommand(@NotNull EnumCommand command, String entry) throws BadCommanEntryException{

        switch (command){
            case PASSWORD:
                handlePassword(entry);
                break;
            case USERNAME:
                handleUserName(entry);
                break;
            case NAME:
            case ROLE:
            case SURNAME:
            case CITY_OF_BIRH:
            case DATE:
                handleCommon(entry);
                break;
            case FLUSH:
                clean();
                break;
            default:
                break;
        }
        this.viewEntries.remove(command);
        this.viewEntries.put(command, entry);
    }

    private void handleCommon(String entry) throws BadCommanEntryException {
        if (entry == null){
            throw new BadCommanEntryException("Entry is null");
        }

        if (entry.isEmpty()){
            throw new BadCommanEntryException("Entry is empty");
        }
    }


    private void handleUserName(String entry) throws BadCommanEntryException {
        this.handleCommon(entry);
        if (entry.length() < 6){
            throw new BadCommanEntryException("this field must contains at least 6 letters");
        }
    }

    private void handlePassword(String entry) throws BadCommanEntryException {
        this.handleUserName(entry);

        final String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!.?_]).*$";

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(entry);

        if (!matcher.matches()){
            throw new BadCommanEntryException("\nPassword must contains upper letter and has num and special character example:" +
                    "\nThis.isG00d - thisIsNot");
        }

    }

    public String getEntry(EnumCommand command) throws BadCommanEntryException {
        String ret = this.viewEntries.get(command);
        if (ret == null){
            throw new BadCommanEntryException(command.name() + " has no entry");
        }
        return ret;
    }


    public void clean(){
        this.viewEntries.clear();
    }



}
