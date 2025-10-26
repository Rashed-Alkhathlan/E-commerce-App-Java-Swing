package Services;

import Enums.OrderStatus;
import Objects.Main;
import Objects.Order;
import Objects.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class OrdersService extends Service {
    private OrdersService() {}

    public static boolean registerOrder(ArrayList<HashMap.Entry<Product, Integer>> items) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String date = LocalDate.now().format(formatter);
        return database.registerOrder(UUID.randomUUID(), date, Main.getCurrentUser().getId(), OrderStatus.PENDING, items);
    }

    public static ArrayList<Order> getOrders() {
        return database.getOrdersById(Main.getCurrentUser().getId());
    }
}
