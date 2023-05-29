package adbistju.system.music.collection;

import adbistju.system.music.collection.cdi.DIControl;
import adbistju.system.music.collection.cdi.Instance;
import adbistju.system.music.collection.cdi.PostConstruct;
import adbistju.system.music.collection.musicsystem.MusicFile;
import adbistju.system.music.collection.musicsystem.Player;
import javafx.scene.control.Button;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.ArrayList;

public class UIMemento implements PostConstruct {

    private Player player;
    private ViewGenerator viewGenerator;
    private Button playButton;
    private Panel currentTrackNamePanel;

    public void setCurrentTrackName(String currentTrackName) {
        this.currentTrackNamePanel.setText(currentTrackName);
    }

    public void updateTrackList() {
        viewGenerator.recreateTracks(player.getPlaylist(), playButton);
    }

    public void updateTrackList(ArrayList<MusicFile> playlist) {
        viewGenerator.recreateTracks(playlist, playButton);
    }

    @Override
    public void construct() {
        this.player = (Player) DIControl.getInstance("player");
        this.playButton = (Button) ((Instance) DIControl.getInstance("playButton")).getData();
        this.viewGenerator = (ViewGenerator) DIControl.getInstance("viewGenerator");
        this.currentTrackNamePanel = (Panel) ((Instance) DIControl.getInstance("currentTrackNamePanel")).getData();
    }
}
