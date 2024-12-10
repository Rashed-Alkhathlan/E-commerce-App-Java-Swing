package Objects;

import GUI.MyFrame;
import java.util.UUID;

import Services.Service;
import Services.UsersService;

public class Main {
    private static boolean isSignedIn = false;
    private static User currentUser = new Customer(UUID.randomUUID(),"", "", "", "", "", "");

    private final static String themeColor1 = "#243B55";
    private final static String themeColor2 = "#141E30";

    public static void setCurrentUser(User user) {
        if (user == null) {
            currentUser = new Customer(UUID.randomUUID(),"", "", "", "", "", "");
            isSignedIn = false;
        } else {
            currentUser = user;
            isSignedIn = true;
        }
        MyFrame.reloadPage();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static String getThemeColor1() {
        return themeColor1;
    }

    public static String getThemeColor2() {
        return themeColor2;
    }

    public static boolean isSignedIn() {
        return isSignedIn;
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        new Service();
        new MyFrame();
        //UsersService.login("0550818870", "Rashed123*");
        //UsersService.login("0550881846", "Khaled123*");
        //MyFrame.reloadPage();
    }
}
