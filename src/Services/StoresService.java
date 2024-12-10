package Services;

import Enums.StoreStatus;
import Objects.Manager;
import Objects.Store;
import Objects.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public final class StoresService extends Service {

    private StoresService() {}

    public static Store getStore(UUID ownerId) {
        return database.getStore(ownerId);
    }

    public static ArrayList<Store> getStores() {
        return database.getAllStores(null, "");
    }

    public static ArrayList<Store> getStores(UUID managerId) {
        return database.getAllStores(managerId, "");
    }

    public static ArrayList<Store> getStores(String searchTerm) {
        return database.getAllStores(null, searchTerm);
    }

    public static ArrayList<Store> getStores(UUID managerId, String searchTerm) {
        return database.getAllStores(managerId, searchTerm);
    }

    public static int getStoresCount() {
        return database.getAllStoresCount(null);
    }

    public static int getStoresCount(StoreStatus status) {
        return database.getAllStoresCount(status);
    }

    public static int getStoreProductCount(UUID store_id) {
        return database.getAllProductsCount(store_id);
    }

    public static ArrayList<User> getStoreManagers(UUID store_id) {
        return database.getAllStoreManagers(store_id);
    }

    public static String getStoreManagersNames(UUID store_id) {
        return getStoreManagers(store_id).stream()
                .map(manager -> manager.getFirstName() + " " + manager.getLastName())
                .collect(Collectors.joining(", "));
    }

    public static Manager getStoreOwner(UUID store_Id) {
        return database.getStoreOwner(store_Id);
    }

    public static void updateStore(UUID storeId, String name, String description, StoreStatus status, ImageIcon icon) {
        database.updateStore(storeId, name, description, status, icon);
    }
}
