package org.dissan.restaurant.patterns.behavioral.observer;

import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.TableState;

public class CookerTableObserver extends TableObserver{
    public CookerTableObserver(Table table) {
        super(table);
    }

    @Override
    public void update(Table table) {
        TableState tableState = table.getTableState();
        if (tableState == TableState.NEW_ORDER){
            outline(tableState);
        }
    }
}
