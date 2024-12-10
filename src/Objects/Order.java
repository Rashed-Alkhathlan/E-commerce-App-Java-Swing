package Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final String date;
    private double totalPrice = 0;
    private int numberOfProducts = 0;
    private final HashMap<Product, Integer> products = new HashMap<>();

    Order(UUID id, String date) {
        this.id = id;
        this.date = date;
    }

    public void addProduct(Product product, int quantity) {
        products.put(product, products.getOrDefault(product, 0) + quantity);
        totalPrice += product.getPrice() * quantity;
        numberOfProducts += quantity;
    }

    public ArrayList<HashMap.Entry<Product, Integer>> getProducts() {
        return new ArrayList<>(products.entrySet());
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
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
}
