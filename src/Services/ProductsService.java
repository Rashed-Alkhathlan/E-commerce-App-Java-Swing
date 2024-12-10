package Services;

import Objects.Product;

import javax.swing.*;
import java.util.ArrayList;
import java.util.UUID;

public final class ProductsService extends Service {

    private ProductsService() {};

    public static ArrayList<Product> getProducts() {
        return database.getAllProducts(null, "");
    }

    public static ArrayList<Product> getProducts(UUID storeId) {
        return database.getAllProducts(storeId, "");
    }

    public static ArrayList<Product> getProducts(String searchTerm) {
        return database.getAllProducts(null, searchTerm);
    }

    public static ArrayList<Product> getProducts(UUID storeId, String searchTerm) {
        return database.getAllProducts(storeId, searchTerm);
    }

    public static int getProductCount() {
        return database.getAllProductsCount(null);
    }

    public static int getProductCount(UUID storeId) {
        return database.getAllProductsCount(storeId);
    }

    public static void registerProduct(UUID storeId, String name, String description, double price, int quantity, ImageIcon icon) {
        database.registerProduct(UUID.randomUUID(), storeId, name, description, price, quantity, icon);
    }
}
