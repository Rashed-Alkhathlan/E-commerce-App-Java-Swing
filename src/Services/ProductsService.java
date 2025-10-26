package Services;

import Enums.StoreStatus;
import Objects.Product;

import javax.swing.*;
import java.util.ArrayList;
import java.util.UUID;

public final class ProductsService extends Service {

    private ProductsService() {};

    public static Product getProduct(UUID productId) {
        return database.getProduct(productId);
    }

    public static ArrayList<Product> getProducts(String searchTerm, UUID storeId, StoreStatus status) {
        return database.getProducts(searchTerm, storeId, status);
    }

    public static int getProductsCount(StoreStatus status) {
        try {
            return database.getProductsCount(null, status);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getProductsCount(UUID storeId) {
        try {
            return database.getProductsCount(storeId, null);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getProductsCount() {
        try {
            return database.getProductsCount(null, null);
        } catch (Exception e) {
            return 0;
        }
    }

    public static void registerProduct(UUID storeId, String name, String description, double price, int quantity, ImageIcon icon) {
        database.registerProduct(UUID.randomUUID(), storeId, name, description, price, quantity, icon);
    }

    public static void deleteProduct(UUID id) {
        database.deleteProduct(id);
    }
}
