package application;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main extends Application {
    private SceneManager sceneManager;

    @Override
    public void start(Stage primaryStage) throws IOException, URISyntaxException {
        sceneManager = new SceneManager(primaryStage);
        sceneManager.initializeScenes();
        String basePath = new File("").getAbsolutePath();
        Image icon = new Image("file:/"+basePath+"/icon/appIcon.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(sceneManager.getImportScene());
        primaryStage.setTitle("Textify");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
