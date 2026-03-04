package Services;

import Objects.*;
import java.util.Map;

public final class CartService extends Service {

    private CartService() {}

    public static int addToCart(Product product, int quantity) {

        int inCart = 0;

        for (Map.Entry<Product, Integer> productInteger : Main.getCurrentUser().getCart().getProducts()) {
            if (productInteger.getKey().getId().equals(product.getId())) {
                inCart = productInteger.getValue();
            }
        }

        int allQuantity = quantity + inCart;
        if (!(allQuantity > product.getQuantity())) {

            Main.getCurrentUser().getCart().addProduct(product ,quantity);

            if (Main.isSignedIn()) {
                for (int i = 0; i < quantity; i++) {
                    database.addToCart(Main.getCurrentUser().getId(), product.getId());
                }
            }

            return 0;
        } else {
            return 1;
        }
    }

    public static void removeFromCart(Product product, int quantity) {
        for (int i = 0; i < quantity; i++) {
            Main.getCurrentUser().getCart().removeProduct(product);
            database.removeFromCart(Main.getCurrentUser().getId(), product.getId());
        }
    }

    public static void emptyCart() {
        for (Map.Entry<Product, Integer> product : Main.getCurrentUser().getCart().getProducts()) {
            int quantity = product.getValue();
            for (int i = 0; i < quantity; i++) {
                Main.getCurrentUser().getCart().removeProduct(product.getKey());
                database.removeFromCart(Main.getCurrentUser().getId(), product.getKey().getId());
            }
        }
    }
}
