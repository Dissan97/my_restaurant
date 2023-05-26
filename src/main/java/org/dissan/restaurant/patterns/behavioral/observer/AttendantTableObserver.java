package org.dissan.restaurant.patterns.behavioral.observer;

import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.TableState;
import org.jetbrains.annotations.NotNull;

public class AttendantTableObserver extends TableObserver{
    public AttendantTableObserver(Table table) {
        super(table);
    }

    @Override
    public void update(@NotNull Table table) {
        TableState tableState = table.getTableState();
        if (tableState == TableState.READY_TO_DELIVER){
            outline(tableState);
        }
    }
}
