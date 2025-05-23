package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.User;
import connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserManagementController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> userIdCol;
    @FXML private TableColumn<User, String> userNameCol;
    @FXML private TableColumn<User, String> userEmailCol;
    @FXML private TableColumn<User, String> userRoleCol;
    @FXML private TableColumn<User, Void> actionCol;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        userEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        userRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        loadUsers();
        addDeleteButtonToTable();
    }

    private void loadUsers() {
        userList.clear();
        try (Connection conn = DBConnection.connectDB()) {
            String query = "SELECT * FROM users";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                userList.add(new User(
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                ));
            }
            userTable.setItems(userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDeleteButtonToTable() {
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    private void deleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete User: " + user.getUsername());
        alert.setContentText("Are you sure you want to delete this user?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (Connection conn = DBConnection.connectDB()) {
                    String query = "DELETE FROM users WHERE email = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, user.getEmail());
                    stmt.executeUpdate();
                    loadUsers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleBack() {
        // try {
        //     Parent root = FXMLLoader.load(getClass().getResource("/views/adminForm.fxml"));
        //     Stage stage = (Stage) btnBack.getScene().getWindow();
        //     stage.setScene(new Scene(root));
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }
}