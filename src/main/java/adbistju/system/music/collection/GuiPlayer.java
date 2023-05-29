package adbistju.system.music.collection;

import adbistju.system.music.collection.cdi.DIControl;
import adbistju.system.music.collection.cdi.Instance;
import adbistju.system.music.collection.fileSysten.FileSystemButtonGenerator;
import adbistju.system.music.collection.fileSysten.TrackListReader;
import adbistju.system.music.collection.musicsystem.Player;
import adbistju.system.music.collection.musicsystem.VolumeController;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.IOException;

import static javafx.scene.media.MediaPlayer.Status.PLAYING;

public class GuiPlayer extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException, InvalidDataException, UnsupportedTagException {

        Button playButton = playTrack();

        Panel basePanel = new Panel();
        basePanel.setStyle("-fx-background-color: null");

        ScrollPane scrollPaneTraclList = new ScrollPane();
        scrollPaneTraclList.getStylesheets().add(0, "style.css");

        ScrollPane scrollPaneFileManage = new ScrollPane();

        Panel panel = new Panel("This is the title track");

        Player player = new Player();

        DIControl.putInstance("player", player);
        DIControl.putInstance("memento", new UIMemento());
        DIControl.putInstance("trackListReader", new TrackListReader("startFolder"));
        DIControl.putInstance("viewGenerator", new ViewGenerator());
        DIControl.putInstance("textPath", new Instance(new TextField("startFolder")));
        DIControl.putInstance("fileSystemButtonGenerator", new FileSystemButtonGenerator());
        DIControl.putInstance("playButton", new Instance(playButton));
        DIControl.putInstance("currentTrackNamePanel", new Instance(panel));
        DIControl.putInstance("volumeController", new VolumeController());
        DIControl.putInstance("volumeUI", new VolumeUI());
        DIControl.putInstance("sliderTrackUi", new SliderTrackUi());
        DIControl.putInstance("fileManagerUI", new FileManagerUI());
        DIControl.putInstance("trackListScrollPanel", new Instance(scrollPaneTraclList));
        DIControl.putInstance("fileManagerScrollPane", new Instance(scrollPaneFileManage));

        DIControl.initAll();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                ((SliderTrackUi) DIControl.getInstance("sliderTrackUi")).setPlayerIsWorkFalse();
                Platform.exit();
            }
        });

        panel.setStyle("-fx-background-color: transparent");
        BorderPane content = new BorderPane();

        ((ViewGenerator) DIControl.getInstance("viewGenerator")).recreateTracks();
        HBox div = div(playButton, ((VolumeUI) DIControl.getInstance("volumeUI")).divVolume());
        BorderPane dive = new BorderPane();
        Slider sliderTrack = ((SliderTrackUi) DIControl.getInstance("sliderTrackUi")).sliderTrack();
        dive.setBottom(sliderTrack);

        content.setCenter(
                scrollPane(
                        (ScrollPane) ((Instance) DIControl.getInstance("trackListScrollPanel")).getData(),
                        vbox(),
                        dive,
                        ((FileManagerUI) DIControl.getInstance("fileManagerUI")).fileManager()
                )
        );
        content.setBottom(div);

        ((UIMemento) DIControl.getInstance("memento")).updateTrackList();

        panel.setBody(content);
        Panel background = background();
        background.setBody(panel);
        Scene scene = scene(stage, background);
        Platform.setImplicitExit(false);

        stage.setTitle("Player");
        stage.setMaximized(false);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaxHeight(900);
        stage.setMaxWidth(1400);
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.sizeToScene();
        stage.show();

    }

    public Panel scrollPane(ScrollPane trackListScrollPanel, VBox vbox, BorderPane dive, Panel fileManager/*, Panel panel*/) {
        trackListScrollPanel.setContent(vbox);
        Panel scrollPanel = new Panel();
        scrollPanel.setCenter(trackListScrollPanel);
        scrollPanel.setBottom(dive);
        scrollPanel.setRight(fileManager);
        scrollPanel.setStyle("-fx-background-color: null");
        return scrollPanel;
    }

    public Panel background() {
        Panel background = new Panel();
        background.setStyle("-fx-background-image: url(Home.png)");
        return background;
    }

    public Scene scene(Stage stage, Panel background) {
        Scene scene = new Scene(background, stage.getWidth(), stage.getHeight(), Color.TRANSPARENT);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        return scene;
    }

    public HBox div(Button playTrackButton, HBox divVolume) {
        HBox div = new HBox();
        div.setSpacing(20);
        div.setAlignment(Pos.BASELINE_CENTER);
        div.setMaxHeight(playTrackButton.getHeight());
        div.getChildren().addAll(
                retry(),
                prevTrack(),
                playTrackButton,
                nextTrack(),
                stopMusic(playTrackButton),
                divVolume
        );
        div.setPadding(new Insets(10, 10, 10, 10));
        return div;
    }

    public VBox vbox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);
        return vbox;
    }

    /**
     * Кнопка play/pause.
     * @return
     */
    public Button playTrack() {

        ImageView playButton = new ImageView(new Image("file:data/Play.png"));
        ImageView pauseButton = new ImageView(new Image("file:data/Pause.png"));
        Button button = new Button("play", playButton);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.getStylesheets().add(0, "button.css");

        double value = 50;
        button.setMinWidth(value);
        button.setMaxWidth(value);
        button.setPrefWidth(value);

        button.setMinHeight(value);
        button.setMaxHeight(value);
        button.setPrefHeight(value);

        button.setMinSize(value, value);
        button.setMaxSize(value, value);
        button.setPrefSize(value, value);

        EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Player player = (Player) DIControl.getInstance("player");
                if (player.getStatus() != PLAYING) {
                    button.setGraphic(pauseButton);
                    player.playList();
                } else {
                    button.setGraphic(playButton);
                    player.pauseMusic();
                }
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, releasedHandler);
        return button;
    }

    /**
     * Создвем кнопку повторения плейлиста.
     */
    public Button retry() {
        Player player = (Player) DIControl.getInstance("player");

        ImageView repeatButtom = new ImageView(new Image("file:data/Repeat.png"));
        ImageView noRepeatButtom = new ImageView(new Image("file:data/NoRepeat.png"));
        Button button = new Button("retry", noRepeatButtom);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.getStylesheets().add(0, "button.css");

        double value = 50;
        button.setMinWidth(value);
        button.setMaxWidth(value);
        button.setPrefWidth(value);

        button.setMinHeight(value);
        button.setMaxHeight(value);
        button.setPrefHeight(value);

        button.setMinSize(value, value);
        button.setMaxSize(value, value);
        button.setPrefSize(value, value);

        EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (!player.getRetry()) {
                    button.setGraphic(repeatButtom);
                } else {
                    button.setGraphic(noRepeatButtom);
                }
                player.retry();

            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, releasedHandler);
        return button;
    }

    /**
     * Кнопка предыдущего трека.
     * @return
     */
    public Button prevTrack() {
        Player player = (Player) DIControl.getInstance("player");

        ImageView prevButton = new ImageView(new Image("file:data/Skip Back.png"));
        Button button = new Button("PrTr", prevButton);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.getStylesheets().add(0, "button.css");

        double value = 50;
        button.setMinWidth(value);
        button.setMaxWidth(value);
        button.setPrefWidth(value);

        button.setMinHeight(value);
        button.setMaxHeight(value);
        button.setPrefHeight(value);

        button.setMinSize(value, value);
        button.setMaxSize(value, value);
        button.setPrefSize(value, value);

        EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                player.previousTrack();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, releasedHandler);
        return button;
    }

    /**
     * Кнопка следующего трека.
     * @return
     */
    public Button nextTrack() {
        Player player = (Player) DIControl.getInstance("player");
        ImageView nextButton = new ImageView(new Image("file:data/Skip Fwd.png"));
        Button button = new Button("NxTr", nextButton);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.getStylesheets().add(0, "button.css");

        double value = 50;
        button.setMinWidth(value);
        button.setMaxWidth(value);
        button.setPrefWidth(value);

        button.setMinHeight(value);
        button.setMaxHeight(value);
        button.setPrefHeight(value);

        button.setMinSize(value, value);
        button.setMaxSize(value, value);
        button.setPrefSize(value, value);

        EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                player.nextMusic();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, releasedHandler);
        return button;
    }

    /**
     * Создаем кнопку stop.
     *
     * @param playButton готовая кнопка play/pause
     * @return
     */
    public Button stopMusic(Button playButton) {
        Player player = (Player) DIControl.getInstance("player");
        ImageView playButtonIcon = new ImageView(new Image("file:data/Play.png"));
        ImageView stopButtom = new ImageView(new Image("file:data/Stop.png"));
        Button button = new Button("stop", stopButtom);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.getStylesheets().add(0, "button.css");

        double value = 50;
        button.setMinWidth(value);
        button.setMaxWidth(value);
        button.setPrefWidth(value);

        button.setMinHeight(value);
        button.setMaxHeight(value);
        button.setPrefHeight(value);

        button.setMinSize(value, value);
        button.setMaxSize(value, value);
        button.setPrefSize(value, value);

        EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (player.getTrack() != null) {
                    playButton.setGraphic(playButtonIcon);
                    player.getTrack().setStartTime(Duration.ZERO);
                    player.getTrack().stop();
                }
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, releasedHandler);

        return button;
    }
}
