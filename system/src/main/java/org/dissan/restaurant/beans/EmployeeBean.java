package org.dissan.restaurant.beans;

import org.dissan.restaurant.models.Employee;

public class EmployeeBean {
    private final Employee employee;
    public EmployeeBean(Employee employee){
        this.employee = employee;
    }

    public String getCode(){
       return this.employee.getEmployeeCode();
    }
}
