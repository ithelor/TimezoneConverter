package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.getIcons().add(new Image("resources/icon.png"));
        primaryStage.setTitle("Time Zone Converter");
        primaryStage.setScene(new Scene(root, 1060, 490));
        primaryStage.show();
//        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
