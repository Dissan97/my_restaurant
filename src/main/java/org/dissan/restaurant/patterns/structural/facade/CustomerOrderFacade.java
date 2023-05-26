package org.dissan.restaurant.patterns.structural.facade;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.controllers.OrderController;
import org.dissan.restaurant.models.dao.meal.MealItemDao;
import org.dissan.restaurant.patterns.behavioral.state.cli.CliState;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrderFacade {
    private TableBean tableBean;
    private OrderController orderController;
    private CliState state;

    public CustomerOrderFacade(CliState cliState) {
        this.state = cliState;

    }

    private List<String> getFreeTables(){
        List<String> tableList = new ArrayList<>();

        return null;
    }

    private void pullItems(){
        this.tableBean.setMealItem(MealItemDao.pullMenuItems());
    }

}
