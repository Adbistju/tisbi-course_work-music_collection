package adbistju.system.music.collection.fileSysten;

import adbistju.system.music.collection.dto.ContentTrackListDto;
import adbistju.system.music.collection.dto.GuiFileDto;
import adbistju.system.music.collection.dto.TrackListDto;
import adbistju.system.music.collection.musicsystem.MusicFile;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrackListReader extends FileManager {

    public TrackListReader(String currentFolder) {
        super(currentFolder);
    }

    public List<GuiFileDto> readContent(String path) {
        List<GuiFileDto> contentList = new ArrayList<>();
        File file = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                contentList.add(new ContentTrackListDto(file, line));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Произошла ошибка!");
        }
        return contentList;
    }

    public void removeLineFromFile(File file, String searchTerm) throws IOException {
        File targetFile = file;
        StringBuffer fileContents = new StringBuffer(FileUtils.readFileToString(targetFile));
        String[] fileContentLines = fileContents.toString().split(System.lineSeparator());
        RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "rw");
        randomAccessFile.setLength(0);
        randomAccessFile.close();
        StringBuffer newFileContent = new StringBuffer();
        searchTerm = Path.of(searchTerm).getFileName().toString();
        String currentLine;
        for (int i = 0; i < fileContentLines.length; i++) {
            currentLine = Path.of(fileContentLines[i]).getFileName().toString();
            if (currentLine.equals(searchTerm)) {
                continue;
            }
            newFileContent.append(fileContentLines[i] + System.lineSeparator());
        }
        FileUtils.writeStringToFile(targetFile, newFileContent.toString().trim());
    }

    public void addLineFromFile(TrackListDto trackListDto, String value) throws IOException {
        value = System.lineSeparator() + value;
        Files.write(Paths.get(trackListDto.getFile().getPath()), value.getBytes(), StandardOpenOption.APPEND);
    }

    public void save(ArrayList<MusicFile> playlist, String name) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < playlist.size(); i++) {
            stringBuilder.append(playlist.get(i).getPath()+System.lineSeparator());
        }

        if (name == null) {
            name = LocalDateTime.now().toString().replace('.', 'y').replace(':', '-');
        }
        Files.write(Path.of(currentFolder + "\\" + name + ".txls"), stringBuilder.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

    }

}
