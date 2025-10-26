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

    public static ArrayList<Store> getStores(String searchTerm, StoreStatus status) {
        return database.getStores(searchTerm, status);
    }

    public static int getStoreCount() {
        try {
            return database.getStoresCount(null);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getStoreCount(StoreStatus status) {
        try {
            return database.getStoresCount(status);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getStoreProductCount(UUID store_id) {
        return database.getProductsCount(store_id, null);
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
