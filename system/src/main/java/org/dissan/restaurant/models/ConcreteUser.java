package org.dissan.restaurant.models;

import org.dissan.restaurant.models.exceptions.UserRoleException;
import org.dissan.restaurant.patterns.creational.factory.RoleFactory;

import java.text.ParseException;
import java.util.logging.Logger;

public class ConcreteUser extends AbstractUser{
    private AbstractModelRole concreteRole;
    public ConcreteUser() {}

    /**
     * Constructor parametrized String
     * @param usr username String
     * @param pwd password String
     * @param nme name String
     * @param snm surname String
     * @param cob cityOfBirth String
     * @param dob dateOfBirth -> {date} -> String
     * @param rle role {String} -> UserRole
     */
    public ConcreteUser(String usr, String pwd, String nme, String snm, String cob, String dob, String rle) {
        setUsername(usr);
        setPassword(pwd);
        setName(nme);
        setSurname(snm);
        setCityOfBirth(cob);
        try {
            setDateOfBirth(dob);
        }catch (ParseException e){
            Logger.getLogger(this.getClass().getSimpleName()).warning(e.getMessage());
        }
        setRole(rle);
    }

    public ConcreteUser(String eCode) {
        this.setUsername(eCode);
    }

    public void switchRole() throws UserRoleException {
        this.setConcreteRole(RoleFactory.getInstance(this));
    }

    public void switchRole(AbstractUser user) throws UserRoleException {
        super.setUpVariables(user);
        this.switchRole();
    }


    public AbstractModelRole getConcreteRole() {
        return concreteRole;
    }

    private void setConcreteRole(AbstractModelRole cRole) {
        this.concreteRole = cRole;
    }

}
