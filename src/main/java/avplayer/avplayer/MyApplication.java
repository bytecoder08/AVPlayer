package avplayer.avplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MyApplication extends Application {

    //public MyController myController = new MyController();

    public Stage myStage;
    public Scene myScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("start method is called myStage is called primary stage .......");
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(MyController.class.getResource("my-view.fxml"));
        Parent firstScreenRoot = loader.load();
        myScene=new Scene(firstScreenRoot);
        myStage.setTitle("AV Player");
        myStage.getIcons().add(new Image("file:icons\\logo.png"));
        myStage.setScene(myScene);
        myStage.show();

    }


    public static void main(String[] args) {

        launch();
    }
}
