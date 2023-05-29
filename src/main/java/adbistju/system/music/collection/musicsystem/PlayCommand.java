package adbistju.system.music.collection.musicsystem;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Класс для следующих по очереди треков.
 */
public class PlayCommand implements Runnable {

    /**
     * Ссылка на контроллер плеера.
     */
    private Player controlPlayer;
    /**
     * Ссылка на следующую композицию
     */
    private PlayCommand nextPlay;
    /**
     * Сам плеер, занимается воспроизведением.
     */
    private MediaPlayer player;
    /**
     * Указывает на какой временой отметке  начать воспроизведение.
     */
    private Duration startPosition;
    /**
     * Указывает на какой временой отметке закончить воспроизведение.
     */
    private Duration endPosition;
    /**
     * Номер трека в плейлисте.
     */
    private int indexTrack;
    /**
     * Имя файла, нужно для отображения в UI
     */
    private String path;

    public PlayCommand(PlayCommand nextPlay, String path, MediaPlayer player, int indexTrack, Duration startPosition, Duration endPosition, Player controlPlayer) {
        this.nextPlay = nextPlay;
        this.path = path;
        this.player = player;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.controlPlayer = controlPlayer;
        this.indexTrack = indexTrack;
    }

    public PlayCommand(PlayCommand nextPlay, String path, MediaPlayer player, int indexTrack, Player controlPlayer) {
        this.nextPlay = nextPlay;
        this.path = path;
        this.player = player;
        this.controlPlayer = controlPlayer;
        this.indexTrack = indexTrack;
    }

    @Override
    public void run() {
        player.setOnEndOfMedia(nextPlay);
        connectTaskToPlayer();
        initTime();
        controlPlayer.setTextCurrentTrack(path);
        player.play();
    }

    /**
     * Подключение задачи к плееру, для возмонжности управления воспроизведением.
     */
    private void connectTaskToPlayer() {
        controlPlayer.setPlayer(player);
        controlPlayer.setMedia(player.getMedia());
        controlPlayer.setIndexTrack(indexTrack);
    }

    /**
     * Установка времени начала воспроизведения и конца.
     */
    private void initTime() {
        if (startPosition == null | player.getCurrentTime().toMillis() > Duration.ZERO.toMillis()) {
            player.setStartTime(Duration.ZERO);
        } else if (player.getCurrentTime().toMillis() > startPosition.toMillis()) {
            player.setStartTime(startPosition);
        } else {
            player.setStartTime(startPosition);
        }

        if (endPosition != null) {
            player.setStopTime(endPosition);
        }
    }

    /**
     * Установить следующий трек по окончанию воспроизведения.
     * @param nextPlay
     */
    public void setNextPlay(PlayCommand nextPlay) {
        this.nextPlay = nextPlay;
    }
}
