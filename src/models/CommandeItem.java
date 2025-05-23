package models;

public class CommandeItem {
    private int productId; // Reference to the product
    private String name;   // Product name (for display and database storage)
    private int quantity;  // Number of units ordered

    // Default constructor
    public CommandeItem() {
    }

    // Constructor
    public CommandeItem(int productId, String name, int quantity) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CommandeItem{" +
               "productId=" + productId +
               ", name='" + name + '\'' +
               ", quantity=" + quantity +
               '}';
    }
}