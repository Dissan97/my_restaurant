package org.dissan.restaurant.patterns.behavioral.observer.subjects;

import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;

import java.util.List;
import java.util.Map;

public abstract class TableSubject {
    protected Map<Table, List<TableObserver>> tableObserverMap;
    abstract void attach(Table table, TableObserver tableObserver);
    abstract void detach(Table table, TableObserver tableObserver);
    abstract void notifyObservers(Table table);
}
