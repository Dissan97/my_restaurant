package org.dissan.restaurant.models;

public class Shift {

    private String code;
    private String task;
    private String role;

    public Shift() {
    }

    public Shift(String cd, String tsk, String rl) {
        this.setCode(cd);
        this.setTask(tsk);
        this.setRole(rl);
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

    @Override
    public String toString(){
        return this.getCode() + ";" + this.getRole() + ";" + this.getTask();
    }
}
