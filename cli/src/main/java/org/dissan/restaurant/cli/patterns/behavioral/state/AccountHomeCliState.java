package org.dissan.restaurant.cli.patterns.behavioral.state;

import org.dissan.restaurant.beans.UserBean;
import org.jetbrains.annotations.NotNull;
import java.util.Locale;

public abstract class AccountHomeCliState extends CliState {
    protected UserBean userBean;

    protected AccountHomeCliState(String className, @NotNull UserBean userBean) {
        super(className);
        this.userBean = userBean;
        this.allowedCommands.remove(String.valueOf(this.allowedCommands.length()));
        addCmd("account");
        setPageName(userBean.getUsername()+'@'+ userBean.getRole().name().toLowerCase(Locale.ROOT));
    }


    protected void showAccountInfo(){
        outline(
                "Account information:" + '\n' +
                        "name: " + this.userBean.getName() + '\n' +
                        "surname: " + this.userBean.getSurname() + '\n' +
                        "city of birth: " + this.userBean.getCityOfBirth() + '\n' +
                        "date of birth: " + this.userBean.getDateOfBirth()
        );
    }


}