package org.dissan.restaurant.patterns.creational.factory;

import org.dissan.restaurant.patterns.behavioral.observer.AttendantTableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.CookerTableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.CustomerTableObserver;
import org.dissan.restaurant.patterns.behavioral.observer.TableObserver;
import org.jetbrains.annotations.NotNull;

public class ObserverFactory {
    private ObserverFactory() {}

    public static TableObserver getInstance(@NotNull TableActor actor, String tableName){
        return switch (actor) {
            case ATTENDANT -> new AttendantTableObserver(tableName);
            case COOKER -> new CookerTableObserver(tableName);
            case CUSTOMER -> new CustomerTableObserver(tableName);
        };
    }
}
