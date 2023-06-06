package org.dissan.restaurant.models;

public class Shift {

    private String code;
    private String task;
    private String role;
    private double salary;

    public Shift(String cd, String tsk, String rl, double slr) {
        this.setCode(cd);
        this.setTask(tsk);
        this.setRole(rl);
        this.setSalary(slr);
    }

    public Shift(String sCode) {
        this.code = sCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString(){
        return this.getCode() + ";" + this.getRole() + ";" + this.getTask();
    }
}
