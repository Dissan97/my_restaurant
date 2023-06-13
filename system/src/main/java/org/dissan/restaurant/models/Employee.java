package org.dissan.restaurant.models;

public abstract class Employee extends AbstractModelRole {
    private String employeeCode;

    protected Employee(AbstractUser user) {
        super(user);
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

}
