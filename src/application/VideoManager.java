package application;


import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

public class VideoManager {

    public static String downloadYouTubeVideo(String videoUrl) throws IOException, InterruptedException {
        try {
        	//Creation d'un ficher temporaire ou on stocke l'audio de ytb video 
            File fileToDelete = new File("videos/maVideo.mp3");
            if (fileToDelete.exists()) {
                FileUtils.forceDelete(fileToDelete);
            }
            System.out.println("Fichier supprimé avec succès !");
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de la suppression du fichier : " + e.getMessage());
            e.printStackTrace();
        }
        String outputDirectory = "videos/";
        String basePath = new File("").getAbsolutePath();
        
        //Téléchargement d'audio a partir de lien ytb donne par l'utilisateur dans le dialog 
        String[] command = {basePath+"/tools/yt-dlp.exe", "--extract-audio", "--audio-format", "mp3", "-o", outputDirectory + "maVideo",
                videoUrl };

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Téléchargement réussi !");

        } else {
            System.err.println("Erreur de téléchargement. Code : " + exitCode);
        }
        return null;
    }

    public static void showUploadOptions(TextInputDialog dialog, Stage ps, Scene rs, Scene ls , Scene errs) {
        dialog.setTitle("Upload Youtube Video");
        dialog.setHeaderText("Here we go! ");
        dialog.setContentText("Enter a YouTube link :");
        Transcription tr = new Transcription();
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(link -> {
            try {
                if (!link.isEmpty()) {
                    ps.setScene(ls);
                    Task<Void> downloadTranscribeTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            downloadYouTubeVideo(link);
                            tr.handleTranscription(ps,ls,rs);
                            return null;
                        }
                    };

                    // Utilisation de thread pour eviter le blockage de UI
                    new Thread(downloadTranscribeTask).start();
                    downloadTranscribeTask .setOnSucceeded(e -> ps.setScene(rs));
                    downloadTranscribeTask .setOnFailed(e -> {
                        errorHandler.showErrorDialog("Error", "Failed to complete transcription.");
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
                errorHandler.showErrorDialog("Upload Error", "Failed to upload or transcribe.");
            }
        });
    }

}