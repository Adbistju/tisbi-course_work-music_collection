package adbistju.system.music.collection;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MusicFile extends Mp3File {

//    private InputStream musicStream;
    private Path path;

    public MusicFile() {
    }

    public MusicFile(String path) throws IOException, UnsupportedTagException, InvalidDataException {
        super(path);
//        this.musicStream = new FileInputStream(path);
        this.path = Paths.get(path);
    }

    public MusicFile(File file) throws IOException, UnsupportedTagException, InvalidDataException {
        super(file);
//        this.musicStream = new FileInputStream(file);
        this.path = Paths.get(file.getPath());
    }

    public MusicFile(Path path) throws IOException, UnsupportedTagException, InvalidDataException {
        super(path);
        this.path = path;
//        this.musicStream = new FileInputStream(path.toString());
    }
//

    public String getPath() {
        return path.toString();
    }
//    public InputStream getMusicStream() throws FileNotFoundException {
//        if (musicStream == null) {
//            this.musicStream = new FileInputStream(super.path.toString());
//        }
//        return musicStream;
//    }

}
