package Objects;

import javax.swing.*;
import java.util.UUID;

public class Product {
    private final UUID id;
    private final UUID storeId;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private ImageIcon mainImageIcon;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product product = (Product) obj;
        return id != null && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Product(UUID id, UUID storeId, String name, String description, double price, int quantity, ImageIcon mainImageIcon) {
        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.mainImageIcon = mainImageIcon;
    }

    public void changesetName(String name) {
        this.name = name;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changePrice(double price) {
        this.price = price;
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void changeMainImageIcon(ImageIcon mainImageIcon) {
        this.mainImageIcon = mainImageIcon;
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public ImageIcon getMainImageIcon() {
        return mainImageIcon;
    }
}