package Services;

import Objects.*;

public final class CartService extends Service {

    private CartService() {}

    public static void addToCart(Product product, int quantity) {
        Main.getCurrentUser().getCart().addProduct(product ,quantity);
        if (Main.isSignedIn()) {
            database.addToCart(Main.getCurrentUser().getId(), product.getId());
        }
    }

    public static void removeFromCart(Product product, int quantity) {
        for (int i = 0; i < quantity; i++) {
            Main.getCurrentUser().getCart().removeProduct(product);
            database.removeFromCart(Main.getCurrentUser().getId(), product.getId());
        }
    }
}
