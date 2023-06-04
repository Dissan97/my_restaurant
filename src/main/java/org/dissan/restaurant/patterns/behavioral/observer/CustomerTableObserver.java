package org.dissan.restaurant.patterns.behavioral.observer;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubjectStates;

public class CustomerTableObserver extends TableObserver{
    public CustomerTableObserver(String tableName) {
        super(tableName);
    }

    @Override
    public void update(TableObserver observer, TableBean tableBean, TableSubjectStates state) {

    }


}
