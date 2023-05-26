package org.dissan.restaurant.patterns.behavioral.observer.subjects;

import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConcreteTableSubject extends TableSubject{

    private static ConcreteTableSubject tableSubject = null;

    private ConcreteTableSubject() {
        tableObserverMap = new HashMap<>();
    }

    @Override
    void attach(Table table,TableObserver tableObserver) {
        if (!tableObserverMap.containsKey(table)){
            List<TableObserver> observerList = new ArrayList<>();
            observerList.add(tableObserver);
            tableObserverMap.put(table, observerList);
        }else {
            tableObserverMap.get(table).add(tableObserver);
        }
    }

    @Override
    void detach(Table table, TableObserver tableObserver) {
        if (tableObserverMap.containsKey(table)){
            tableObserverMap.get(table).remove(tableObserver);
        }
    }

    @Override
    void notifyObservers(Table table) {
        List<TableObserver> observerList = tableObserverMap.get(table);
        for (TableObserver obs:
             observerList) {
            obs.update(table);
        }
    }

    public static TableSubject getSubject(){
        if (tableSubject == null){
            tableSubject = new ConcreteTableSubject();
        }
        return tableSubject;
    }

}
