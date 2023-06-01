package org.dissan.restaurant.patterns.behavioral.observer;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubjectStates;

public class CookerTableObserver extends TableObserver{
    public CookerTableObserver() {
    }

    @Override
    public void update(TableObserver observer, TableBean tableBean, TableSubjectStates state) {
        OutStream.println("hello");
        if (!(observer instanceof CookerTableObserver) && state == TableSubjectStates.NEW_ORDER){
            OutStream.println(observer.getClass().getSimpleName() + " sent a new order from: " + tableBean.getTableInfo());
        }
    }

}
