package adbistju.system.music.collection.musicsystem;

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

    private String fileName;

    public MusicFile() {
    }

    public MusicFile(String path) throws IOException, UnsupportedTagException, InvalidDataException {
        super(path);
        File file = new File(Paths.get(path).toString());
        this.fileName = file.getName();
        this.path = Paths.get(path);
    }

    public MusicFile(File file) throws IOException, UnsupportedTagException, InvalidDataException {
        super(file);
        this.fileName = file.getName();
        this.path = Paths.get(file.getPath());
    }

    public String getPath() {
        return path.toString();
    }

    public String getFilename() {
        return fileName;
    }
    
}
