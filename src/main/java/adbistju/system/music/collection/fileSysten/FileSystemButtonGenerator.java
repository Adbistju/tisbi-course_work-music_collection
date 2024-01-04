package adbistju.system.music.collection.fileSysten;

import adbistju.system.music.collection.ViewGenerator;
import adbistju.system.music.collection.cdi.DIControl;
import adbistju.system.music.collection.cdi.Instance;
import adbistju.system.music.collection.cdi.PostConstruct;
import adbistju.system.music.collection.musicsystem.MusicFile;
import adbistju.system.music.collection.musicsystem.Player;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static adbistju.system.music.collection.GuiPlayer.pauseImage;

public class FileSystemButtonGenerator implements PostConstruct {

    private Player player;
    private TrackListReader trackListReader;
    private ViewGenerator viewGenerator;
    private TextField currentPath;

    public Button createAdd(File file) {
        Button button = new Button("add");

        EventHandler<MouseEvent> released = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    MusicFile musicFile = new MusicFile(file);
                    player.getPlaylist().add(musicFile);
                    viewGenerator.recreateTracks();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, released);

        return button;
    }

    public Button createPlayOne(File file, Button playButton) {
        ImageView pauseButtonImage = pauseImage;
        Button button = new Button("play");

        EventHandler<MouseEvent> released = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                playButton.setGraphic(pauseButtonImage);
                try {
                    MusicFile musicFile = new MusicFile(file);
                    player.getPlaylist().clear();
                    player.getPlaylist().add(musicFile);
                    player.playList();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, released);

        return button;
    }

    public Button createOpenFolder(File file) {
        Button button = new Button("open");
        EventHandler<MouseEvent> released = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                trackListReader.changeDirectory(file.getName());
                currentPath.setText(trackListReader.getCurrentFolder());
                viewGenerator.recreateFileManager();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, released);

        return button;
    }

    public Button createRead(File file) {
        Button button = new Button("open");//"read"
        EventHandler<MouseEvent> released = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                trackListReader.changeDirectory(file.getName());
                currentPath.setText(trackListReader.getCurrentFolder());
                viewGenerator.recreateFileManager();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, released);
        return button;
    }

    public Button createDelete(File file, String values) {
        Button button = new Button("delete");
        EventHandler<MouseEvent> released = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    trackListReader.removeLineFromFile(file, values);
                } catch (IOException ex) {

                }
                viewGenerator.recreateFileManager();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, released);
        return button;
    }

    public Button createTrackPlayButton(int i, Button buttonPlay, ImageView pauseButtonIcon) {
        Button currentTrack = new Button(String.valueOf(i));
        int finalI = i;
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                buttonPlay.setGraphic(pauseButtonIcon);
                player.stopMusic();
                player.playList(finalI, -1);
            }
        };
        currentTrack.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);
        return  currentTrack;
    }

    public Button createPlayTrackList(File file, Button playButton) {
        ImageView pauseButtonImage = pauseImage;
        Button button = new Button("play");
        EventHandler<MouseEvent> released = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                playButton.setGraphic(pauseButtonImage);
                ArrayList<MusicFile> playlist = (ArrayList<MusicFile>)  trackListReader.readContent(file.getPath()).stream().filter(x->x.getName().matches("(.*).(mp3)$"))
                        .map(y-> {
                            try {
                                return new MusicFile(y.getName());//-------------------------
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (UnsupportedTagException ex) {
                                ex.printStackTrace();
                            } catch (InvalidDataException ex) {
                                ex.printStackTrace();
                            }
                            return null;
                        }).collect(Collectors.toList());
                try {
                    player.setPlaylist(playlist);
                    player.playList();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, released);
        return button;
    }

    public Button createSavePlayList() {
        Button button = new Button("save playlist");
        EventHandler<MouseEvent> released = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    String name = currentPath.getText();
                    String currentFolder = trackListReader.getCurrentFolder();
                    if (name.equals(currentFolder)) {
                        trackListReader.save(player.getPlaylist(), null);
                    } else {
                        trackListReader.save(player.getPlaylist(), name);
                    }
                    currentPath.setText(currentFolder);
                    viewGenerator.recreateFileManager();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, released);
        return button;
    }

    public Button createCopyTrackList(File file) {
        Button button = new Button("copy");
        EventHandler<MouseEvent> released = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                String name = currentPath.getText();
                String currentFolder = trackListReader.getCurrentFolder();
                if (name.equals(currentFolder)) {
                    trackListReader.copyFile(file.getName(), currentPath.getText() + "List" + ".txls");
                } else {
                    trackListReader.copyFile(file.getName(), currentPath.getText() + ".txls");
                }
                currentPath.setText(currentFolder);
                viewGenerator.recreateFileManager();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, released);
        return button;
    }

    @Override
    public void construct() {
        this.player = (Player) DIControl.getInstance("player");
        this.trackListReader = (TrackListReader) DIControl.getInstance("trackListReader");
        this.viewGenerator = (ViewGenerator) DIControl.getInstance("viewGenerator");
        this.currentPath = (TextField) ((Instance) DIControl.getInstance("textPath")).getData();
    }
}
