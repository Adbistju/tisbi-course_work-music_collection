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

    private final Listener infoListenerTest;

    private AdvancedPlayer player;
    private List<MusicFile> playlist;
    private MusicFile currentTrack;
    private AtomicInteger indexTrack;
    private AtomicBoolean nextPlay;
    private AtomicInteger positionTrack;

    public Player() {
        this.indexTrack = new AtomicInteger(0);
        this.nextPlay = new AtomicBoolean(true);
        this.positionTrack = new AtomicInteger(0);
        this.infoListenerTest = new Listener();
    }

    public void playList() {
        playList(indexTrack.get());
    }

    public void playList(int indexTrack) {
        stopMusic();
        nextPlay.set(true);
        this.indexTrack.set(indexTrack);
        for (int i = this.indexTrack.get(); i < playlist.size(); i++) {
            if (!nextPlay.get()) {
                return;
            }
            currentTrack = playlist.get(i);
            this.indexTrack.set(i);
            playTrack(currentTrack, positionTrack.get(), -1);
        }
    }

    public void playTrackPercent(MusicFile currentTrack, float percentPosition, float percentEnd) {
        long a = (currentTrack.getLength() / 1000 / 100);// 1000 попугаев + 100 часть для процента
        int position = Math.toIntExact((long) (a * percentPosition));
        int endPosition = Math.toIntExact((long) (a * percentEnd));
        playTrack(currentTrack, position, endPosition);
    }

    public void playTrack(MusicFile currentTrack, int position, int endPosition) {
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

    public void volumeControl(int level) {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixers) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getTargetLineInfo();
            for (Line.Info lineInfo : lineInfos) {
                Line line = null;
                boolean opened = true;
                try {
                    line = mixer.getLine(lineInfo);
                    opened = line.isOpen() || line instanceof Clip;
                    if (!opened) {
                        line.open();
                    }
                    FloatControl volControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                    volControl.setValue(floatVolumeLevel(volControl, level));
                } catch (LineUnavailableException | IllegalArgumentException e) {

                } finally {
                    if (line != null && !opened) {
                        line.close();
                    }
                }
            }
        }
    }

    private float floatVolumeLevel(FloatControl volControl, int percent) {
        float mr = volControl.getMaximum() - volControl.getMinimum();
        return mr / 100 * percent;
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
        nextPlay.set(false);
        indexTrack.set(0);
        positionTrack.set(0);
        currentTrack = playlist.get(0);
    }

    public void pauseMusic() {
        if (player != null) {
            player.stop();
        }
        nextPlay.set(false);
    }

    public void nextMusic() {
        if (player != null) {
            player.stop();
            nextPlay.set(true);
            positionTrack.set(0);
        }
    }

    public class Listener extends PlaybackListener {
        public Listener() {
        }

        public void playbackStarted(PlaybackEvent evt) {

        }

        public void playbackFinished(PlaybackEvent evt) {
            int frame = evt.getFrame() / 25;
            positionTrack.set(frame);
        }
    }
}
