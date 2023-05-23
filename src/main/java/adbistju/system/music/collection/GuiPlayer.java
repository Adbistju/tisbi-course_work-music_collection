package adbistju.system.music.collection;

import adbistju.system.music.collection.dto.ContentTrackListDto;
import adbistju.system.music.collection.dto.FolderDto;
import adbistju.system.music.collection.dto.GuiFileDto;
import adbistju.system.music.collection.dto.MusicFileDto;
import adbistju.system.music.collection.dto.TrackListDto;
import adbistju.system.music.collection.fileSysten.FileManager;
import adbistju.system.music.collection.fileSysten.FileSystemButton;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static javafx.scene.media.MediaPlayer.Status.PLAYING;

//todo класс нужно переработать
public class GuiPlayer extends Application {

    /**
     * Значения переменных muss можно заменить на доступные треки.
     *
     * Работа с фалйовой системой еще не готова.
     */
    public static String muss = "01. Don-t Blame Me (Split).mp3";
    public static String muss1 = "01. Butterfly (split).mp3";
    public static String muss2 = "John_Coltrane_-_Blue_Train_(-).mp3";
    private static Player player;
    private static VolumeController volumeController = new VolumeController();

    private static FileSystemButton fileSystemButton;
    private static FileManager fileManager = new FileManager("startFolder\\aa");

    private static float count = 0;

    /**
     * Панель для отображения текущего трека.
     */
    private Panel panel = new Panel("This is the title track");

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException, InvalidDataException, UnsupportedTagException {
        player = new Player(panel);
        fileSystemButton = new FileSystemButton(player, fileManager);

        MusicFile musicFile = new MusicFile(muss);
        MusicFile musicFile1 = new MusicFile(muss1);
        MusicFile musicFile2 = new MusicFile(muss2);
        player.setPlaylist(new ArrayList<MusicFile>(List.of(musicFile, musicFile1, musicFile2, musicFile, musicFile1, musicFile2, musicFile, musicFile1, musicFile2)));

        panel.setStyle("-fx-background-color: transparent");
        BorderPane content = new BorderPane();
        VBox vbox = vbox();
        List<MusicFile> tracks = player.getPlaylist();
        Button playButton = playTrack();

        //

        tracks(vbox, tracks, playButton);
        HBox div = div(playButton, divVolume());
        BorderPane dive = new BorderPane();
        Slider sliderTrack = sliderTrack();
        dive.setBottom(sliderTrack);
        content.setCenter(scrollPane(vbox, dive, fileManager()));
        content.setBottom(div);

        panel.setBody(content);
        Panel background = background();
        background.setBody(panel);
        Scene scene = scene(stage, background);
        Platform.setImplicitExit(false);

        stage.setTitle("BootstrapFX");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaxHeight(900);
        stage.setMaxWidth(1400);
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.sizeToScene();
        stage.show();

    }

    private Panel fileManager() {
        Panel panel= new Panel();

        panel.setMinSize(300, 300);
        panel.setStyle("-fx-background-color: red");

        TextField textField = new TextField();

        VBox vBox = new VBox();

        String path = fileManager.getCurrentFolder();
        textField.setText(path);
        List<GuiFileDto> files = fileManager.listOfFiles(path);

        for (int i = 0; i < files.size(); i++) {
            String pathss = files.get(i).getName();
            HBox hBox = new HBox(new Text("|"),
                    new Text(pathss));
            hBox.setAlignment(Pos.BASELINE_LEFT);
            vBox.getChildren().add(hBox);
        }

        Button search = new Button("search");
        EventHandler<MouseEvent> relesedsearch = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                recreateFileManager(textField, fileManager, panel);
          /*      String path = textField.getCharacters().toString();
                textField.setText(path);
                List<GuiFileDto> files = fileManager.listOfFiles(path);
                VBox locVBox = new VBox();
                for (int i = 0; i < files.size(); i++) {
                    GuiFileDto curFile = files.get(i);
                    String pathss = files.get(i).getName();
                    HBox hBox = new HBox();
                    if (curFile instanceof MusicFile) {
                        hBox = new HBox(new Button("add") ,new Text("|"),
                                new Text(pathss), new Button("play"));

                    } else if (curFile instanceof FolderDto) {
                        hBox = new HBox(new Text("|"),
                                new Text(pathss), new Button("open"));

                    } else if (curFile instanceof TrackListDto) {
                        hBox = new HBox(new Text("|"),
                                new Text(pathss), new Button("read"), new Button("cp"), new Button("rn"), new Button("dlt"));

                    }
                    hBox.setAlignment(Pos.BASELINE_LEFT);
                    locVBox.getChildren().add(hBox);
                }

                ScrollPane scrollPane = new ScrollPane();
                scrollPane.getStylesheets().add(0, "style.css");
                scrollPane.setContent(locVBox);

                panel.setCenter(scrollPane);*/
            }
        };
        search.addEventFilter(MouseEvent.MOUSE_RELEASED, relesedsearch);

        Button button = new Button("prev");
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("prev");
                textField.setText(fileManager.getPrevFolder());
                String path = textField.getCharacters().toString();
                textField.setText(path);
                recreateFileManager(textField, fileManager, panel);
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);

        HBox hBox = new HBox(button, textField, search);
        panel.setTop(hBox);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add(0, "style.css");
        scrollPane.setContent(vBox);

        panel.setCenter(scrollPane);
        return panel;
    }

    private void recreateFileManager(TextField textField, FileManager fileManager, Panel panel) {
        String path = textField.getCharacters().toString();
        textField.setText(path);
        List<GuiFileDto> files = fileManager.listOfFiles(path);
        VBox locVBox = new VBox();
        for (int i = 0; i < files.size(); i++) {
            GuiFileDto curFile = files.get(i);
            String pathss = files.get(i).getName();
            HBox hBox = new HBox();

            if (curFile instanceof MusicFileDto) {
                Button add = fileSystemButton.createAdd(curFile.getFile());
                Button playOne = fileSystemButton.createPlayOne(curFile.getFile());
                hBox = new HBox(new Text("|"),
                        new Text(pathss), add, playOne);

            } else if (curFile instanceof FolderDto) {
                Button open = fileSystemButton.createOpenFolder(curFile.getFile());
                hBox = new HBox(new Text("|"),
                        new Text(pathss), open);

            } else if (curFile instanceof TrackListDto) {
                Button read = fileSystemButton.createRead(curFile.getFile());
                hBox = new HBox(new Text("|"),
                        new Text(pathss), read, new Button("cp"), new Button("rn"), new Button("dlt"));

            } else if (curFile instanceof ContentTrackListDto) {
                hBox = new HBox(new Text("|"),
                        new Text(pathss), new Button("delete"));
            }

            hBox.setAlignment(Pos.BASELINE_LEFT);
            locVBox.getChildren().add(hBox);

        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add(0, "style.css");
        scrollPane.setContent(locVBox);

        panel.setCenter(scrollPane);

    }

    public VBox vbox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);
        return vbox;
    }

    /**
     * Панель со списком треков и кнопок воспроизведения выбранного трека.
     *
     * @param vbox
     * @param tracks коллекция композиций.
     * @param buttonPlay кнопка play/pause
     * @return
     */
    public VBox tracks(VBox vbox, List<MusicFile> tracks, Button buttonPlay) {
        ImageView pauseButtonImage = new ImageView(new Image("file:data/Pause.png"));
        for (int i = 0; i < tracks.size(); i++) {
            Button currentTrack = createTrackPlayButton(i, buttonPlay, pauseButtonImage);

            try {
                ID3v1 id3v1Tag = tracks.get(i).getId3v1Tag();
                HBox hBox = new HBox(
                        currentTrack,
                        new Text("|"),
                        new Text(Optional.ofNullable(id3v1Tag.getTitle()).orElse("   ")),
                        new Text("|"),
                        new Text(String.valueOf(tracks.get(i).getLengthInSeconds())),
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
                        new Text(tracks.get(i).getPath()),
                        new Text("|"),
                        new Text(String.valueOf(tracks.get(i).getLengthInSeconds()))
                );
                hBox.setAlignment(Pos.BASELINE_LEFT);
                hBox.setSpacing(6);
                vbox.getChildren().add(hBox);
            }
        }
        return vbox;
    }

    public Button createTrackPlayButton(int i, Button buttonPlay, ImageView pauseButton) {
        Button currentTrack = new Button(String.valueOf(i));
        int finalI = i;
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                buttonPlay.setGraphic(pauseButton);
                player.stopMusic();
                panel.setText(player.getPlaylist().get(finalI).getPath());
                player.playList(finalI, -1);
            }
        };
        currentTrack.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);
        return  currentTrack;
    }

    public Panel scrollPane(VBox vbox, BorderPane dive, Panel fileManager) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add(0, "style.css");
        scrollPane.setContent(vbox);
        Panel scrollPanel = new Panel();
        scrollPanel.setCenter(scrollPane);
        scrollPanel.setBottom(dive);
        scrollPanel.setRight(fileManager);
        scrollPanel.setStyle("-fx-background-color: null");
        return scrollPanel;
    }

    public HBox divVolume() {
        HBox divVolume = new HBox();
        divVolume.getChildren().setAll(sliderVolume());
        divVolume.setAlignment(Pos.CENTER);
        return divVolume;
    }

    public HBox div(Button playTrackButton, HBox divVolume) {
        HBox div = new HBox();
        div.setSpacing(20);
        div.setAlignment(Pos.BASELINE_CENTER);
        div.setMaxHeight(playTrackButton.getHeight());
        div.getChildren().addAll(retry(), prevTrack(), playTrackButton,/*pauseMusic(),*/ nextTrack(), stopMusic(playTrackButton), divVolume);
        div.setPadding(new Insets(10, 10, 10, 10));
        return div;
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

    /**
     * Слайдер трека.
     * @return
     */
    public Slider sliderTrack() {
        Slider slider = new Slider(0, 100, 50);
        slider.getStylesheets().add(0, "style.css");

        slider.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100.0;
            return String.format(Locale.US, "-slider-track-color: linear-gradient(to left, -slider-filled-track-color 0%%, "
                            + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    100 - percentage, 100 - percentage);
        }, slider.valueProperty(), slider.minProperty(), slider.maxProperty()));

        EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                player.rewind(slider.getValue());
                clickSlider.set(false);
            }
        };
        slider.addEventFilter(MouseEvent.MOUSE_RELEASED, releasedHandler);

        EventHandler<MouseEvent> pressedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                clickSlider.set(true);
            }
        };
        slider.addEventFilter(MouseEvent.MOUSE_PRESSED, pressedHandler);

        //todo вынести в планировщик задач.
        Thread thread = new Thread(() -> {
            long time = System.currentTimeMillis();
            long delta = 500;
            while (true) {
                if ((time + delta) - System.currentTimeMillis() <= 0) {
                    time = System.currentTimeMillis();
                    if (player.getStatus() == PLAYING && !clickSlider.get()) {
                        slider.setValue(player.getCurrentTime().toMillis() / player.getTrack().getMedia().getDuration().toMillis() * 100);
                    }
                }
            }
        });
        thread.start();
        return slider;
    }

    private AtomicBoolean clickSlider = new AtomicBoolean(false);

    /**
     * Слайдер громкости прилоежния.
     * @return
     */
    public Slider sliderVolume() {
        Slider slider = new Slider(0, 100, 50);
        slider.getStylesheets().add(0, "style.css");
        volumeController.volumeControl(50);

        slider.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100.0;
            return String.format(Locale.US, "-slider-track-color: linear-gradient(to right, -slider-filled-track-color 0%%, "
                            + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, slider.valueProperty(), slider.minProperty(), slider.maxProperty()));
        EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                count = (float) slider.getValue();
                volumeController.volumeControl(count);
            }
        };
        slider.addEventFilter(MouseEvent.MOUSE_RELEASED, releasedHandler);
        return slider;
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
     * Кнопка следующего трека.
     * @return
     */
    public Button nextTrack() {
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
     * Кнопка предыдущего трека.
     * @return
     */
    public Button prevTrack() {

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
     * Создвем кнопку повторения плейлиста.
     */
    public Button retry() {

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
     * Создаем кнопку stop.
     *
     * @param playButton готовая кнопка play/pause
     * @return
     */
    public Button stopMusic(Button playButton) {
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
                playButton.setGraphic(playButtonIcon);
                player.getTrack().setStartTime(Duration.ZERO);
                player.getTrack().stop();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, releasedHandler);

        return button;
    }

    /*public Button pauseMusic() {
        Button button = new Button("pause");
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("pause");
                player.getTrack().pause();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);

        return button;
    }*/
}
