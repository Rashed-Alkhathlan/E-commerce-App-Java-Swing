package Objects;

import Enums.UserRole;

import java.util.UUID;

public class Admin extends User {
    public Admin(UUID iD, String firstName, String lastName, String phoneNumber, String email, String password, String joinedDate) {
        super(iD, firstName, lastName, phoneNumber, email, password, joinedDate);
    }

    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }
}
