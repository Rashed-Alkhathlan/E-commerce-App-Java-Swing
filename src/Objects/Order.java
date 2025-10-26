package Objects;

import Enums.OrderStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final String date;
    private final double totalPrice;
    private final int numberOfProducts;
    private int numberOfItems;
    private final HashMap<Product, Integer> products;
    private final OrderStatus status;

    public Order(UUID id, String date, double totalPrice, HashMap<Product, Integer> products, OrderStatus status) {
        this.id = id;
        this.date = date;
        this.totalPrice = totalPrice;
        this.numberOfProducts = products.size();
        this.products = products;
        this.status = status;

        for (HashMap.Entry<Product, Integer> product : products.entrySet()) {
            numberOfItems += product.getValue();
        }
    }

    public ArrayList<HashMap.Entry<Product, Integer>> getProducts() {
        return new ArrayList<>(products.entrySet());
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public UUID getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
