package adbistju.system.music.collection;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;

import javafx.util.Duration;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main extends Application {

    //    public static String muss = "30112022Ig_2.mp3";
    public static String muss = "01. Don-t Blame Me (Split).mp3";
    public static String muss1 = "01. Butterfly (split).mp3";
    public static String muss2 = "01. Don-t Blame Me (Split).mp3";
    public static String muss3 = "John_Coltrane_-_Blue_Train_(-).mp3";
    private static Player player = new Player();

    private static Media media = new Media(new File(muss1).toURI().toString());
    private static MediaPlayer mediaPlayer = new MediaPlayer(media);

    @Override
    public void start(Stage stage) throws InvalidDataException, UnsupportedTagException, IOException {

        //by setting this property to true, the audio will be played
//        mediaPlayer.setAutoPlay(true);


            MusicFile musicFile = new MusicFile(muss1);

            mediaPlayer.setStartTime(Duration.millis(musicFile.getLengthInMilliseconds()/2));
        mediaPlayer.play();
        mediaPlayer.currentTimeProperty().get();
//        stage.setTitle("Playing Audio");
//        stage.show();


//        Panel panel = new Panel("This is the title");
//        String enteredByUser = "abcdef";
//        panel.setStyle("-fx-background-color: " + "linear-gradient(from 25% 25% to 100% 100%, #dc143c, #abcdef)");
//        BorderPane content = new BorderPane();
//
//
//        nextTrack(content);
//        odonButton(content);
//        stop(content);
//
//        panel.setBody(content);
//        Scene scene = new Scene(panel);
//        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
//        stage.setTitle("BootstrapFX");
//        stage.setScene(scene);
//        stage.sizeToScene();
//        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void odonButton(BorderPane content) {

//        Media media = new Media("http://path/file_name.mp3");
//
//
//        javafx.scene.media.MediaPlayer mediaPlayer = new javafx.scene.media.MediaPlayer(media);
//        mediaPlayer.setAutoPlay(true);
////        javafx.scene.media.MediaPlayer.setAutoPlay(true);

        try {
            MusicFile musicFile = new MusicFile(muss);
            MusicFile musicFile1 = new MusicFile(muss1);
            MusicFile musicFile2 = new MusicFile(muss2);
            MusicFile musicFile3 = new MusicFile(muss3);
            player.setPlaylist(List.of(musicFile, musicFile1, musicFile2, musicFile3));
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {

        }

        Image playButton = new Image("file:data/3.png");

        Button buttonIco = new Button("Hello BootstrapFX", new ImageView(playButton));
        buttonIco.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

//        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent e) {
//                buttonIco.setStyle("-fx-background-color: " + "linear-gradient(from 25% 25% to 100% 100%, #32cd32, #dc143c)");
////                System.out.println("Pause");
////                player.pauseMusic();
//            }
//        };
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                buttonIco.setStyle("-fx-background-color: " + "linear-gradient(from 25% 25% to 100% 100%, #dc143c, #32cd32);" + "-fx-background-radius: " + " 30");
                System.out.println("Play");
                Thread thread = new Thread(() -> {
                    player.stopMusic();
                    player.playList();
                });
                thread.start();
            }
        };
        buttonIco.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);

//        buttonIco.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        content.setLeft(buttonIco);
    }

    public void nextTrack(BorderPane content) {
        Button button = new Button("NextTrack");

        button.setStyle("-fx-background-color: " + "linear-gradient(from 25% 25% to 100% 100%, #dc143c, #32cd32);" + "-fx-background-radius: " + " 30");

        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("Next");
                player.nextMusic();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);

        button.getStyleClass().setAll("btn", "btn-danger");
        content.setCenter(button);

    }

    public void stop(BorderPane content) {
        Button button = new Button("stop");

        button.setStyle("-fx-background-color: " + "linear-gradient(from 50% 25% to 50% 100%, #dc143c, #32cd32);" + "-fx-background-radius: " + " 30");

        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("stop");
                player.stopMusic();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);

//        button.getStyleClass().setAll("btn", "btn-danger");
        content.setRight(button);

    }
}