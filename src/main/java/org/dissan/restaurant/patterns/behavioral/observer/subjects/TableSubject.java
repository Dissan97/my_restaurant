package org.dissan.restaurant.patterns.behavioral.observer.subjects;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;

import java.util.List;
import java.util.Map;

public abstract class TableSubject implements TableCustomerApi {
    protected Map<Table, List<TableObserver>> tableObserverMap;
    protected List<String> freeTables;
    protected List<TableBean> tableBeanList;

    public Map<Table, List<TableObserver>> getTableObserverMap() {
        return tableObserverMap;
    }

    public TableBean getRelativeTable(String table){
            for (TableBean tb:
                 tableBeanList) {
                if (tb.getTableName().equalsIgnoreCase(table)){
                    return tb;
                }
        }
        return null;
    }

    public abstract void attach(Table table, TableObserver tableObserver);
    public abstract void detach(Table table, TableObserver tableObserver);
    public abstract void notifyObservers(Table table, TableObserver observer, TableSubjectStates state);
}
