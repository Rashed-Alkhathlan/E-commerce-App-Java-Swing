package GUI;

import javax.swing.*;

public class CheckoutPage extends Page {
    private final JButton completeCheckoutButton = new JButton();
    private final JButton addDiscountButton = new JButton();
    private final JButton addAddressButton = new JButton();
    private final JButton BackButton = new JButton();

    private final JLabel subtotalLabel = new JLabel();
    private final JLabel totalAfterDiscountLabel = new JLabel();
    private final JLabel totalLabel = new JLabel();


    CheckoutPage() {
        initPage();
    }

    @Override
    protected void initPage() {

    }

    protected void actionListener() {

    }
}
