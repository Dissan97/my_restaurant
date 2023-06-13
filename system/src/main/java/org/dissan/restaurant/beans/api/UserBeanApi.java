package org.dissan.restaurant.beans.api;

import org.dissan.restaurant.beans.BadCommandEntryException;
import org.dissan.restaurant.beans.UserBeanCommand;
import org.dissan.restaurant.models.UserRole;
import org.jetbrains.annotations.NotNull;

public interface UserBeanApi {
    String getUsername();
    String getPassword();
    String getName();
    String getSurname();
    String getCityOfBirth();
    String getDateOfBirth();
    UserRole getRole();
    void insertCommand(@NotNull UserBeanCommand command, String entry) throws BadCommandEntryException;
    void clean();
}
