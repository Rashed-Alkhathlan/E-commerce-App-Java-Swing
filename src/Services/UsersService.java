package Services;

import Enums.StoreStatus;
import Enums.UserRole;
import Objects.*;
import Utils.Images;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public final class UsersService extends Service {

    private UsersService() {}

    public static int register(String firstName, String lastName, String phoneNumber, String email, String password) {
        UUID iD = UUID.randomUUID();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String date = LocalDate.now().format(formatter);

        int response = database.register(iD, firstName, lastName, phoneNumber, email.isEmpty() ? null : email, password, date);

        if (response == 0) {
            Cart oldCart = Main.getCurrentUser().getCart();
            Main.setCurrentUser(new Customer(iD, firstName, lastName, phoneNumber, email, password, date));
            Main.getCurrentUser().setCart(oldCart);

            for (Map.Entry<Product, Integer> product : oldCart.getProducts()) {
                for (int i = 0; i < product.getValue(); i++) {
                    database.addToCart(iD, product.getKey().getId());
                }
            }
        }

        return response;
    }

    public static boolean login(String phoneNumberOrEmail, String password) {
        User user = database.login(phoneNumberOrEmail, password);
        if (user != null) {
            Cart oldCart = Main.getCurrentUser().getCart();
            user.setCart(database.getCart(user.getId()));
            Main.setCurrentUser(user);
            for (Map.Entry<Product, Integer> product : oldCart.getProducts()) {
                for (int i = 0; i < product.getValue(); i++) {
                    database.addToCart(user.getId(), product.getKey().getId());
                    user.getCart().addProduct(product.getKey(), product.getValue());
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static int getUsersCount(UserRole role) {
        return database.getAllUsersCount(role);
    }

    public static int getUsersCount() {
        return database.getAllUsersCount(null);
    }

    public static ArrayList<User> getUsers() {
        return database.getAllUsers(null);
    }

    public static ArrayList<User> getUsers(UserRole role) {
        return database.getAllUsers(role);
    }

    public static ArrayList<Store> getManagerStores(UUID managerId) {
        return database.getAllStores(managerId, "");
    }

    public static void makeManager(User user) {
        UUID storeId = UUID.randomUUID();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String date = LocalDate.now().format(formatter);

        database.updateUser(user.getId(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getEmail(), user.getPassword(), UserRole.MANAGER);
        database.registerStore(storeId, user.getId(), "", "", date, StoreStatus.CLOSED, Images.getJPGImage("MissingImg"));

        Main.setCurrentUser(new Manager(user.getId(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getEmail(), user.getPassword(), user.getJoinedDate()));
        Main.getCurrentUser().setCart(user.getCart());
        ((Manager)Main.getCurrentUser()).setStoreId(storeId);
    }

    public static void deleteUser(UUID userId) {
        database.deleteUser(userId);
    }

    public static void registerAddress(String country, String city, String postalCode, String additionalInfo) {
        database.registerAddress(UUID.randomUUID(), Main.getCurrentUser().getId(),country, city, postalCode, additionalInfo);
    }

    public static ArrayList<Address> getAddresses(UUID userId) {
        return database.getAllAddresses(userId);
    }
}
