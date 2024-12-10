package Objects;

import Enums.UserRole;

import java.util.UUID;

public class Manager extends User {
    private UUID storeId;

    public Manager(UUID iD, String firstName, String lastName, String phoneNumber, String email, String password, String joinedDate) {
        super(iD, firstName, lastName, phoneNumber, email, password, joinedDate);
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    @Override
    public UserRole getRole() {
        return UserRole.MANAGER;
    }
}
