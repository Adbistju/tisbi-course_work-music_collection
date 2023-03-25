package adbistju.system.music.collection;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Player {

    private final PlaybackTrackStateListener infoListenerTest;

    private AdvancedPlayer player;
    private List<MusicFile> playlist;
    private MusicFile currentTrack;
    private AtomicInteger indexTrack;
    private AtomicBoolean nextPlay;
    private AtomicInteger positionTrack;
    private AtomicBoolean pausePlay;

    public Player() {
        this.indexTrack = new AtomicInteger(0);
        this.nextPlay = new AtomicBoolean(true);
        this.positionTrack = new AtomicInteger(0);
        this.infoListenerTest = new PlaybackTrackStateListener();
        this.pausePlay = new AtomicBoolean(false);
    }

    public void playList() {
        playList(indexTrack.get(), 0);
    }

    public void playList(int indexTrack, float percentPosition) {
        if (!pausePlay.get()) {
            stopMusic();
        }

        pausePlay.set(false);
        nextPlay.set(true);

        playTrackPercent(playlist.get(indexTrack), indexTrack, percentPosition, -1);
        indexTrack++;

        for (int i = indexTrack; i < playlist.size(); i++) {
            if (!nextPlay.get()) {
                return;
            }

            playTrackPercent(playlist.get(i), i, 0, -1);
        }

    }

    public void playListPosition(int indexTrack, int position) {
        if (!pausePlay.get()) {
            stopMusic();
        }

        pausePlay.set(false);
        nextPlay.set(true);

        if (position < 0) {
            position = positionTrack.get();
        }

        for (int i = indexTrack; i < playlist.size(); i++) {
            if (!nextPlay.get()) {
                return;
            }

            playTrack(playlist.get(i), i, position, -1);
        }

    }

    public void playTrackPercent(MusicFile currentTrack, int indexTrack, float percentPosition, float percentEnd) {
        long a = (currentTrack.getFrameCount() / 100);// 100 часть для процента
        int position = Math.toIntExact((long) (a * percentPosition));
        int endPosition = Math.toIntExact((long) (a * percentEnd));
        playTrack(currentTrack, indexTrack, position, endPosition);
    }

    public void playTrack(MusicFile currentTrack, int indexTrack, int position, int endPosition) {

        this.indexTrack.set(indexTrack);
        this.currentTrack = currentTrack;

        AudioDevice auDev = new JavaSoundAudioDevice();
        try {
            InputStream musicStream = new FileInputStream(currentTrack.getPath());
            player = new AdvancedPlayer(musicStream, auDev);
            player.setPlayBackListener(infoListenerTest);
            player.play(position, endPosition < 0 ? currentTrack.getFrameCount() : endPosition);
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
        if (player != null) {
            try {
                player.stop();
            } catch (Exception e) {

            }
        }
        pausePlay.set(false);
        nextPlay.set(false);
        indexTrack.set(0);
        positionTrack.set(0);
        currentTrack = playlist.get(0);
    }

    public void pauseMusic() {
        if (player != null) {
            player.stop();
        }
        pausePlay.set(true);
        nextPlay.set(false);
    }

    public void nextMusic() {
        if (player != null) {
            player.stop();
            nextPlay.set(true);
            positionTrack.set(0);
        }
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
