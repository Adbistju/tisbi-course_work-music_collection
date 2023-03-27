package adbistju.system.music.collection;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    //    public static String muss = "30112022Ig_2.mp3";
    public static String muss = "01. Don-t Blame Me (Split).mp3";
    public static String muss1 = "01. Butterfly (split).mp3";
    public static String muss2 = "01. Don-t Blame Me (Split).mp3";
    public static String muss3 = "John_Coltrane_-_Blue_Train_(-).mp3";
    private static Player player = new Player();

    private static float count = 0;

//    private static Media media = new Media(new File(muss1).toURI().toString());
//    private static MediaPlayer mediaPlayer = new MediaPlayer(media);

    @Override
    public void start(Stage stage) throws FileNotFoundException {

        //by setting this property to true, the audio will be played
//        mediaPlayer.setAutoPlay(true);
//
//
//            MusicFile musicFile = new MusicFile(muss1);
//
//            mediaPlayer.setStartTime(Duration.millis(musicFile.getLengthInMilliseconds()/2));
//        mediaPlayer.play();
//        mediaPlayer.currentTimeProperty().get();
//        stage.setTitle("Playing Audio");
//        stage.show();


        Panel panel = new Panel("This is the title");
        panel.setStyle("-fx-background-color: " +
                "linear-gradient(from 0% 100% to 100% 0%, #3f87a6, #ebf8e1, #f69d3c, #e66465)"
        );


        BorderPane content = new BorderPane();

        BorderPane trackBox = new BorderPane();
//        trackBox.setStyle("-fx-background-color: #ebf8e1");


        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);

        List<MusicFile> tracks = player.getPlaylist();

        for (int i = 0; i < tracks.size(); i++) {
            ID3v1 id3v1Tag = tracks.get(i).getId3v1Tag();
            try {
                HBox hBox = new HBox(
                        new Text(String.valueOf(i)),
                        new Text("|"),
                        new Text(id3v1Tag.getTitle()),
                        new Text("|"),
                        new Text(id3v1Tag.getAlbum()),
                        new Text("|"),
                        new Text(id3v1Tag.getArtist()),
                        new Text("|"),
                        new Text(id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")")
                );
                hBox.setAlignment(Pos.BASELINE_LEFT);
                hBox.setSpacing(6);
//                hBox.setPadding(new Insets(3, 100, 3, 10));
                vbox.getChildren().add(hBox);
            } catch (Exception e) {

            }
        }

        trackBox.setCenter(vbox);

        content.setTop(trackBox);

        HBox divVolume = new HBox();
        divVolume.getChildren().setAll(sliderTrack());
        divVolume.setAlignment(Pos.TOP_LEFT);


        HBox div = new HBox();
        div.setSpacing(20);
        div.setAlignment(Pos.BASELINE_CENTER);
        BorderPane dive = new BorderPane();
        Button playTrackButton = playTrack();
        div.setMaxHeight(playTrackButton.getHeight());

        div.getChildren().addAll(new Button("1"), new Button("2"), new Button("3"), nextTrack(), playTrackButton, stopMusic(), divVolume);
        div.setPadding(new Insets(10, 10, 10, 10));

        dive.setBottom(sliderTrack());
        content.setCenter(dive);
        content.setBottom(div);

        panel.setBody(content);
        Scene scene = new Scene(panel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("BootstrapFX");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

    }

    public static void main(String[] args) {
        try {
            MusicFile musicFile = new MusicFile(muss);
            MusicFile musicFile1 = new MusicFile(muss1);
            MusicFile musicFile2 = new MusicFile(muss2);
            MusicFile musicFile3 = new MusicFile(muss3);
            player.setPlaylist(List.of(musicFile, musicFile1, musicFile2, musicFile3));
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {

        }
        launch();
    }

    public Slider sliderTrack() {
        Slider slider = new Slider(0, 100, 50);
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                count = (float) slider.getValue();
                slider.setValue(slider.getValue());
                Thread thread = new Thread(() -> {
                    int a = player.getIndexTrack();
                    player.stopMusic();
                    player.playList(a, (float) slider.getValue());
                });
                thread.start();
            }
        };
        slider.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);
                return slider;
    }

    public Button playTrack() {
        Button buttonIco = new Button("play");
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("Play");
                Thread thread = new Thread(() -> {
                    player.stopMusic();
                    player.playList();
                });
                thread.start();
            }
        };
        buttonIco.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);
        return buttonIco;
    }

    public Button nextTrack() {
        Button button = new Button("NxTr");
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("Next");
                player.nextMusic();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);
//        button.getStyleClass().setAll("btn", "btn-danger");
        return button;
    }

    public Button stopMusic() {
        Button button = new Button("stop");
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("stop");
                player.stopMusic();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);

        return button;
    }
}