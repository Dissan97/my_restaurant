package org.dissan.restaurant.patterns.behavioral.observer;

import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.TableState;

public class CustomerTableObserver extends TableObserver{
    public CustomerTableObserver(Table table) {
        super(table);
    }

    @Override
    public void update(Table table) {
        TableState tableState = table.getTableState();
        if (tableState == TableState.IN_DELIVERY){
            outline(tableState);
        }
    }
}
