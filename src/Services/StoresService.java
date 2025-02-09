package Services;

import Enums.StoreStatus;
import Objects.Manager;
import Objects.Store;
import Utils.Images;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public final class StoresService extends Service {

    private StoresService() {}

    public static Store getStore(UUID id) {
        return database.getStore(id);
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

    public static Manager getStoreOwner(UUID store_Id) {
        return database.getStoreOwner(store_Id);
    }

    public static void updateStore(UUID storeId, String name, String description, StoreStatus status, ImageIcon icon) {
        database.updateStore(storeId, name, description, status, icon);
    }

    public static void deleteStore(UUID id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String date = LocalDate.now().format(formatter);
        UUID ownerId = getStoreOwner(id).getId();

        database.deleteStore(id);
        database.registerStore(UUID.randomUUID(), ownerId, "", "", date, StoreStatus.CLOSED, Images.getJPGImage("MissingImg"));

    }
}
