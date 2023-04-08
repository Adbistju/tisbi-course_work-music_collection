package adbistju.system.music.collection;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Класс для следующих по очереди треков.
 */
public class PlayCommand implements Runnable {

    private Player controlPlayer;
    private PlayCommand nextPlay;
    private MediaPlayer player;
    private Duration startPosition;
    private Duration endPosition;
    private int indexTrack;

    public PlayCommand(PlayCommand nextPlay, MediaPlayer player, int indexTrack, Duration startPosition, Duration endPosition, Player controlPlayer) {
        this.nextPlay = nextPlay;
        this.player = player;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.controlPlayer = controlPlayer;
        this.indexTrack = indexTrack;
    }

    public PlayCommand(PlayCommand nextPlay, MediaPlayer player, int indexTrack, Player controlPlayer) {
        this.nextPlay = nextPlay;
        this.player = player;
        this.controlPlayer = controlPlayer;
        this.indexTrack = indexTrack;
    }

    @Override
    public void run() {
        player.setOnEndOfMedia(nextPlay);
        connectTaskToPlayer();
        initTime();

        System.out.println("start new track " + player.getMedia().getMetadata());
        player.play();
    }

    /**
     * Подключение задачие к плееру, для возмонжности управления воспроизведением.
     */
    private void connectTaskToPlayer() {
        controlPlayer.setPlayer(player);
        controlPlayer.setMedia(player.getMedia());
        controlPlayer.incrementIndex(indexTrack);
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
