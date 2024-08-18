import pojo.Order;

import java.util.List;

public class OrderData {

    public static Order getRandomOrder(List<String> color) {
        return new Order("pusundy", "olivoch", "pushkina-kolotushkina 21", "akademicheskaya", "8 999 888 76 54", 5, "2020-06-06", "ne obyazatelno", color);
    }
}