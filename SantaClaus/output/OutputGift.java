package output;

import simulation.Gift;

public final class OutputGift {

    private final String productName;
    private final double price;
    private final String category;

    public OutputGift(final String productName, final double price, final String category) {
        this.productName = productName;
        this.price = price;
        this.category = category;
    }

    public OutputGift(final Gift gift) {
        this.productName = gift.getProductName();
        this.price = gift.getPrice();
        this.category = gift.getCategory();
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}
