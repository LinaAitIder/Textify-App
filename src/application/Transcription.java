package application;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.SpeechModel;
import com.assemblyai.api.resources.transcripts.types.Transcript;
import com.assemblyai.api.resources.transcripts.types.TranscriptLanguageCode;
import com.assemblyai.api.resources.transcripts.types.TranscriptOptionalParams;
import com.assemblyai.api.resources.transcripts.types.TranscriptStatus;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Transcription {
	
	//Affichage de resultat de la fonction transcribe() dans resultScene
    public void handleTranscription(Stage primaryStage, Scene loadingScene, Scene resultScene) {
        try {
            String transcriptionResult = transcribe();
            Platform.runLater(() -> {
                try {
                    TextArea resultTextArea = (TextArea) ((VBox) ((BorderPane) resultScene.getRoot()).getCenter())
                            .getChildren().get(1);
                    resultTextArea.setText(transcriptionResult);

                    primaryStage.setScene(resultScene);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorHandler.showErrorDialog("Error during UI update", e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> errorHandler.showErrorDialog("Error during transcription", e.getMessage()));
        }
    }

   
    public String transcribe() throws IOException, InterruptedException {
        String outputDirectory = "videos/";
        File downloadedFile = new File(outputDirectory + "maVideo.mp3");

        if (!downloadedFile.exists()) {
            throw new FileNotFoundException("Please Verify the link you provided! ");
        }

        String audioFilePath = downloadedFile.getAbsolutePath();

        AssemblyAI client = AssemblyAI.builder().apiKey("2fa806274b514ea1a9a17ad130a3f5fc").build();

        TranscriptOptionalParams params = TranscriptOptionalParams.builder()
                .speechModel(SpeechModel.NANO)
                .speakerLabels(true)
                .languageDetection(true)
                .languageCode(TranscriptLanguageCode.EN)
                .build();

        Transcript transcript = client.transcripts().transcribe(new File(audioFilePath));

        if (transcript.getStatus().equals(TranscriptStatus.ERROR)) {
            throw new RuntimeException("Transcription failed: " + transcript.getError().orElse("Unknown error"));
        }

        String transcriptionText = transcript.getText().orElse("");

        File textFile = new File(outputDirectory + "myTxt.txt");
        if (!textFile.exists()) {
            textFile.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(textFile))) {
            writer.write(transcriptionText);
        }

        return transcriptionText;
    }

}