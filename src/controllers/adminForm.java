package controllers;

import java.beans.Statement;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.Product;
import models.User;

public class adminForm implements Initializable {

   
    @FXML
    private Button btnImport;

    @FXML
    private ComboBox<String> Category;

    @FXML
    private RadioButton available;

    @FXML
    private TableColumn<Product, Boolean> availableProduct;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnProduit;

    @FXML
    private TextField decritption;

    @FXML
    private Button Btnadd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnUsers;

    @FXML
    private Button btnlogout;

    @FXML
    private TableColumn<Product, String> catProduct;

    @FXML
    private TableColumn<Product, String> descProduct;

    @FXML
    private TableColumn<Product, String> idProduct;

    @FXML
    private ImageView imageImport;

    @FXML
    private TextField name;

    @FXML
    private TableColumn<Product, String> nameProduct;

    @FXML
    private TableColumn<Product, String> preparationTimeProduct;

    @FXML
    private TextField price;

    @FXML
    private TableColumn<Product, Boolean> priceProduct;

    @FXML
    private TextField timePre;

    @FXML
    private Label username;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private TableView<Product> tableViewContent;

    // user
    @FXML
    private AnchorPane productspace;
  
    @FXML
    private AnchorPane UserSpace;
    
    @FXML
    private ComboBox<String> roleUserCombobox;

    @FXML
    private Button btnDeleteUser;
    @FXML
    private Button btnUpdateUser1;



@FXML
private TableView<User> tableViewUsers;
@FXML
private TableColumn<User, Integer> userIdColumn;
@FXML
private TableColumn<User, String> usernameColumn;
@FXML
private TableColumn<User, String> emailColumn;
@FXML
private TableColumn<User, String> roleColumn1;


@FXML
void OndeleteUser(ActionEvent event) {
    User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();

    // Validate selection
    if (selectedUser == null) {
        alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please select a user to delete.");
        alert.showAndWait();
        return;
    }

    // Show confirmation dialog
    alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to delete user " + selectedUser.getUsername() + "?");
    Optional<ButtonType> option = alert.showAndWait();

    if (option.get() != ButtonType.OK) {
        return; // User canceled deletion
    }

    // Delete user from database
    String deleteQuery = "DELETE FROM users WHERE id = ?";
    try {
        connection = DBConnection.connectDB();
        preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, selectedUser.getId());

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            userShowData(); // Refresh table
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User deleted successfully!");
            alert.showAndWait();
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to delete user.");
            alert.showAndWait();
        }
    } catch (Exception e) {
        e.printStackTrace();
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred while deleting the user.");
        alert.showAndWait();
    } finally {
        try {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


    @FXML
    void onUpdateUser(ActionEvent event) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        String selectedRole = roleUserCombobox.getValue();
    
        if (selectedUser == null) {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user from the table.");
            alert.showAndWait();
            return;
        }
    
        if (selectedRole == null) {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a role from the ComboBox.");
            alert.showAndWait();
            return;
        }
    
        String updateQuery = "UPDATE users SET role = ? WHERE id = ?";
        try {
            connection = DBConnection.connectDB();
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, selectedRole);
            preparedStatement.setInt(2, selectedUser.getId());
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                userShowData(); // Refresh table
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User role updated successfully!");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update user role.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the role.");
            alert.showAndWait();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void onUserSelected() {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            roleUserCombobox.setValue(selectedUser.getRole());
        } else {
            roleUserCombobox.setValue(null); // Clear the ComboBox if no user is selected
        }
    }

    @FXML
    void onSelectUserRole(ActionEvent event) {

    }

    @FXML
    void ListUser(ActionEvent event) {
        UserSpace.setVisible(true);
        productspace.setVisible(false);
        userShowData(); // Refresh user data when switching to UserSpace
    }
    
    @FXML
    void ListProdut(ActionEvent event) {
        productspace.setVisible(true);
        UserSpace.setVisible(false);
    }

    public void initializeRoleComboBox() {
        ObservableList<String> roles = FXCollections.observableArrayList("client", "serveur");
        roleUserCombobox.setItems(roles);
    }



// Fetch users with role "client" or "serveur"
public ObservableList<User> userDataList() {
    ObservableList<User> userList = FXCollections.observableArrayList();
    String sql = "SELECT * FROM users WHERE role IN ('client', 'serveur')";
    connection = DBConnection.connectDB();

    try {
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            User user = new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role")
            );
            userList.add(user);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return userList;
}

// Display users in tableViewUsers
public void userShowData() {
    ObservableList<User> userList = userDataList();

    userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    roleColumn1.setCellValueFactory(new PropertyValueFactory<>("role"));

    tableViewUsers.setItems(userList);
}






    private Alert alert;

    private Image image;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;

    public void inventroyUpdateBtn() {
        if (name.getText().isEmpty()
                || price.getText().isEmpty()
                || decritption.getText().isEmpty()
                || Category.getSelectionModel().getSelectedItem() == null
                || timePre.getText().isEmpty()
                || data.path == null) {
    
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            return;
        }
    
        // Get selected product
        Product selectedProduct = tableViewContent.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to update.");
            alert.showAndWait();
            return;
        }
    
        int productId = selectedProduct.getId(); // assuming you have getId() in your Product class
    
        String updateQuery = "UPDATE product SET image = ?, description = ?, name = ?, price = ?, preparationTime = ?, available = ?, category = ? WHERE id = ?";
    
        try {
            connection = DBConnection.connectyourdb("jdbc:mysql://localhost:3306/manageRestaurant");
    
            preparedStatement = connection.prepareStatement(updateQuery);
            String path;
            if (data.path != null) {
                // L'utilisateur a importé une nouvelle image → on échappe les \
                path = data.path.replace("\\", "\\\\");
            } else {
                // Pas de nouvelle image → on garde l'ancienne telle quelle
                path = selectedProduct.getImage();
            }
            
    
            preparedStatement.setString(1, path);
            preparedStatement.setString(2, decritption.getText());
            preparedStatement.setString(3, name.getText());
            preparedStatement.setDouble(4, Double.parseDouble(price.getText()));
            preparedStatement.setString(5, timePre.getText());
            preparedStatement.setBoolean(6, available.isSelected()); // assuming available is a RadioButton or CheckBox
            preparedStatement.setString(7, Category.getSelectionModel().getSelectedItem());
            preparedStatement.setInt(8, productId);
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                inventoryShowData(); // refresh table
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Product updated successfully!");
                alert.showAndWait();
                clearFields();
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void inventorySelectdata() {
        Product prodData = tableViewContent.getSelectionModel().getSelectedItem();

        int num = tableViewContent.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1)
            return;

        name.setText(prodData.getName());
        price.setText(String.valueOf(prodData.getPrice()));

        decritption.setText(prodData.getDescription());

        data.path = "File:" + prodData.getImage();
        Image image = new Image(data.path, 200, 121, false, true);
        imageImport.setImage(image);

        if (prodData.getPreparationTime() != null) {
            timePre.setText(prodData.getPreparationTime().toString()); // ex: 00:20:00
        } else {
            timePre.setText("");
        }

        if (prodData.getCategory() != null) {
            Category.setValue(prodData.getCategory());
        } else {
            Category.setValue(null);
        }

        available.setSelected(prodData.isAvailable());

    }

    public void inventoryAddBtn() {
        if (name.getText().isEmpty()
                || price.getText().isEmpty()
                || decritption.getText().isEmpty()
                || Category.getSelectionModel().getSelectedItem() == null
                || timePre.getText().isEmpty()
                || data.path == null) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            connection = DBConnection.connectyourdb("jdbc:mysql://localhost:3306/manageRestaurant");
            try {
                // Check if product with the same name already exists
                String checkProductQuery = "SELECT * FROM Product WHERE name = ?";
                preparedStatement = connection.prepareStatement(checkProductQuery);
                preparedStatement.setString(1, name.getText());
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Product exists
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Duplicate");
                    alert.setHeaderText(null);
                    alert.setContentText("Product with this name already exists!");
                    alert.showAndWait();
                } else {
                    // Insert new product
                    String insertData = "INSERT INTO Product (image, description, name, price, preparationTime, available, category) VALUES (?, ?, ?, ?, ?, ?, ?)";

                    preparedStatement = connection.prepareStatement(insertData);
                    String path = data.path;
                    path = path.replace("\\", "\\\\");

                    preparedStatement.setString(1, path);
                    preparedStatement.setString(2, decritption.getText());
                    preparedStatement.setString(3, name.getText());
                    preparedStatement.setDouble(4, Double.parseDouble(price.getText()));
                    preparedStatement.setString(5, timePre.getText());
                    preparedStatement.setBoolean(6, available.isSelected());
                    preparedStatement.setString(7, Category.getSelectionModel().getSelectedItem());

                    preparedStatement.executeUpdate();
                    inventoryShowData();
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Product added successfully!");
                    alert.showAndWait();
                    clearFields();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        name.clear();
        price.clear();
        decritption.clear();
        timePre.clear();
        available.setSelected(false);
        Category.getSelectionModel().clearSelection();
        imageImport.setImage(null);
        data.path = null;
    }

    public void inventoryImportBtn() {
        FileChooser openFile = new FileChooser();

        openFile.getExtensionFilters().add(new ExtensionFilter("Open image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(mainForm.getScene().getWindow());
        if (file != null) {
            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 200, 121, false, true);
            imageImport.setImage(image);

        }

    }




    public void inventoryDeleteBtn() {
    Product selectedProduct = tableViewContent.getSelectionModel().getSelectedItem();
    if (selectedProduct == null) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Please select a product to delete");
        alert.showAndWait();
        return;
    }

    alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation Message");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to delete this product?");
    Optional<ButtonType> option = alert.showAndWait();

    if (option.get() == ButtonType.OK) {
        try {
            connection = DBConnection.connectyourdb("jdbc:mysql://localhost:3306/manageRestaurant");
            String deleteQuery = "DELETE FROM product WHERE id = ?";
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, selectedProduct.getId());
            preparedStatement.executeUpdate();
            
            inventoryShowData();
            clearFields();
            
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Product deleted successfully!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    public ObservableList<Product> inventorydataList() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql = "select * from product";
        connection = DBConnection.connectyourdb("jdbc:mysql://localhost:3306/manageRestaurant");

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            Product data;
            while ((resultSet.next())) {
                data = new Product(resultSet.getInt("id"),
                        resultSet.getString("image"),
                        resultSet.getString("description"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("preparationTime"),
                        resultSet.getBoolean("available"),
                        resultSet.getString("category"));

                productList.add(data);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return productList;
    }

    private ObservableList<Product> inventoryListdata;

    public void inventoryShowData() {
        inventoryListdata = inventorydataList();

        idProduct.setCellValueFactory(new PropertyValueFactory<>("id"));

        nameProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        catProduct.setCellValueFactory(new PropertyValueFactory<>("category"));
        descProduct.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceProduct.setCellValueFactory(new PropertyValueFactory<>("price"));
        availableProduct.setCellValueFactory(new PropertyValueFactory<>("available"));
        preparationTimeProduct.setCellValueFactory(new PropertyValueFactory<>("preparationTime"));

        tableViewContent.setItems(inventoryListdata);

    }

    @FXML
    void onLogOut(ActionEvent event) {

        try {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you  sure you want to logout ?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                btnlogout.getScene().getWindow().hide();

                Parent rooot = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(rooot);
                stage.setTitle("Happy Bites");
                stage.setScene(scene);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayName() {
        String user = data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
    }

    public void CategoryLoad() {
        ObservableList<String> Cats = FXCollections.observableArrayList("Pizzas", "Pastas", "Burgers  & Sandwiches ",
                "Grilled Dishes / Meat", "Traditional Dishes", "Salads");
        Category.setItems(Cats);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayName();
        CategoryLoad();
        inventoryShowData();
        userShowData(); 
        initializeRoleComboBox(); // Initialize the ComboBox with roles
    // Add listener for tableViewUsers selection
    tableViewUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        onUserSelected();
    });
    }

}
