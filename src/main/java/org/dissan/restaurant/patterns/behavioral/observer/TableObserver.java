package org.dissan.restaurant.patterns.behavioral.observer;

import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.TableState;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class TableObserver {
    public TableObserver(Table table) {

    }

    protected void outline(@NotNull TableState tableState){
        OutStream.println("order " + tableState.name().toLowerCase(Locale.ROOT));
    }

    public abstract void update(Table table);
}
