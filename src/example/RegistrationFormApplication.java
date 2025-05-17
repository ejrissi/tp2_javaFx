package example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegistrationFormApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
    Parent root =
    FXMLLoader.load(getClass().getResource("/example/Registration_form.fxml"));
    primaryStage.setTitle("Registration Form FXML Application");
   
    Scene scene = new Scene(root, 800, 500);
    scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.show();
    }
    public static void main(String[] args) {
    launch(args);
    }
}