package adbistju.system.music.collection;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс плеера, осуществляет взаимодействие с UI и управляет процессом воспроизведения треков, их последовательности.
 */
public class Player {

    private static final int DEFAULT_POSITION = 0;
    private static final int END_VALUE = -1;

    /**
     * Ссылка на панель UI, для обновления названия трека.
     */
    private Panel panel;

    private Media media;
    private MediaPlayer player;

    /**
     * Плей лист.
     */
    private ArrayList<MusicFile> playlist;
    /**
     * Номер текущего трека.
     */
    private AtomicInteger indexTrack;
    /**
     * Повторять ли плей лист?
     */
    private AtomicBoolean retry;
    /**
     * Позиция трека текущего трека при остановке.
     */
    private AtomicInteger positionTrack;

    public Player(Panel panel) {
        this.indexTrack = new AtomicInteger(DEFAULT_POSITION);
        this.retry = new AtomicBoolean(false);
        this.positionTrack = new AtomicInteger(DEFAULT_POSITION);
        this.panel = panel;
    }

    public void addPlayList() {
        playList(indexTrack.get(), positionTrack.get());
    }

    /**
     * Начать воспроизведение с первого трека.
     */
    public void playList() {
        playList(indexTrack.get(), END_VALUE);
    }

    /**
     * Начать воспроизведение с indexTrack трека, с указанием времени чала.
     */
    public void playList(int indexTrack, double percentPosition) {

        if (player != null) {
            if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                stopMusic();
            }
            if (player.getStatus() == MediaPlayer.Status.PAUSED) {
                player.play();
                return;
            }
        }

        if (player != null) {
            player.dispose();
        }

        MusicFile currentFile = playlist.get(indexTrack);
        if (percentPosition >= DEFAULT_POSITION) {
            positionTrack.set(TrackUtils.convertPercentToFrame(currentFile, percentPosition));
        }

        PlayCommand nextTracks = null;
        TrackList trackList = new TrackList();
        if (retry.get()) {
            nextTracks = trackList.toTrackListRepeat(indexTrack, playlist, this);
        } else {
            nextTracks = trackList.toTrackListNoRepeat(indexTrack, playlist, this);
        }

        playTrack(playlist.get(indexTrack), nextTracks, indexTrack, Duration.millis(positionTrack.get()), Duration.millis(currentFile.getLengthInMilliseconds()));

//        playTrack(playlist.get(indexTrack), indexTrack, Duration.millis(currentFile.getLengthInMilliseconds()/2), Duration.millis(currentFile.getLengthInMilliseconds()/2+1500));

    }

    /**
     * Воспроизвести трек.
     *
     * @param currentTrack
     * @param prev
     * @param indexTrack
     * @param position
     * @param endPosition
     */
    public void playTrack(MusicFile currentTrack, PlayCommand prev, int indexTrack, Duration position, Duration endPosition) {
        this.indexTrack.set(indexTrack);
        this.positionTrack.set(DEFAULT_POSITION);
        media = new Media(new File(currentTrack.getPath()).toURI().toString());

        setTextCurrentTrack(currentTrack.getPath());

        player = new MediaPlayer(media);
        player.setStartTime(position);
        player.setStopTime(endPosition);
        player.setOnEndOfMedia(prev);
        player.play();
    }

    public void setTextCurrentTrack(String currentTrack) {
        panel.setText(currentTrack);
    }

    public ArrayList<MusicFile> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(ArrayList<MusicFile> playlist) {
        this.playlist = playlist;
    }

    public void stopMusic() {
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
        if (player != null) {
            player.pause();
        }
    }

    public void nextMusic() {
        indexTrack.set(getNewIndex(true));
        if (player != null) {
            player.stop();
        }
        playList(indexTrack.get(), -1);
    }

    private int getNewIndex(boolean increment) {
        int currentIndex = indexTrack.get();
        if (currentIndex + 1 >= playlist.size() && increment) {
            currentIndex = currentIndex - playlist.size() + 1;
        } else if (increment) {
            currentIndex = currentIndex + 1;
        } else if (currentIndex - 1 < 0) {
            currentIndex = currentIndex + playlist.size() - 1;
        } else {
            currentIndex = currentIndex - 1;
        }
        return currentIndex;
    }

    public int getIndexTrack() {
        return indexTrack.get();
    }

    public Duration getCurrentTime() {
        return player.getCurrentTime();
    }

    public MediaPlayer getTrack() {
        return player;
    }

    public void previousTrack() {
        indexTrack.set(getNewIndex(false));
        if (player != null) {
            player.stop();
        }
        playList(indexTrack.get(), -1);
    }

    public void rewind(double value) {
        if (player != null) {
            double v = player.getMedia().getDuration().toMillis() / 100 * value;
            if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                player.stop();
            }
            player.setStartTime(Duration.millis(v));
            player.play();
            return;
        }
        playList(indexTrack.get(), value);
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public void incrementIndex(int indexTrack) {
        this.indexTrack.set(indexTrack);
    }

    public boolean retry() {
        if (retry.get()) {
            retry.set(false);
        } else if (!retry.get()) {
            retry.set(true);
        }
        return retry.get();
    }

    public boolean getRetry() {
        return retry.get();
    }

    public MediaPlayer.Status getStatus() {
        if (player == null) {
            return MediaPlayer.Status.UNKNOWN;
        }
        return player.getStatus();
    }

    public void setIndexTrack(int indexTrack) {
        this.indexTrack.set(indexTrack);
    }
}
