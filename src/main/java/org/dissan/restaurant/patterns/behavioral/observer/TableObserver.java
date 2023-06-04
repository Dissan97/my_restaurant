package org.dissan.restaurant.patterns.behavioral.observer;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.ConcreteTableSubject;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubject;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubjectStates;


public abstract class TableObserver {

    protected TableSubject tableSubject;
    public TableObserver(String tableName) {
        tableSubject = ConcreteTableSubject.getSubject(tableName);
    }


    // TODO: 31/05/23 update the implementation flow
    public abstract void update(TableObserver observer, TableBean tableBean, TableSubjectStates state);
}
