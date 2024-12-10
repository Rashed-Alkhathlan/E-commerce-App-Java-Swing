package Database;

import Enums.StoreStatus;
import Enums.UserRole;
import Objects.*;
import Utils.Images;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

@SuppressWarnings("CallToPrintStackTrace")
public class DBUtils {
    private final Connection conn = DBConnector.getInstance().getConnection();

    public User login(String identifier, String password) {
        String query = "SELECT * FROM Users WHERE (email = ? OR phone_number = ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, identifier); // Matches email
            stmt.setString(2, identifier); // Matches phone number
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (password.equals(storedPassword)) {
                        UUID iD = UUID.fromString(rs.getString("id"));
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");
                        String phoneNumber = rs.getString("phone_number");
                        String email = rs.getString("email");
                        String joinedDate = rs.getString("joined_date");
                        String role = rs.getString("role");
                        if (role.equalsIgnoreCase(UserRole.ADMIN.toString())) {
                            return new Admin(iD, firstName, lastName, phoneNumber, email, password, joinedDate);
                        } else if (role.equalsIgnoreCase(UserRole.MANAGER.toString())) {
                            return new Manager(iD, firstName, lastName, phoneNumber, email, password, joinedDate);
                        } else {
                            return new Customer(iD, firstName, lastName, phoneNumber, email, password, joinedDate);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int register(UUID iD, String firstName, String lastName, String phoneNumber, String email, String password, String date) {
        String query = "INSERT INTO Users (id, first_name, last_name, phone_number, email, password, joined_date, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iD.toString());
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, phoneNumber);
            stmt.setString(5, email);
            stmt.setString(6, password);
            stmt.setString(7, date);
            stmt.setString(8, "CUSTOMER");
            stmt.executeUpdate();
            return 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                if (e.getMessage().contains("phone_number")) {
                    return -1;
                } else if (e.getMessage().contains("email")) {
                    return 1;
                }
            }
            e.printStackTrace();
            return 2;
        }
    }

    public void addToCart(UUID userId, UUID productId) {
        String query = "INSERT INTO Cart (user_id, product_id, quantity) VALUES (?, ?, 1) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + 1";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId.toString());
            stmt.setString(2, productId.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFromCart(UUID userId, UUID productId) {
        String updateQuery = "UPDATE Cart SET quantity = quantity - 1 WHERE user_id = ? AND product_id = ? AND quantity > 1";
        String deleteQuery = "DELETE FROM Cart WHERE user_id = ? AND product_id = ? AND quantity = 1";

        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setString(1, userId.toString());
            updateStmt.setString(2, productId.toString());
            int rowsUpdated = updateStmt.executeUpdate();

            // If no rows were updated, the quantity was already 1, so delete the product
            if (rowsUpdated == 0) {
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                    deleteStmt.setString(1, userId.toString());
                    deleteStmt.setString(2, productId.toString());
                    deleteStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void registerAddress(UUID id, UUID userId, String country, String city, String postalCode, String additionalInfo) {
        String query = "INSERT INTO Address (id, user_id, country, city, postal_code, additional_info) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            stmt.setString(2, userId.toString());
            stmt.setString(3, country);
            stmt.setString(4, city);
            stmt.setString(5, postalCode);
            stmt.setString(6, additionalInfo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerStore(UUID id, UUID ownerId, String name, String description, String creationDate, StoreStatus status, ImageIcon image) {
        String query = "INSERT INTO Stores (id, name, description, creation_date, status, main_image_icon, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setString(4, creationDate);
            stmt.setString(5, status.name());
            stmt.setBytes(6, Images.imageIconToByteArray(image));
            stmt.setString(7, ownerId.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerProduct(UUID id, UUID storeId, String name, String description, double price, int quantity, ImageIcon image) {
        String query = "INSERT INTO Products (id, store_id, name, description, price, quantity, main_image_icon) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            stmt.setString(2, storeId.toString());
            stmt.setString(3, name);
            stmt.setString(4, description);
            stmt.setDouble(5, price);
            stmt.setInt(6, quantity);
            stmt.setBytes(7, Images.imageIconToByteArray(image));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getAllUsers(UserRole role) {
        String query;
        ArrayList<User> users = new ArrayList<>();
        if (role != null) {
            query = "SELECT * FROM users WHERE role = ?";
        } else {
            query = "SELECT * FROM users";
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            if (role != null) {
                stmt.setString(1, role.name());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID iD = UUID.fromString(rs.getString("id"));
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String phoneNumber = rs.getString("phone_number");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String joinedDate = rs.getString("joined_date");
                    String roleString = rs.getString("role");
                    if (UserRole.valueOf(roleString.toUpperCase()) == UserRole.ADMIN) {
                        users.add(new Admin(iD, firstName, lastName, phoneNumber, email, password, joinedDate));
                    } else if (UserRole.valueOf(roleString.toUpperCase()) == UserRole.MANAGER) {
                        users.add(new Manager(iD, firstName, lastName, phoneNumber, email, password, joinedDate));
                    } else {
                        users.add(new Customer(iD, firstName, lastName, phoneNumber, email, password, joinedDate));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public ArrayList<Store> getAllStores(UUID managerId, String searchTerm) {
        String query;
        ArrayList<Store> stores = new ArrayList<>();

        if (managerId != null && searchTerm.isEmpty()) {
            query = "SELECT Stores.* FROM Stores JOIN Managed ON Stores.id = Managed.store_id WHERE Managed.user_id = ?";
        } else {
            query = "SELECT * FROM stores WHERE name LIKE CONCAT('%', ?, '%') OR description LIKE CONCAT('%', ?, '%')";
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            if (managerId != null && searchTerm.isEmpty()) {
                stmt.setString(1, managerId.toString());
            } else {
                stmt.setString(1, searchTerm);
                stmt.setString(2, searchTerm);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String creationDate = rs.getString("creation_date");
                    String status = rs.getString("status");
                    byte[] image = rs.getBytes("main_image_icon");
                    UUID ownerId = UUID.fromString(rs.getString("owner_id"));

                    stores.add(new Store(id, ownerId ,name, description, creationDate, Images.byteArrayToImageIcon(image), StoreStatus.valueOf(status.toUpperCase())));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stores;
    }

    public ArrayList<Product> getAllProducts(UUID storeId, String searchTerm) {
        String query;
        ArrayList<Product> products = new ArrayList<>();

        if (storeId != null && searchTerm.isEmpty()) {
            query = "SELECT * FROM Products WHERE store_id = ?";
        } else if (storeId == null) {
            query = "SELECT * FROM Products WHERE name LIKE CONCAT('%', ?, '%') OR description LIKE CONCAT('%', ?, '%')";
        } else {
            query = "SELECT * FROM Products WHERE store_id = ? AND (name LIKE CONCAT('%', ?, '%') OR description LIKE CONCAT('%', ?, '%'))";
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            if (storeId != null & searchTerm.isEmpty()) {
                stmt.setString(1, storeId.toString());
            } else if (storeId == null) {
                stmt.setString(1, searchTerm);
                stmt.setString(2, searchTerm);
            } else {
                stmt.setString(1, storeId.toString());
                stmt.setString(2, searchTerm);
                stmt.setString(3, searchTerm);
            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    UUID iD = UUID.fromString(rs.getString("id"));
                    UUID pStoreId = UUID.fromString(rs.getString("store_id"));
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    byte[] image = rs.getBytes("main_image_icon");

                    Product product = new Product(iD, pStoreId, name, description, price, quantity, Images.byteArrayToImageIcon(image));
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public Store getStore(UUID id) {
        String query = "SELECT * FROM Stores WHERE (owner_id = ? OR id = ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            stmt.setString(2, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    UUID store_id = UUID.fromString(rs.getString("id"));
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String creationDate = rs.getString("creation_date");
                    String status = rs.getString("status");
                    byte[] image = rs.getBytes("main_image_icon");
                    UUID ownerId = UUID.fromString(rs.getString("owner_id"));

                    return new Store(store_id, ownerId ,name, description, creationDate, Images.byteArrayToImageIcon(image), StoreStatus.valueOf(status.toUpperCase()));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Product getProduct(UUID productId) {
        String query = "SELECT * FROM Products WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, productId.toString());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    UUID iD = UUID.fromString(rs.getString("id"));
                    UUID storeId = UUID.fromString(rs.getString("store_id"));
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    byte[] image = rs.getBytes("main_image_icon");

                    return new Product(iD, storeId, name, description, price, quantity, Images.byteArrayToImageIcon(image));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Manager getStoreOwner(UUID storeId) {
        String query = "SELECT Users.* FROM Users " +
                "JOIN Stores ON Users.id = Stores.owner_id " +
                "WHERE Stores.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, storeId.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String phoneNumber = rs.getString("phone_number");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String joinedDate = rs.getString("joined_date");

                    return new Manager(id, firstName, lastName, phoneNumber, email, password, joinedDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<User> getAllStoreManagers(UUID storeId) {
        ArrayList<User> managers = new ArrayList<>();
        String query = "SELECT Users.* FROM Users JOIN Managed ON Users.id = Managed.user_id WHERE Managed.store_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, storeId.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String phoneNumber = rs.getString("phone_number");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String joinedDate = rs.getString("joined_date");

                    managers.add(new Manager(id, firstName, lastName, phoneNumber, email, password, joinedDate));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return managers;
    }


    public ArrayList<Address> getAllAddresses(UUID userId) {
        String query = "SELECT * FROM Address WHERE user_id = ?";
        ArrayList<Address> addresses = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    String country = rs.getString("country");
                    String city = rs.getString("city");
                    String postalCode = rs.getString("postal_code");
                    String additionalInfo = rs.getString("additional_info");

                    addresses.add(new Address(id, country, city, postalCode, additionalInfo));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addresses;
    }


    public int getAllUsersCount(UserRole role) {
        String query;
        int userCount = 0;

        if (role != null) {
            query = "SELECT COUNT(*) AS user_count FROM Users WHERE role = ?";
        } else {
            query = "SELECT COUNT(*) AS user_count FROM Users";
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            if (role != null) {
                stmt.setString(1, role.name());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userCount = rs.getInt("user_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userCount;
    }

    public int getAllStoresCount(StoreStatus status) {
        String query;
        int storeCount = 0;

        if (status != null) {
            query = "SELECT COUNT(*) AS store_count FROM Stores WHERE status = ?";
        } else {
            query = "SELECT COUNT(*) AS store_count FROM Stores";
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            if (status != null) {
                stmt.setString(1, status.name());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    storeCount = rs.getInt("store_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return storeCount;
    }

    public int getAllProductsCount(UUID storeId) {
        String query;
        int productCount = 0;

        if (storeId != null) {
            query = "SELECT COUNT(*) AS product_count FROM Products WHERE store_id = ?";
        } else {
            query = "SELECT COUNT(*) AS product_count FROM Products";
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            if (storeId != null) {
                stmt.setString(1, storeId.toString());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    productCount = rs.getInt("product_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productCount;
    }

    public Cart getCart(UUID userId) {
        String query = "SELECT * FROM Cart WHERE user_id = ?";
        Cart cart = new Cart();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String iD = rs.getString("product_id");
                    int quantity = rs.getInt("quantity");
                    cart.addProduct(getProduct(UUID.fromString(iD)), quantity);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cart;
    }

    public void updateUser(UUID id, String firstName, String lastName, String phoneNumber, String email, String password, UserRole role) {
        String query = "UPDATE Users SET first_name = ?, last_name = ?, phone_number = ?, email = ?, password = ?, role = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, role.name());
            stmt.setString(7, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStore(UUID id, String name, String description, StoreStatus status, ImageIcon mainImageIcon) {
        String query = "UPDATE Stores SET name = ?, description = ?, status = ?, main_image_icon = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, status.name());
            stmt.setBytes(4, Images.imageIconToByteArray(mainImageIcon));
            stmt.setString(5, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(UUID id, String name, String description, double price, int quantity, ImageIcon image) {
        String query = "UPDATE Products SET name = ?, description = ?, price = ?, quantity = ?, main_image_icon = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setBytes(5, Images.imageIconToByteArray(image));
            stmt.setString(6, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAddress(UUID id, String country, String city, String postalCode, String additionalInfo) {
        String query = "UPDATE Address SET country = ?, city = ?, postal_code = ?, additional_info = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, country);
            stmt.setString(2, city);
            stmt.setString(3, postalCode);
            stmt.setString(4, additionalInfo);
            stmt.setString(5, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(UUID id) {
        String query = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStore(UUID id) {
        String query = "DELETE FROM Stores WHERE owner_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(UUID id) {
        String query = "DELETE FROM Products WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAddress(UUID id) {
        String query = "DELETE FROM Address WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
