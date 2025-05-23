package controllers;

import connection.DBConnection;
import connection.data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Commande;
import models.CommandeItem;
import models.Product;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class UserController implements Initializable {

    @FXML private Button btnOrders;
    @FXML private Button btnUsers;
    @FXML private Button btnlogout;
    @FXML private AnchorPane mainForm;
    @FXML private Button menu;
    @FXML private AnchorPane navBar;
    @FXML private Label username;
    @FXML private ListView<Product> productListView;
    @FXML private ListView<CommandeItem> currentOrderListView;
    @FXML private Button submitOrderBtn;

    private Alert alert;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    // Store the current order
    private Commande currentOrder;

    // Initialize currentOrder
    private void initializeOrder() {
        currentOrder = new Commande();
        currentOrder.setUserId(data.userId);
        currentOrder.setOrderDate(new Date());
        currentOrder.setStatus("Pending");
        // Update ListView with empty items
        currentOrderListView.setItems(FXCollections.observableArrayList(currentOrder.getItems()));
    }

    // Fetch products from the database
    public ObservableList<Product> inventoryDataList() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product WHERE available = true";
        connection = DBConnection.connectyourdb("jdbc:mysql://localhost:3306/manageRestaurant");

        try {
            if (connection == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to connect to manageRestaurant database.");
                alert.showAndWait();
                return productList;
            }
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product data = new Product(
                    resultSet.getInt("id"),
                    resultSet.getString("image"),
                    resultSet.getString("description"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getString("preparationTime"),
                    resultSet.getBoolean("available"),
                    resultSet.getString("category")
                );
                productList.add(data);
            }
            System.out.println("Fetched " + productList.size() + " products from database.");
            if (productList.isEmpty()) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Products");
                alert.setHeaderText(null);
                alert.setContentText("No available products found in the database.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Error fetching products: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return productList;
    }

    // Set up the productListView with custom product cards
    public void setupProductListView() {
        productListView.setItems(inventoryDataList());

        productListView.setCellFactory(param -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                if (empty || product == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductCard.fxml"));
                        Parent card = loader.load();

                        // Access controls in ProductCard.fxml
                        Label productName = (Label) card.lookup("#productName");
                        Label productCategory = (Label) card.lookup("#productCategory");
                        Label productPrice = (Label) card.lookup("#productPrice");
                        Label productPreparationTime = (Label) card.lookup("#productPreparationTime");
                        Label productDescription = (Label) card.lookup("#productDescription");
                        ImageView productImage = (ImageView) card.lookup("#productImage");
                        Spinner<Integer> quantitySpinner = (Spinner<Integer>) card.lookup("#quantitySpinner");
                        Button addToOrderBtn = (Button) card.lookup("#addToOrderBtn");

                        // Set product data
                        productName.setText(product.getName());
                        productCategory.setText("Category: " + (product.getCategory() != null ? product.getCategory() : "N/A"));
                        productPrice.setText(String.format("Price: $%.2f", product.getPrice()));
                        productPreparationTime.setText("Prep Time: " + (product.getPreparationTime() != null ? product.getPreparationTime() : "N/A"));
                        productDescription.setText(product.getDescription() != null ? product.getDescription() : "No description");

                        // Set up quantity spinner (1-10 by default)
                        SpinnerValueFactory<Integer> valueFactory =
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
                        quantitySpinner.setValueFactory(valueFactory);

                        // Set image
                        if (product.getImage() != null && !product.getImage().isEmpty()) {
                            try {
                                Image image = new Image("file:" + product.getImage(), 100, 100, true, true);
                                productImage.setImage(image);
                            } catch (Exception e) {
                                System.out.println("Failed to load image for " + product.getName() + ": " + e.getMessage());
                                productImage.setImage(new Image("file:/path/to/default/image.jpg"));
                            }
                        } else {
                            productImage.setImage(new Image("file:/path/to/default/image.jpg"));
                        }

                        // Handle add to order button
                        addToOrderBtn.setOnAction(event -> {
                            int quantity = quantitySpinner.getValue();
                            CommandeItem item = new CommandeItem(product.getId(), product.getName(), quantity);
                            currentOrder.addItem(item);
                            currentOrderListView.setItems(FXCollections.observableArrayList(currentOrder.getItems()));

                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Order Update");
                            alert.setHeaderText(null);
                            alert.setContentText(quantity + " " + product.getName() + "(s) added to your order!");
                            alert.showAndWait();
                        });

                        setGraphic(card);
                    } catch (Exception e) {
                        e.printStackTrace();
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("UI Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to load product card: " + e.getMessage());
                        alert.showAndWait();
                    }
                }
            }
        });
    }

    // Setup currentOrderListView with delete and update quantity functionality
    public void setupCurrentOrderListView() {
        currentOrderListView.setCellFactory(param -> new ListCell<CommandeItem>() {
            @Override
            protected void updateItem(CommandeItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create HBox to hold label, spinner, and buttons
                    HBox hBox = new HBox(10);
                    Label label = new Label(item.getName());
                    Spinner<Integer> quantitySpinner = new Spinner<>();
                    Button updateButton = new Button("Update");
                    Button deleteButton = new Button("Delete");

                    // Style buttons
                    updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                    deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");

                    // Set up quantity spinner
                    SpinnerValueFactory<Integer> valueFactory =
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, item.getQuantity());
                    quantitySpinner.setValueFactory(valueFactory);

                    // Handle update button action
                    updateButton.setOnAction(event -> {
                        int newQuantity = quantitySpinner.getValue();
                        if (newQuantity != item.getQuantity()) {
                            item.setQuantity(newQuantity);
                            currentOrderListView.setItems(FXCollections.observableArrayList(currentOrder.getItems()));
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Order Update");
                            alert.setHeaderText(null);
                            alert.setContentText(item.getName() + " quantity updated to " + newQuantity + "!");
                            alert.showAndWait();
                        }
                    });

                    // Handle delete button action
                    deleteButton.setOnAction(event -> {
                        currentOrder.getItems().remove(item);
                        currentOrderListView.setItems(FXCollections.observableArrayList(currentOrder.getItems()));
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Order Update");
                        alert.setHeaderText(null);
                        alert.setContentText(item.getName() + " removed from order!");
                        alert.showAndWait();
                    });

                    hBox.getChildren().addAll(label, quantitySpinner, updateButton, deleteButton);
                    setGraphic(hBox);
                }
            }
        });
    }

    // Save and submit the order
    @FXML
    void submitOrder(ActionEvent event) {
        if (currentOrder == null || currentOrder.getItems().isEmpty()) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Your order is empty!");
            alert.showAndWait();
            return;
        }

        String userCheckSql = "SELECT id FROM users WHERE id = ?";
        String commandeSql = "INSERT INTO commande (user_id, order_date, total_amount, status) VALUES (?, ?, ?, ?)";
        String itemSql = "INSERT INTO commande_item (commande_id, product_id, name, quantity) VALUES (?, ?, ?, ?)";
        String priceSql = "SELECT price FROM product WHERE id = ?";

        Connection userDbConn = null;
        Connection manageDbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
           
            userDbConn = DBConnection.connectyourdb("jdbc:mysql://localhost:3306/user_db");
            if (userDbConn == null) throw new Exception("Failed to connect to user_db");
            stmt = userDbConn.prepareStatement(userCheckSql);
            stmt.setInt(1, currentOrder.getUserId());
            rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new Exception("Invalid user_id: User does not exist.");
            }
            rs.close();
            stmt.close();

            // Connect to manageRestaurant
            manageDbConn = DBConnection.connectyourdb("jdbc:mysql://localhost:3306/manageRestaurant");
            if (manageDbConn == null) throw new Exception("Failed to connect to manageRestaurant");

            // Calculate total amount
            double totalAmount = 0;
            stmt = manageDbConn.prepareStatement(priceSql);
            for (CommandeItem item : currentOrder.getItems()) {
                stmt.setInt(1, item.getProductId());
                rs = stmt.executeQuery();
                if (rs.next()) {
                    totalAmount += rs.getDouble("price") * item.getQuantity();
                } else {
                    throw new Exception("Invalid product_id: " + item.getProductId());
                }
                rs.close();
            }
            currentOrder.setTotalAmount(totalAmount);

            // Save commande
            stmt = manageDbConn.prepareStatement(commandeSql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, currentOrder.getUserId());
            stmt.setTimestamp(2, new java.sql.Timestamp(currentOrder.getOrderDate().getTime()));
            stmt.setDouble(3, currentOrder.getTotalAmount());
            stmt.setString(4, currentOrder.getStatus());
            stmt.executeUpdate();

            // Get commande ID
            rs = stmt.getGeneratedKeys();
            if (!rs.next()) {
                throw new Exception("Failed to retrieve commande ID.");
            }
            int commandeId = rs.getInt(1);
            rs.close();
            stmt.close();

            // Save items
            stmt = manageDbConn.prepareStatement(itemSql);
            for (CommandeItem item : currentOrder.getItems()) {
                stmt.setInt(1, commandeId);
                stmt.setInt(2, item.getProductId());
                stmt.setString(3, item.getName());
                stmt.setInt(4, item.getQuantity());
                stmt.executeUpdate();
            }

            // Reset current order
            initializeOrder();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Order submitted successfully!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to submit order: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (userDbConn != null) userDbConn.close();
                if (manageDbConn != null) manageDbConn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void ListUser(ActionEvent event) {
        // Your existing user list implementation
    }

    @FXML
    void listMenu(ActionEvent event) {
        productListView.setVisible(true);
        currentOrderListView.setVisible(true);
    }

    @FXML
    void onOrders(ActionEvent event) {
        productListView.setVisible(false);
        currentOrderListView.setVisible(false);
        // Add order history implementation later if needed
    }

    @FXML
    void onLogOut(ActionEvent event) {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to log out?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                btnlogout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Coeur de Cuisine");
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred during logout.");
            alert.showAndWait();
        }
    }

    public void displayName() {
        String user = data.username;
        if (user != null && !user.isEmpty()) {
            user = user.substring(0, 1).toUpperCase() + user.substring(1);
            username.setText(user);
        } else {
            username.setText("User");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayName();
        setupProductListView();
        setupCurrentOrderListView();
        initializeOrder();
    }
}