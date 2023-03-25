package adbistju.system.music.collection;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Player {

    private static final int DEFAULT_POSITION = 0;
    private static final int END_VALUE = -1;
    
    private final PlaybackTrackStateListener infoListenerTest;

    private AdvancedPlayer player;
    private List<MusicFile> playlist;
    private AtomicInteger indexTrack;
    private AtomicBoolean nextPlay;
    private AtomicInteger positionTrack;
    private AtomicBoolean pausePlay;

    public Player() {
        this.indexTrack = new AtomicInteger(DEFAULT_POSITION);
        this.nextPlay = new AtomicBoolean(true);
        this.positionTrack = new AtomicInteger(DEFAULT_POSITION);
        this.infoListenerTest = new PlaybackTrackStateListener();
        this.pausePlay = new AtomicBoolean(false);
    }

    public void playList() {
        playList(indexTrack.get(), END_VALUE);
    }

    public void playList(int indexTrack, float percentPosition) {
        if (!pausePlay.get()) {
            stopMusic();
        }

        pausePlay.set(false);
        nextPlay.set(true);

        MusicFile currentFile = playlist.get(indexTrack);

        if (percentPosition >= DEFAULT_POSITION) {
            positionTrack.set(TrackUtils.convertPercentToFrame(currentFile, percentPosition));
        }

        for (int i = indexTrack; i < playlist.size(); i++) {
            if (!nextPlay.get()) {
                return;
            }

            playTrack(playlist.get(i), i, positionTrack.get(), END_VALUE);
        }

    }

    public void playTrack(MusicFile currentTrack, int indexTrack, int position, int endPosition) {

        this.indexTrack.set(indexTrack);
        this.positionTrack.set(DEFAULT_POSITION);

        AudioDevice auDev = new JavaSoundAudioDevice();
        try {
            InputStream musicStream = new FileInputStream(currentTrack.getPath());
            player = new AdvancedPlayer(musicStream, auDev);
            player.setPlayBackListener(infoListenerTest);
            player.play(position, endPosition < DEFAULT_POSITION ? currentTrack.getFrameCount() : endPosition);
            musicStream.close();
            player.close();
        } catch (JavaLayerException | IOException e) {

        } finally {
            if (auDev.isOpen()) {
                auDev.close();
            }
        }
    }

    public List<MusicFile> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<MusicFile> playlist) {
        this.playlist = playlist;
    }

    public void stopMusic() {
        pausePlay.set(false);
        nextPlay.set(false);
        indexTrack.set(DEFAULT_POSITION);
        positionTrack.set(DEFAULT_POSITION);
        
        if (player != null) {
            try {
                player.stop();
            } catch (Exception e) {

            }
        }
    }

    public void pauseMusic() {
        pausePlay.set(true);
        nextPlay.set(false);
        
        if (player != null) {
            player.stop();
        }
    }

    public void nextMusic() {
        if (player != null) {
            player.stop();
            nextPlay.set(true);
            positionTrack.set(DEFAULT_POSITION);
        }
    }

    public int getIndexTrack() {
        return indexTrack.get();
    }

    public class PlaybackTrackStateListener extends PlaybackListener {
        public PlaybackTrackStateListener() {

        }

        public void playbackStarted(PlaybackEvent evt) {

        }

        public void playbackFinished(PlaybackEvent evt) {
            int frame = evt.getFrame() / 25;//25 попугаев
            positionTrack.set(frame);
        }
    }
}
