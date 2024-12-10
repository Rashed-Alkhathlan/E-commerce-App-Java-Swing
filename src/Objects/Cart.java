package Objects;

import java.util.*;

public class Cart {
    private double totalPrice = 0;
    private int numberOfProducts = 0;
    private final HashMap<Product, Integer> products = new HashMap<>();

    public void addProduct(Product product, int quantity) {
        products.put(product, products.getOrDefault(product, 0) + quantity);
        totalPrice += product.getPrice() * quantity;
        numberOfProducts += quantity;
    }

    public void removeProduct(Product product) {
        UUID productId = product.getId();
        if (products.containsKey(product)) {
            if (products.get(product) <= 1) {
                totalPrice -= product.getPrice();
                products.remove(product);
            } else {
                totalPrice -= product.getPrice();
                products.put(product, products.get(product) - 1);
            }
        }
        numberOfProducts--;
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
}
