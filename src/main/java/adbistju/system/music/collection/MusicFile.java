package adbistju.system.music.collection;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Файл музыкального трека.
 *
 * Mp3File - позволяет получить информацию об файле.
 */
public class MusicFile extends Mp3File {

    /**
     * Путь до файла.
     */
    private Path path;

    public MusicFile() {
    }

    public MusicFile(String path) throws IOException, UnsupportedTagException, InvalidDataException {
        super(path);
        this.path = Paths.get(path);
    }

    public MusicFile(File file) throws IOException, UnsupportedTagException, InvalidDataException {
        super(file);
        this.path = Paths.get(file.getPath());
    }

    public MusicFile(Path path) throws IOException, UnsupportedTagException, InvalidDataException {
        super(path);
        this.path = path;
    }

    public String getPath() {
        return path.toString();
    }

}
