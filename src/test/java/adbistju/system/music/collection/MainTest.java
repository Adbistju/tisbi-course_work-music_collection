package adbistju.system.music.collection;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MainTest {

    public static String muss = "30112022Ig_2.mp3";
    public static String muss1 = "01. Butterfly (split).mp3";
    public static String muss2 = "01. Don-t Blame Me (Split).mp3";
    public static String muss3 = "John_Coltrane_-_Blue_Train_(-).mp3";

    private static Scanner scanner = new Scanner(System.in);
    private static Player player = new Player();

    @Test
    public void getDataFileTest() throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File mp3file = new Mp3File(muss1);
        if (mp3file.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            System.out.println("Track: " + id3v1Tag.getTrack());
            System.out.println("Artist: " + id3v1Tag.getArtist());
            System.out.println("Title: " + id3v1Tag.getTitle());
            System.out.println("Album: " + id3v1Tag.getAlbum());
            System.out.println("Year: " + id3v1Tag.getYear());
            System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
            System.out.println("Comment: " + id3v1Tag.getComment());
        }
        System.out.println(mp3file.getLengthInSeconds());
        System.out.println(mp3file.getStartOffset());
        System.out.println(mp3file.getLayer());
    }
    
    @Test
    public void playPlayListTest() throws InvalidDataException, UnsupportedTagException, IOException {
        MusicFile musicFile = new MusicFile(muss);
        MusicFile musicFile1 = new MusicFile(muss1);
        player.setPlaylist(List.of(musicFile, musicFile1));
        player.playList();
    }

}
