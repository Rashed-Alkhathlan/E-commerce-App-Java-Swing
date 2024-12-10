package GUI;

import Components.Dialog;
import Objects.Main;
import Services.UsersService;

public class CustomerAccount extends AccountPage {

    CustomerAccount() {
        super(true);
        initCustomerPage();
    }

    private void initCustomerPage() {
        setupCustomerMenu();
    }

    private void setupCustomerMenu() {
        menuPanel.addButton("Start your own Store!", "AccsImg", e -> {
            Dialog dialog = new Dialog("Confirmation", "<html>Continuing means you get to manage and configure your own store and products. Are sure you want to proceed?</html>");
            dialog.setVisible(true);
            dialog.addNoButtonAction(a -> dialog.dispose());
            dialog.addYesButtonAction(a -> {
                dialog.dispose();
                UsersService.makeManager(Main.getCurrentUser());
            });
        });
    }
}
