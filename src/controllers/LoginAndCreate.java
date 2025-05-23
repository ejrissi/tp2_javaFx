 
package controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import connection.DBConnection;
import connection.data;
import javafx.animation.TranslateTransition;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginAndCreate  implements Initializable{

    @FXML
    private AnchorPane SlideCreate;

    @FXML
    private AnchorPane Slidesignup;

    @FXML
    private Button btnSignup;

    @FXML
    private Button btncreate;

    @FXML
    private Button btncreate1;

    @FXML
    private Button btnlogin;

    @FXML
    private TextField email;

    @FXML
    private TextField email2;

    @FXML
    private Label labelcreate;

    @FXML
    private Label labellogin;

    @FXML
    private Hyperlink lienForgot;

    @FXML
    private AnchorPane loginform;

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField password2;

    @FXML
    private ComboBox<String> role;



    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;


    @FXML
    void onSignUP(ActionEvent event) {
    
        if (name.getText().isEmpty() || password2.getText().isEmpty()
            || role.getSelectionModel().getSelectedItem() == null ||
            email2.getText().isEmpty()) {
    
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
    
        } else {
    
            String regData = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
    
            try {
                // Make sure your connection is established beforehand
                connection = DBConnection.connectDB(); // replace with your actual connection logic
    
                prepare = connection.prepareStatement(regData);
                prepare.setString(1, name.getText());
                prepare.setString(2, email2.getText());
                prepare.setString(3, password2.getText());
                prepare.setString(4, role.getValue());
    
                int result = prepare.executeUpdate();
    
                if (result > 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Account successfully created!");
                    alert.showAndWait();
    
                    // Clear fields after success
                    name.clear();
                    email2.clear();
                    password2.clear();
                    role.getSelectionModel().clearSelection();

                    TranslateTransition slider =new TranslateTransition ();

                    slider.setNode(SlideCreate);
                    slider.setToX(0);
                    slider.setDuration(Duration.seconds(.5));
                    slider.setOnFinished((ActionEvent e)->
                    {
                        btncreate1.setVisible(false);
                        btncreate.setVisible(true );
                        labelcreate.setVisible(true);
                        labellogin.setVisible(false);
                    });
        
                    slider.play();
                }
    
            } catch (Exception e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred while creating account.");
                alert.showAndWait();
            }
        }
    }
    
    @FXML
    void onlogin(ActionEvent event) {
        String emailInput = email.getText();
        String passwordInput = password.getText();
    
        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all login fields");
            alert.showAndWait();
            return;
        }
    
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
    
        try {
            connection = DBConnection.connectDB();
            prepare = connection.prepareStatement(query);
            prepare.setString(1, emailInput);
            prepare.setString(2, passwordInput);
    
            result = prepare.executeQuery();
    
            if (result.next()) {
                data.username = result.getString("username");
                data.userId=result.getInt("id");

                System.out.println("userid"+data.userId);


                String userName = result.getString("username");
                String userRole = result.getString("role");
                
    
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText(null);
                alert.setContentText("Welcome " + userName + " (" + userRole + ")");
                alert.showAndWait();
    
                // Determine which page to load based on role
                String fxmlPath = userRole.equals("admin") ? "/views/page.fxml" : "/views/userManagement.fxml";
                Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Coeur de Cuisine");
                stage.setScene(scene);
                stage.setMinHeight(780);
                stage.setMaxHeight(790);
                stage.setMinWidth(1270);
                stage.setMaxWidth(1280);
    
                stage.show();
                btnlogin.getScene().getWindow().hide();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid email or password.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while trying to log in.");
            alert.showAndWait();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
   



    













    @FXML
   
    public void switchForm(ActionEvent event)
    {
        TranslateTransition slider =new TranslateTransition ();


        if(event.getSource()== btncreate)
        {
            slider.setNode(SlideCreate);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));
            slider.setOnFinished((ActionEvent e)->
            {
                btncreate1.setVisible(true);
                btncreate.setVisible(false);
                labelcreate.setVisible(false);
                labellogin.setVisible(true);
            });

            slider.play();
        }else if(event.getSource()==btncreate1)
        {
            slider.setNode(SlideCreate);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));
            slider.setOnFinished((ActionEvent e)->
            {
                btncreate1.setVisible(false);
                btncreate.setVisible(true );
                labelcreate.setVisible(true);
                labellogin.setVisible(false);
            });

            slider.play();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
           ObservableList<String> roles = FXCollections.observableArrayList( "Client", "Serveur");
        role.setItems(roles);
    }


}