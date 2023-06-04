package org.dissan.restaurant.patterns.creational.factory;

import org.dissan.restaurant.patterns.behavioral.observer.AttendantTableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.CookerTableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.CustomerTableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObserverFactory {
    private ObserverFactory() {}

    public static @Nullable TableObserver getInstance(@NotNull TableActor actor, String tableName){
        switch (actor) {
            case ATTENDANT:
                return new AttendantTableObserver(tableName);
            case COOKER:
                return new CookerTableObserver(tableName);
            case CUSTOMER:
                return new CustomerTableObserver(tableName);
            default:
                return null;
        }
    }
}
