package org.dissan.restaurant.patterns.behavioral.observer;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubjectStates;

import java.util.logging.Logger;

public class CookerTableObserver extends TableObserver{
    public CookerTableObserver(String tableName) {
        super(tableName);
    }

    private final Logger logger = Logger.getLogger(TableObserver.class.getSimpleName());

    @Override
    public void update(TableObserver observer, TableBean tableBean, TableSubjectStates state) {
        if (!(observer instanceof CookerTableObserver) && state == TableSubjectStates.NEW_ORDER){
            logger.info(observer.getClass().getSimpleName() + " sent a new order from: " + tableBean.getTableInfo());
        }
    }

}
