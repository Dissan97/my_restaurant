package org.dissan.restaurant.patterns.behavioral.observer.subjects;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConcreteTableSubject extends TableSubject{

    private final List<TableObserver> observerList;

    private ConcreteTableSubject(String tableName) {
        this.observerList = new ArrayList<>();
        tableBean = new TableBean();
        tableBean.setTable(freeTables.get(tableName));
    }


    @Override
    public void attach(TableObserver tableObserver) {
        observerList.add(tableObserver);
    }

    @Override
    public void detach(TableObserver tableObserver) {
        observerList.remove(tableObserver);
    }

    @Override
    public void notifyObservers(TableObserver observer, TableSubjectStates state) {
        for (TableObserver obs:
             observerList) {
            if (obs != observer){
                obs.update(observer, tableBean, state);
            }
        }
    }

    public static TableSubject getSubject(String tableName){
        if (tableObserverMap == null){
            tableObserverMap = new HashMap<>();
        }
        ConcreteTableSubject concreteTableSubject = new ConcreteTableSubject(tableName);
        if (tableObserverMap.putIfAbsent(tableName, concreteTableSubject) == null){
            tableNoMoreFree(tableName);
        }

        return tableObserverMap.get(tableName);

    }


}
