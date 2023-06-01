package org.dissan.restaurant.patterns.behavioral.observer;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.TableState;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubject;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubjectStates;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class TableObserver {
    public TableObserver() {

    }

    protected void outline(@NotNull TableState tableState){
        OutStream.println("order " + tableState.name().toLowerCase(Locale.ROOT));
    }

    // TODO: 31/05/23 update the implementation flow
    public abstract void update(TableObserver observer, TableBean tableBean, TableSubjectStates state);
}
