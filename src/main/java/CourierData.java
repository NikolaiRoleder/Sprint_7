import pojo.Courier;

public class CourierData {

    public static Courier getRandomCourier() {
        return new Courier("wertyu", "8500", "qwertyui");
    }

    public static Courier getCourierWithSameLogin() {
        return new Courier("wertyu", "4500", "asdasd");
    }

    public static Courier getCourierWithoutFirstName() {
        return new Courier("wertsa", "2345", null);
    }

    public static Courier getCourierWithEmptyFirstName() {
        return new Courier("wertsa", "2345", "");
    }
}