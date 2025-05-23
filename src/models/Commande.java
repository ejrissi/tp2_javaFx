package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commande {
    private int id;
    private int userId;            // Reference to the user who placed the order
    private List<CommandeItem> items; // Items in the order
    private Date orderDate;        // When the order was placed
    private double totalAmount;    // Total price of the order
    private String status;         // Order status (e.g., "Pending", "Completed", "Cancelled")

    // Default constructor
    public Commande() {
        this.items = new ArrayList<>();
    }

    // Constructor
    public Commande(int id, int userId, List<CommandeItem> items, Date orderDate, double totalAmount, String status) {
        this.id = id;
        this.userId = userId;
        this.items = items != null ? items : new ArrayList<>();
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<CommandeItem> getItems() {
        return items;
    }

    public void setItems(List<CommandeItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Add an item to the commande
    public void addItem(CommandeItem item) {
        this.items.add(item);
    }

    // toString method
    @Override
    public String toString() {
        return "Commande{" +
               "id=" + id +
               ", userId=" + userId +
               ", items=" + items +
               ", orderDate=" + orderDate +
               ", totalAmount=" + totalAmount +
               ", status='" + status + '\'' +
               '}';
    }
}