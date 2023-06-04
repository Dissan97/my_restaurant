package org.dissan.restaurant.patterns.behavioral.observer;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubjectStates;

public class CookerTableObserver extends TableObserver{
    public CookerTableObserver(String tableName) {
        super(tableName);
    }

    @Override
    public void update(TableObserver observer, TableBean tableBean, TableSubjectStates state) {
        if (!(observer instanceof CookerTableObserver) && state == TableSubjectStates.NEW_ORDER){
            OutStream.println(observer.getClass().getSimpleName() + " sent a new order from: " + tableBean.getTableInfo());
        }
    }

}
