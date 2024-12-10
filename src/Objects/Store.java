package Objects;

import Enums.StoreStatus;

import javax.swing.*;
import java.util.UUID;

public class Store {
    private final UUID iD;
    private final UUID ownerId;
    private String name;
    private String description;
    private final String creationDate;
    private ImageIcon mainImageIcon;
    private StoreStatus status;

    public Store(UUID iD, UUID ownerId, String name, String description, String creationDate, ImageIcon mainImageIcon, StoreStatus status) {
        this.iD = iD;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.mainImageIcon = mainImageIcon;
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMainImageIcon(ImageIcon mainImageIcon) {
        this.mainImageIcon = mainImageIcon;
    }

    public void setStatus(StoreStatus status) {
        this.status = status;
    }


    public UUID getId() {
        return iD;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public StoreStatus getStatus() {
        return status;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public ImageIcon getMainImageIcon() {
        return mainImageIcon;
    }
}
