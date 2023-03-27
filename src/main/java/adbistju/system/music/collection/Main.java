package adbistju.system.music.collection;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXSlider;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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
        String enteredByUser = "abcdef";
//        panel.setStyle("-fx-background-color: " + "linear-gradient(from 25% 25% to 100% 100%, #dc143c, #abcdef)");

        panel.setStyle("-fx-background-color: " +
                "linear-gradient(from 0% 100% to 100% 0%, #3f87a6, #ebf8e1, #f69d3c, #e66465)"
        );

//        panel.setStyle("-fx-background-insets: "
//                        + "linear-gradient(from 100% 0% to 0% 70%, #3f87a6, #FFFFFF),"
////                        + "-fx-background-color: "
//                        + " linear-gradient(from 0% 100% to 100% 0%, #f69d3c, #FFFFFF)"
//
////                "linear-gradient(from 0% 100% to 100% 0%, #3f87a6, #ebf8e1, #f69d3c, #e66465)"
//        );


        BorderPane content = new BorderPane();

        Slider slider = sliderTrack(content);
//https://morioh.com/p/f394ce9e52d2
        JFXSlider slider1 = new JFXSlider();
        slider1.setOpacity(1);
//        slider1.getStyleClass().add("jfx-slider");

//        slider1.setStyle(
//                " -jfx-default-track: #f69d3c;" +
//                        "       .jfx-slider-style . track {" +
//                        "-fx-background-color: #3f87a6;" +
//                        "-fx-pref-height: 20px;" +
//                        "-fx-pref-height: 20px;" +
//                        "}"
//        );
        System.out.println(slider1.getCssMetaData());
        System.out.println(slider1.getStyleClass().get(1));
//        slider1.getStyleClass().add("jfx-slider");
//        JFoenixResources.load("data/style.css").toExternalForm();
//        String scc = getClass().getResource("path/style.css").toExternalForm();
//        String scc = new FileInputStream("style.css").read();
//        slider1.getStyleClass().add(scc);
//        slider1.setStyle("\n" +
//                ".jfx-slider .track,\n" +
//                ".jfx-slider:vertical .track {\n" +
//                "    -fx-background-color: -jfx-default-track;\n" +
//                "    -fx-background-radius: 5;\n" +
//                "    -fx-background-insets: 0;\n" +
//                "    -fx-pref-width: 20px;\n" +
//                "    -fx-pref-height: 20px;\n" +
//                "}\n" +
//                "\n" +
//                ".jfx-slider .thumb,\n" +
//                ".jfx-slider:focused .thumb {\n" +
//                "    -fx-background-color: -jfx-default-thumb;\n" +
//                "    -fx-background-radius: 20;\n" +
//                "    -fx-background-insets: 0;\n" +
//                "}");
//        file:data/3.png
//        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        slider1.setMin(0);
        slider1.setMax(100);
        slider1.setValue(10);
//        slider1.setCursor(new ImageCursor(new Image("file:data/3.png")));

//        slider1.setMajorTickUnit(10);
        //zoomSlider.setMinorTickCount(5);
//        slider1.setShowTickLabels(true);
//        slider1.setShowTickMarks(true);
//        slider1.setSnapToTicks(false);
        slider1.setBlockIncrement(10);
        slider1.setOrientation(Orientation.HORIZONTAL);
//        slider1.setIndicatorPosition(JFXSlider.IndicatorPosition.LEFT);
        slider1.styleProperty().bind(Bindings.createStringBinding(() -> {
            double min = slider.getMin();
            double max = slider.getMax();
            double value = slider.getValue() ;
            return createSliderStyle(slider.getValue(), min, max, value);
        }, slider.valueProperty()));
        content.setBottom(slider1);

        nextTrack(content);
        odonButton(content);
        stop(content);

//        Thread thread = new Thread(() -> {
//
//            while (true) {
//                try {
//                    Thread.sleep(300);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (count >= 100) {
//                    count = 0;
//                } else {
//                    count = count + 1;
//                }
//                slider.setValue(count);
//            }
//        });
//        thread.start();

        panel.setBody(content);
        Scene scene = new Scene(panel);

//        String scc = getClass().getResource("path/style.css").toExternalForm();
//        scene.getStylesheets().add(scc);

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("BootstrapFX");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    private String createSliderStyle(double startingValue, double min, double max, double value) {
        StringBuilder gradient = new StringBuilder("-slider-track-color: ");
        String defaultBG = "derive(-fx-control-inner-background, -5%) ";
        gradient.append("linear-gradient(to right, ").append(defaultBG).append("0%, ");

//        double valuePercent = 100.0 * (value - min) / (max - min);

        double valuePercent = value;

        double startingValuePercent = startingValue * 100.0;


        if (valuePercent > startingValuePercent) {
            gradient.append(defaultBG).append(startingValuePercent).append("%, ");
            gradient.append("green ").append(startingValuePercent).append("%, ");
            gradient.append("green ").append(valuePercent).append("%, ");
            gradient.append(defaultBG).append(valuePercent).append("%, ");
            gradient.append(defaultBG).append("100%); ");
        } else {
            gradient.append(defaultBG).append(valuePercent).append("%, ");
            gradient.append("red ").append(valuePercent).append("%, ");
            gradient.append("red ").append(startingValuePercent).append("%, ");
            gradient.append(defaultBG).append(startingValuePercent).append("%, ");
            gradient.append(defaultBG).append("100%); ");
        }
        return gradient.toString();
    }

    public Slider sliderTrack(BorderPane content) {

        Slider slider = new Slider(0, 100, 50);


        slider.setStyle(" -fx-background-color:\n" +
                "          -fx-shadow-highlight-color,\n" +
                "          linear-gradient(to bottom, derive(-fx-text-box-border, -10%), -fx-text-box-border),\n" +
                "          linear-gradient(to bottom,\n" +
                "            derive(-fx-control-inner-background, -9%),\n" +
                "            derive(-fx-control-inner-background, 0%),\n" +
                "            derive(-fx-control-inner-background, -5%),\n" +
                "            derive(-fx-control-inner-background, -12%)\n" +
                "          );\n" +
                "    -fx-background-insets: 0 0 -1 0, 0, 1;\n" +
                "    -fx-background-radius: 0.25em, 0.25em, 0.166667em; \n" +
                "    -fx-padding: 0.25em;");
//        slider.styleProperty().bind(Bindings.createStringBinding(() -> {
//            double min = slider.getMin();
//            double max = slider.getMax();
//            double value = slider.getValue() ;
//            return createSliderStyle(slider.getValue(), min, max, value);
//        }, slider.valueProperty()));

//    setStyle("-slider-track-color: derive(-fx-control-inner-background, -5%) ;");

        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
//                перекраска слайдера
//                https://stackoverflow.com/questions/51343759/how-to-change-fill-color-of-slider-in-javafx
//                slider.setStyle("-fx-background-color: " + "linear-gradient(from " + slider.getValue() + "% " + slider.getValue() + "% to " + (100 - slider.getValue()) + "% " + (100 - slider.getValue()) + "% , #dc143c, #32cd32)");
//                slider.setStyle("-fx-background-color: linear-gradient(to right, #2D819D 20%, #969696 20%);");
                slider.setStyle("-fx-background-color: linear-gradient(to left, #2D819D " + (100 - slider.getValue()) + "%, #969696 " + (100 - slider.getValue()) + "%);");
                System.out.println(slider.getValue());
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

        content.setBottom(slider);

        return slider;
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