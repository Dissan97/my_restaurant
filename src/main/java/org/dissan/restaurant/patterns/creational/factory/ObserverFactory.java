package org.dissan.restaurant.patterns.creational.factory;

import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.patterns.behavioral.observer.AttendantTableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.CookerTableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.CustomerTableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObserverFactory {
    private ObserverFactory() {}

    public static @Nullable TableObserver getInstance(@NotNull TableActor actor){
        switch (actor) {
            case ATTENDANT:
                return new AttendantTableObserver();
            case COOKER:
                return new CookerTableObserver();
            case CUSTOMER:
                return new CustomerTableObserver();
            default:
                return null;
        }
    }
}
