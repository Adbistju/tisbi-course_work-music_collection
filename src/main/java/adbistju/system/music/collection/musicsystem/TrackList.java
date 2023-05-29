package adbistju.system.music.collection.musicsystem;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

/**
 * Создает односвязный список воспроизведения
 */
public class TrackList {

    protected Node firstElement;
    private Node current;

    public TrackList() {
    }

    /**
     * Отдает первую ноу односвязного списка, для установки следующего трека воспроизведения с зацикленной зависимостью.
     */
    public Node toTrackListRepeat(int indexTrack, List<MusicFile> playlist, Player player) {
        for (int i = indexTrack + 1; i < playlist.size(); i++) {
            insertAfterRepeat(null, playlist.get(i).getFilename(), new MediaPlayer(new Media(new File(playlist.get(i).getPath()).toURI().toString())), indexTrack, player);
        }
        for (int i = 0; i <= indexTrack; i++) {
            insertAfterRepeat(null, playlist.get(i).getFilename(), new MediaPlayer(new Media(new File(playlist.get(i).getPath()).toURI().toString())), indexTrack, player);
        }
        return firstElement;
    }

    /**
     * Отдает первую ноу односвязного списка, для установки следующего трека воспроизведения. Не зациклен.
     */
    public Node toTrackListNoRepeat(int indexTrack, List<MusicFile> playlist, Player player) {
        for (int i = indexTrack + 1; i < playlist.size(); i++) {
            insertAfterNoRepeat(null, playlist.get(i).getFilename(), new MediaPlayer(new Media(new File(playlist.get(i).getPath()).toURI().toString())), indexTrack, player);
        }
        return firstElement;
    }

    /**
     * Отдает первую ноу односвязного списка, для установки следующего трека воспроизведения. Зациклен.
     */
    private void insertAfterNoRepeat(PlayCommand nextPlay, String path, MediaPlayer player, int indexTrack, Player controlPlayer) {
        Node newItem = new Node(nextPlay, path, player, indexTrack, controlPlayer);
        if (firstElement == null) {
            firstElement = newItem;
            current = firstElement;
        } else {
            current.setNextPlay(newItem);
            current = newItem;
        }
    }


    private void insertAfterRepeat(PlayCommand nextPlay, String path, MediaPlayer player, int indexTrack, Player controlPlayer) {
        Node newItem = new Node(nextPlay, path, player, indexTrack, controlPlayer);
        if (firstElement == null) {
            firstElement = newItem;
            current = firstElement;
        } else {
            current.setNextPlay(newItem);
            current = newItem;
            newItem.setNextPlay(firstElement);
        }
    }

    /**
     * Класс ноды для облегчения создания односвязного списка.
     */
    class Node extends PlayCommand {

        public Node(PlayCommand nextPlay, String path, MediaPlayer player, int indexTrack, Player controlPlayer) {
            super(nextPlay, path, player, indexTrack, controlPlayer);
        }

        public Node(PlayCommand nextPlay, String path, MediaPlayer player, int indexTrack, Duration startPosition, Duration endPosition, Player controlPlayer) {
            super(nextPlay, path, player, indexTrack, startPosition, endPosition, controlPlayer);
        }

    }

}
