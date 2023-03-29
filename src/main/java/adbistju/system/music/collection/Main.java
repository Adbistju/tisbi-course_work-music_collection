package adbistju.system.music.collection;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.NamedArg;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main extends Application {

    //    public static String muss = "30112022Ig_2.mp3";
    public static String muss = "01. Don-t Blame Me (Split).mp3";
    public static String muss1 = "01. Butterfly (split).mp3";
    public static String muss2 = "30112022Ig_2.mp3";
    public static String muss3 = "John_Coltrane_-_Blue_Train_(-).mp3";
    private static Player player = new Player();
    private static VolumeController volumeController = new VolumeController();

    private static float count = 0;

//    private static Media media = new Media(new File(muss1).toURI().toString());
//    private static MediaPlayer mediaPlayer = new MediaPlayer(media);

    Panel panel = new Panel("This is the title");

    @Override
    public void start(Stage stage) throws FileNotFoundException {
//        stage.setMinWidth(6000);
//        stage.setMaxHeight(6000);
//        stage.setWidth(1400);
//        stage.setHeight(700);
//        stage.setResizable(false);
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



        panel.setStyle(/*"-fx-background-color: " +
                "linear-gradient(from 0% 100% to 100% 0%, #3f87a6, #ebf8e1, #f69d3c, #e66465)"*/
                "-fx-background-color: transparent"
        );

        BorderPane content = new BorderPane();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add(0, "style.css");

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);

        List<MusicFile> tracks = player.getPlaylist();


        for (int i = 0; i < tracks.size(); i++) {
            Button currentTrack = new Button(String.valueOf(i));
            int finalI = i;
            EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    player.stopMusic();
                    panel.setText(player.getPlaylist().get(finalI).getPath());
                    Thread thread = new Thread(() -> {
                        player.playList(finalI, -1);
                    });
                    thread.start();
                }
            };
            currentTrack.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);

            try {
                ID3v1 id3v1Tag = tracks.get(i).getId3v1Tag();
                HBox hBox = new HBox(
                        currentTrack,
                        new Text("|"),
                        new Text(Optional.ofNullable(id3v1Tag.getTitle()).orElse("   ")),
                        new Text("|"),
                        new Text(Optional.ofNullable(id3v1Tag.getAlbum()).orElse("   ")),
                        new Text("|"),
                        new Text(Optional.ofNullable(id3v1Tag.getArtist()).orElse("   ")),
                        new Text("|"),
                        new Text((Optional.ofNullable(id3v1Tag.getGenre()).orElse(-1)) + " (" + Optional.ofNullable(id3v1Tag.getGenreDescription()).orElse("   ") + ")")
                );
                hBox.setAlignment(Pos.BASELINE_LEFT);
                hBox.setSpacing(6);
                vbox.getChildren().add(hBox);
            } catch (Exception e) {
                HBox hBox = new HBox(
                        currentTrack,
                        new Text("|"),
                        new Text(tracks.get(i).getPath())
                );
                hBox.setAlignment(Pos.BASELINE_LEFT);
                hBox.setSpacing(6);
                vbox.getChildren().add(hBox);
            }
        }


        scrollPane.setContent(vbox);
        scrollPane.setMaxHeight(300);
        content.setTop(scrollPane);

        HBox divVolume = new HBox();
        divVolume.getChildren().setAll(sliderVolume());
        divVolume.setAlignment(Pos.TOP_LEFT);


        HBox div = new HBox();
        div.setSpacing(20);
        div.setAlignment(Pos.BASELINE_CENTER);
        BorderPane dive = new BorderPane();
        Button playTrackButton = playTrack();
        div.setMaxHeight(playTrackButton.getHeight());

        div.getChildren().addAll(new Button("1"), prevTrack(), pauseMusic(), nextTrack(), playTrackButton, stopMusic(), divVolume);
        div.setPadding(new Insets(10, 10, 10, 10));

        dive.setBottom(sliderTrack());
        content.setCenter(dive);
        content.setBottom(div);

        panel.setBody(content);
//        panel.setStyle("-fx-background-color: #3f87a6"/* +
//                        "                linear-gradient(from 0% 100% to 100% 0%, #3f87a6, #ebf8e1, #f69d3c, #e66465)"*/);
        panel.setStyle("-fx-background-color: rgba(56, 176, 209, 0)");
        panel.setEffect(new BoxBlur(10,10 ,3 ));
//        GaussianBlur glow = new GaussianBlur();

        Panel background = new Panel();

        background.setStyle("-fx-background-color: " +
                "linear-gradient(from 0% 100% to 100% 0%, #3f87a6, #ebf8e1, #f69d3c, #e66465)");
        background.setStyle("-fx-background-image: url(Home.png)");
        background.setBackground(
                new Background(
                        new BackgroundImage(
                                new Image("Home.png"), BackgroundRepeat.SPACE, BackgroundRepeat.SPACE, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT
                        )
                )
        );
//        background.setEffect(new GaussianBlur(1));

        background.setBody(panel);

        Scene scene = new Scene(background, 1400, 900, Color.TRANSPARENT);
//        scene.getStylesheets().add("style.css");
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
//        scene.setFill(Color.TRANSPARENT);
//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("BootstrapFX");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaxHeight(900);
        stage.setMaxWidth(1400);

        stage.sizeToScene();
        stage.show();

    }
    /** https://coderlessons.com/tutorials/java-tekhnologii/vyuchi-javafx/javafx-effekty */

    public static void main(String[] args) {
        try {
            MusicFile musicFile = new MusicFile(muss);
            MusicFile musicFile1 = new MusicFile(muss1);
            MusicFile musicFile2 = new MusicFile(muss2);
            MusicFile musicFile3 = new MusicFile(muss3);
            player.setPlaylist(List.of(musicFile, musicFile1, musicFile2, musicFile3, musicFile, musicFile1, musicFile2, musicFile3, musicFile, musicFile1, musicFile2, musicFile3));
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {

        }
        launch();
    }

    public Slider sliderTrack() {
        Slider slider = new Slider(0, 100, 50);
        slider.getStylesheets().add(0, "style.css");

        slider.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100.0 ;
            return String.format(Locale.US,"-slider-track-color: linear-gradient(to left, -slider-filled-track-color 0%%, "
                            + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    100-percentage, 100-percentage);
        }, slider.valueProperty(), slider.minProperty(), slider.maxProperty()));
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                count = (float) slider.getValue();
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

    public Slider sliderVolume() {
        Slider slider = new Slider(0, 100, 50);
        slider.getStylesheets().add(0, "style.css");
        volumeController.volumeControl(50);

        slider.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100.0 ;
            return String.format(Locale.US,"-slider-track-color: linear-gradient(to right, -slider-filled-track-color 0%%, "
                            + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, slider.valueProperty(), slider.minProperty(), slider.maxProperty()));
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                count = (float) slider.getValue();
                volumeController.volumeControl(count);
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
                player.onStopMusic();
                Thread thread = new Thread(() -> {
                    panel.setText(player.getPlaylist().get(player.getIndexTrack() - 1).getPath());
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
                panel.setText(player.getPlaylist().get(player.getIndexTrack() - 1).getPath());
                player.nextMusic();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);
//        button.getStyleClass().setAll("btn", "btn-danger");
        return button;
    }

    public Button prevTrack() {
        Button button = new Button("PrTr");
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                player.stopMusic();
                System.out.println("Previous");
                panel.setText(player.getPlaylist().get(player.getIndexTrack() - 1).getPath());
                Thread thread = new Thread(() -> {
                    player.playList(player.getIndexTrack() - 1, -1);
                });
                thread.start();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);
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

    public Button pauseMusic() {
        Button button = new Button("pause");
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("pause");
                player.pauseMusic();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);

        return button;
    }
}