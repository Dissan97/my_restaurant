package org.dissan.restaurant.patterns.behavioral.observer.subjects;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.dao.table.TableDao;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TableSubject{
    protected static Map<String, TableSubject> tableObserverMap;
    protected static Map<String, Table> freeTables = null;
    protected TableBean tableBean;



    private static void loadTables(){
        if (freeTables == null) {
            freeTables = new HashMap<>();
            List<Table> tableList = TableDao.pullTables();
            for (Table t :
                    tableList) {
                freeTables.put(t.getTableCode(), t);
            }
        }
    }

    public abstract void attach(TableObserver tableObserver);
    public abstract void detach (TableObserver tableObserver);
    public abstract void notifyObservers(TableObserver observer, TableSubjectStates state);


    protected static void tableNoMoreFree(String tableName){
        if (freeTables == null){
            loadTables();
        }
        freeTables.remove(tableName);
    }

    public static List<String> getFreeTables(){
        loadTables();
        List<String> freeList = new ArrayList<>();
        for (Map.Entry<String, Table> entry:
             freeTables.entrySet()) {
            freeList.add(entry.getKey());
        }
        return freeList;
    }


    public TableBean getTableBean() {
        return this.tableBean;
    }
}
