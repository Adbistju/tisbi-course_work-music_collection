package adbistju.system.music.collection.fileSysten;

import adbistju.system.music.collection.MusicFile;
import adbistju.system.music.collection.Player;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class FileSystemButton {

    private Player player;
    private FileManager fileManager;
    private TrackListReader trackListReader;

    public FileSystemButton(Player player, FileManager fileManager) {
        this.player = player;
        this.fileManager = fileManager;
    }

    public Button createAdd(File file) {
        Button button = new Button("add");

        EventHandler<MouseEvent> released = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("add");
                try {
                    MusicFile musicFile = new MusicFile(file);
                    player.getPlaylist().add(musicFile);
                    player.getPlaylist().forEach(x-> System.out.println(x.getFilename()));
                    player.addPlayList();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, released);

        return button;
    }

    public Button createPlayOne(File file) {
        Button button = new Button("play");

        EventHandler<MouseEvent> released = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("play");
                try {
                    MusicFile musicFile = new MusicFile(file);
                    player.getPlaylist().clear();
                    player.getPlaylist().add(musicFile);
                    player.getPlaylist().forEach(x-> System.out.println(x.getFilename()));
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
//todo не работает
        EventHandler<MouseEvent> released = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("open");
                System.out.println(file.getName());
                System.out.println(file.getPath());
                fileManager.changeDirectory(file.getName());
                fileManager.setCurrentFolder(file.getPath());
                System.out.println("--------------------------------------------");
                System.out.println(fileManager.getCurrentFolder());
                System.out.println("--------------------------------------------");
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, released);

        return button;
    }

    public Button createRead(File file) {
        return new Button("read");
    }


//     if (curFile instanceof MusicFileDto) {
//        hBox = new HBox(new Text("|"),
//                new Text(pathss), new Button("add"), new Button("play"));
//
//    } else if (curFile instanceof FolderDto) {
//        hBox = new HBox(new Text("|"),
//                new Text(pathss), new Button("open"));
//
//    } else if (curFile instanceof TrackListDto) {
//        hBox = new HBox(new Text("|"),
//                new Text(pathss), new Button("read"), new Button("cp"), new Button("rn"), new Button("dlt"));
//
//    } else if (curFile instanceof ContentTrackListDto) {
//        hBox = new HBox(new Text("|"),
//                new Text(pathss), new Button("delete"));
//    }

}
