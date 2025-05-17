package example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class RegistrationFormController {

    @FXML
    private Button btn;

    @FXML
    private TextField email;

    @FXML
    private TextField name;

    @FXML
    private TextField password;

    @FXML
    void onLogin(ActionEvent event) {
        System.out.println("hello ahmed");


        Window owner = name.getScene().getWindow();
        if(name.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Form Error!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your name");
            alert.initOwner(owner);
            alert.show();}

        if(email.getText().isEmpty())
        {
            Alert alert =new Alert(AlertType.ERROR);
            alert.setTitle("Email is required");
            alert.setHeaderText((null));
            alert.setContentText("Please enter your email");
            alert.initOwner(owner);
            alert.show();


        }

        if (password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Form Error!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your password");
            alert.initOwner(owner);
            alert.show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText(null);
        alert.setContentText("Welcome " + name.getText() + "!");
        alert.initOwner(owner);
        alert.show();

// else  
// {
//    " Welcome "+name.getText();
// }
  
}



}


