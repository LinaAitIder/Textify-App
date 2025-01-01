package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SceneManager {
    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;

    private final Stage primaryStage;
    private Scene importScene, transcribeScene, summaryScene, resultScene, loadingScene, errorScene;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initializeScenes() {
        String css = getClass().getResource("application.css").toExternalForm();

        importScene = createImportScene();
        transcribeScene = createTranscribeScene();
        summaryScene = createSummaryScene();
        resultScene = createResultScene();
        loadingScene = createLoadingScene();
        errorScene = createErrorScene();

        importScene.getStylesheets().add(css);
        transcribeScene.getStylesheets().add(css);
        summaryScene.getStylesheets().add(css);
        resultScene.getStylesheets().add(css);
        loadingScene.getStylesheets().add(css);
        errorScene.getStylesheets().add(css);
    }

    public Scene getImportScene() {
        return importScene;
    }

    private Scene createScene(javafx.scene.layout.Pane root) {
        return new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    }

    private Scene createImportScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        

        Image videoImageFile = new Image("https://img.icons8.com/?size=80&id=0sONqLDKQv5N&format=png");
        ImageView videoImage = new ImageView(videoImageFile);
        videoImage.setFitWidth(100);
        videoImage.setFitHeight(100);
        videoImage.setPreserveRatio(true);

        Label importLabel = new Label("Hello there, ");
        Label subLabel = new Label("Welcome to Textify!");

        content.getChildren().addAll(videoImage, importLabel, subLabel);

        HBox navigation = new HBox(10);
        navigation.setAlignment(Pos.CENTER);

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> primaryStage.setScene(transcribeScene));

        navigation.getChildren().add(nextButton);

        root.getChildren().addAll(content, navigation);

        return createScene(root);
    }


    private Scene createTranscribeScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        Image audioImageFile = new Image("https://img.icons8.com/?size=80&id=cGkCdBqLKjs5&format=png");
        ImageView audioImage = new ImageView(audioImageFile);
        audioImage.setFitWidth(100);
        audioImage.setFitHeight(100);
        audioImage.setPreserveRatio(true);

        Label transcribeLabel = new Label("Simply paste a YouTube link,");
        Label descriptionLabel = new Label("and let our app download and convert your video to text effortlessly!");

        content.getChildren().addAll(audioImage, transcribeLabel, descriptionLabel);

        HBox navigation = new HBox(10);
        navigation.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(importScene));

        Button finishButton = new Button("Finish");
        finishButton.setOnAction(e -> primaryStage.setScene(summaryScene));

        navigation.getChildren().addAll(backButton, finishButton);

        root.getChildren().addAll(content, navigation);

        return createScene(root);
    }




    private Scene createSummaryScene() {
        BorderPane summaryPane = new BorderPane();

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        Image uploadImageFile = new Image("https://img.icons8.com/?size=80&id=pE3H3KbttZgL&format=png");
        ImageView uploadImage = new ImageView(uploadImageFile);
        uploadImage.setFitWidth(100);
        uploadImage.setFitHeight(100);
        uploadImage.setPreserveRatio(true);

        Label titleLabel = new Label("Just provide a YouTube link,");

        Label descriptionLabel = new Label("and we'll handle the rest – from download to text conversion!");


        Button uploadButton = new Button("Upload Video");

        TextInputDialog dialog = new TextInputDialog();
        uploadButton.setOnAction(e -> {
            VideoManager.showUploadOptions(dialog, primaryStage, resultScene, loadingScene, errorScene);
        });
        Button convertButton = new Button("Show text");
        convertButton.setOnAction(e -> {
            primaryStage.setScene(resultScene);
        });

        content.getChildren().addAll(uploadImage, titleLabel, descriptionLabel, uploadButton, convertButton);
        summaryPane.setCenter(content);

        return createScene(summaryPane);
    }


    private Scene createResultScene() {
        BorderPane resultPane = new BorderPane();

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        Label resultLabel = new Label("Transcription Result");

        TextArea resultTextArea = new TextArea();
        resultTextArea.setWrapText(true);
        resultTextArea.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(summaryScene));

        content.getChildren().addAll(resultLabel, resultTextArea, backButton);
        resultPane.setCenter(content);

        return createScene(resultPane);
    }



    private Scene createLoadingScene() {
        VBox loadingBox = new VBox(20);
        loadingBox.setAlignment(Pos.CENTER);

        Label loadingLabel = new Label("Processing, please wait...");
        loadingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle("-fx-pref-width: 50px; -fx-pref-height: 50px;");

        loadingBox.getChildren().addAll(loadingLabel, progressIndicator);

        return createScene(loadingBox);
    }


    public Scene createErrorScene() {
        VBox errorLayout = new VBox();
        errorLayout.setSpacing(10);
        errorLayout.setAlignment(Pos.CENTER);
        errorLayout.getStyleClass().add("root");

        Label errorMessage = new Label("An error occurred during the download.");
        errorMessage.getStyleClass().add("label");

        Button closeButton = new Button("Close");
        closeButton.getStyleClass().add("button");
        closeButton.setOnAction(e -> {
            primaryStage.close();
        });

        errorLayout.getChildren().addAll(errorMessage, closeButton);

        Scene errorScene = new Scene(errorLayout, 400, 200);
        errorScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        return errorScene;
    }




}
