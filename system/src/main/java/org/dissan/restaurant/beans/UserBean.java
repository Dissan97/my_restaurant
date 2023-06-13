package org.dissan.restaurant.beans;

import org.dissan.restaurant.beans.api.UserBeanApi;
import org.dissan.restaurant.models.AbstractUser;
import org.dissan.restaurant.models.UserRole;
import org.jetbrains.annotations.NotNull;
import java.util.EnumMap;

public class UserBean implements UserBeanApi {
    private AbstractUser user;
    private final EnumMap<UserBeanCommand, String> viewEntries = new EnumMap<>(UserBeanCommand.class);

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
    public void insertCommand(@NotNull UserBeanCommand command, String entry) throws BadCommanEntryException{

        switch (command) {
            case PASSWORD -> BeanUtil.handlePassword(entry);
            case USERNAME -> BeanUtil.handleUserName(entry);
            case NAME, ROLE, SURNAME, CITY_OF_BIRH, DATE -> BeanUtil.handleCommon(entry);
            case FLUSH -> clean();
        }
        this.viewEntries.remove(command);
        this.viewEntries.put(command, entry);
    }


    public String getEntry(UserBeanCommand command) throws BadCommanEntryException {
        String ret = this.viewEntries.get(command);
        if (ret == null){
            throw new BadCommanEntryException(command.name() + " has no entry");
        }
        return ret;
    }

    @Override
    public void clean(){
        this.viewEntries.clear();
    }



}
