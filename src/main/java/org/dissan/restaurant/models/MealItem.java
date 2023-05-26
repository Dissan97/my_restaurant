package org.dissan.restaurant.models;

import java.util.List;

public class MealItem {
    private String name;
    private List<String> ingredients;
    private double price;

    public MealItem(String name, List<String> ingredients, double price) {
        setName(name);
        setIngredients(ingredients);
        setPrice(price);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
