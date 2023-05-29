package adbistju.system.music.collection;

import adbistju.system.music.collection.cdi.DIControl;
import adbistju.system.music.collection.cdi.Instance;
import adbistju.system.music.collection.cdi.PostConstruct;
import adbistju.system.music.collection.dto.ContentTrackListDto;
import adbistju.system.music.collection.dto.FolderDto;
import adbistju.system.music.collection.dto.GuiFileDto;
import adbistju.system.music.collection.dto.MusicFileDto;
import adbistju.system.music.collection.dto.TrackListDto;
import adbistju.system.music.collection.fileSysten.FileSystemButtonGenerator;
import adbistju.system.music.collection.fileSysten.TrackListReader;
import adbistju.system.music.collection.musicsystem.MusicFile;
import adbistju.system.music.collection.musicsystem.Player;
import com.mpatric.mp3agic.ID3v1;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

public class ViewGenerator implements PostConstruct {

    private FileSystemButtonGenerator fileSystemButtonGenerator;
    private TrackListReader trackListReader;
    private TextField textPath;
    private Player player;
    private Button playButton;
    private ScrollPane trackListScrollPanel;
    private ScrollPane fileManagerScrollPane;

    public ViewGenerator() {
    }

    public VBox recreateFileManager() {
        String path = textPath.getCharacters().toString();
        textPath.setText(path);
        List<GuiFileDto> files;
        try {
            files = trackListReader.listOfFiles(path);
        } catch (NullPointerException e) {
            files = trackListReader.readContent(path);
        }

        VBox locVBox = new VBox();
        for (int i = 0; i < files.size(); i++) {
            GuiFileDto curFile = files.get(i);
            String fileName = files.get(i).getName();
            HBox hBox = new HBox();

            if (curFile instanceof MusicFileDto) {
                Button add = fileSystemButtonGenerator.createAdd(curFile.getFile());
                Button playOne = fileSystemButtonGenerator.createPlayOne(curFile.getFile(), playButton);
                hBox = new HBox(new Text("| "),
                        new Text(fileName), add, playOne);

            } else if (curFile instanceof FolderDto) {
                Button open = fileSystemButtonGenerator.createOpenFolder(curFile.getFile());
                hBox = new HBox(new Text("| "),
                        new Text(fileName), open);

            } else if (curFile instanceof TrackListDto) {
                Button read = fileSystemButtonGenerator.createRead(curFile.getFile());
                Button play = fileSystemButtonGenerator.createPlayTrackList(curFile.getFile(), playButton);
                Button copy = fileSystemButtonGenerator.createCopyTrackList(curFile.getFile());
                hBox = new HBox(new Text("| "),
                        new Text(fileName), read, play, copy);

            } else if (curFile instanceof ContentTrackListDto) {
                hBox = new HBox(new Text("| "),
                        new Text(fileName), fileSystemButtonGenerator.createDelete(curFile.getFile(), fileName));
            }

            hBox.setAlignment(Pos.BASELINE_LEFT);
            locVBox.getChildren().add(hBox);

        }

        fileManagerScrollPane.setContent(locVBox);
        return locVBox;
    }

    public VBox recreateTracks() {
        return recreateTracks(player.getPlaylist(), playButton);
    }

    public VBox recreateTracks(List<MusicFile> tracks, Button buttonPlay) {
        VBox vbox = new VBox();
        ImageView pauseButtonImage = new ImageView(new Image("file:data/Pause.png"));
        for (int i = 0; i < tracks.size(); i++) {
            Button currentTrack = fileSystemButtonGenerator.createTrackPlayButton(i, buttonPlay, pauseButtonImage);

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
                        new Text(tracks.get(i).getFilename()),
                        new Text("|"),
                        new Text(String.valueOf(tracks.get(i).getLengthInSeconds()))
                );
                hBox.setAlignment(Pos.BASELINE_LEFT);
                hBox.setSpacing(6);
                vbox.getChildren().add(hBox);
            }
        }
        trackListScrollPanel.setContent(vbox);
        return vbox;
    }

    @Override
    public void construct() {
        this.player = (Player) DIControl.getInstance("player");
        this.fileSystemButtonGenerator = (FileSystemButtonGenerator) DIControl.getInstance("fileSystemButtonGenerator");
        this.trackListReader = (TrackListReader) DIControl.getInstance("trackListReader");
        this.textPath = (TextField) ((Instance) DIControl.getInstance("textPath")).getData();
        this.playButton = (Button) ((Instance) DIControl.getInstance("playButton")).getData();
        this.trackListScrollPanel = (ScrollPane) ((Instance) DIControl.getInstance("trackListScrollPanel")).getData();
        this.fileManagerScrollPane = (ScrollPane) ((Instance) DIControl.getInstance("fileManagerScrollPane")).getData();
    }
}

