import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        // Uncomment and update the path if you have a stylesheet
        // scene.getStylesheets().add(getClass().getResource("/path/to/your/stylesheet.css").toExternalForm());

        primaryStage.setTitle("DnD Board Builder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}