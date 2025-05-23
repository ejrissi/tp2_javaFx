
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application
 {

   @Override
   public void start(Stage primaryStage) throws Exception {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
       Scene scene = new Scene(loader.load());
       primaryStage.setTitle(" Login");
       primaryStage.setMaxHeight(420);
       primaryStage.setMaxWidth(600);
       primaryStage.setScene(scene);
       primaryStage.show();
   }

   public static void main(String[] args) {
       launch(args);
   }
   
}
