package org.dissan.restaurant.models;

public class Table {

    private String tableCode;
    private int customers;
    private final OrderCheck check;
    private Employee attendant;
    private TableState tableState;

    public Table(String tc) {
        this(tc, 0);
    }

    public Table(String tableName, int customerNum) {
        this.tableCode = tableName;
        this.customers = customerNum;
        check = new OrderCheck(this);
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public int getCustomers() {
        return customers;
    }

    public void setCustomers(int customers) {
        this.customers = customers;
    }


    public Employee getAttendant() {
        return attendant;
    }

    public void setAttendant(Employee attendant) {
        this.attendant = attendant;
    }

    public OrderCheck getCheck() {
        return check;
    }

    public TableState getTableState() {
        return tableState;
    }

    public void setTableState(TableState tableState) {
        this.tableState = tableState;
    }
}

