package Objects;
import Enums.UserRole;

import java.util.UUID;

public abstract class User {

    private final UUID iD;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private final String joinedDate;
    private Cart cart = new Cart();

    public User(UUID iD, String firstName, String lastName, String phoneNumber, String email, String password, String joinedDate) {
        this.iD = iD;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.joinedDate = joinedDate;
    }

    public abstract UserRole getRole();

    // Update methods
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getters
    public UUID getId() {
        return iD;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}