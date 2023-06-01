package org.dissan.restaurant.patterns.behavioral.observer.subjects;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.dao.table.TableDao;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConcreteTableSubject extends TableSubject{

    private static ConcreteTableSubject tableSubject = null;

    private ConcreteTableSubject() {
        tableObserverMap = new HashMap<>();
        freeTables = TableDao.getTables();
    }

    @Override
    public void attach(Table table,TableObserver tableObserver) {
        if (!tableObserverMap.containsKey(table)){
            List<TableObserver> observerList = new ArrayList<>();
            observerList.add(tableObserver);
            tableObserverMap.put(table, observerList);
            TableBean tableBean = new TableBean();
            tableBean.setTable(table);
            tableBeanList.add(tableBean);
            freeTables.remove(table.getTableCode());
        }else {
            tableObserverMap.get(table).add(tableObserver);
        }
    }

    @Override
    public void detach(Table table, TableObserver tableObserver) {
        if (tableObserverMap.containsKey(table)){
            tableObserverMap.get(table).remove(tableObserver);
            if (tableObserverMap.get(table).isEmpty()){
                freeTables.add(table.getTableCode());
            }
        }
    }

    @Override
    public void notifyObservers(Table table, TableObserver observer, TableSubjectStates state) {
        List<TableObserver> observerList = this.tableObserverMap.get(table);
        for (TableObserver obs:
             observerList) {
            if (obs != observer)
                obs.update(observer, getRelativeTable(table.getTableCode()),  state);
        }
    }

    public static TableSubject getSubject(){
        if (tableSubject == null){
            tableSubject = new ConcreteTableSubject();
            tableSubject.tableBeanList = new ArrayList<>();
        }
        return tableSubject;
    }

    @Override
    public List<String> getFreeTables() {
        return freeTables;
    }
}
