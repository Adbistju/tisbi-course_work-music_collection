package adbistju.system.music.collection.fileSysten;

import adbistju.system.music.collection.dto.TrackListDto;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class TrackListReader {

    private String currentFolder = "startFolder";

    public List<Content> readContent(TrackListDto trackListDto) {
        List<Content> contentList = new ArrayList<>();
//        File file = new File(currentFolder + "\\" + fileName);
        File file = trackListDto.getFile();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                contentList.add(new Content(file, line));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Произошла ошибка!");
        }
        return contentList;
    }

    public void removeLineFromFile(TrackListDto trackListDto, String searchTerm) throws IOException {
//        File targetFile = new File(currentFolder + "\\" + fileName);
        File targetFile = trackListDto.getFile();
        StringBuffer fileContents = new StringBuffer(FileUtils.readFileToString(targetFile));
        String[] fileContentLines = fileContents.toString().split(System.lineSeparator());
        RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "rw");
        randomAccessFile.setLength(0);
        randomAccessFile.close();
        fileContents = new StringBuffer();
        for (int fileContentLinesIndex = 0; fileContentLinesIndex < fileContentLines.length; fileContentLinesIndex++) {
            if (fileContentLines[fileContentLinesIndex].contains(searchTerm)) {
                continue;
            }
            fileContents.append(fileContentLines[fileContentLinesIndex] + System.lineSeparator());
        }
        FileUtils.writeStringToFile(targetFile, fileContents.toString().trim());
    }

    public void addLineFromFile(TrackListDto trackListDto, String value) throws IOException {
        value = "\n" + value;
        Files.write(Paths.get(trackListDto.getFile().getPath()), value.getBytes(), StandardOpenOption.APPEND);
//        Files.write(Paths.get(currentFolder + "\\" + fileName), value.getBytes(), StandardOpenOption.APPEND);
    }

//    public static void main(String[] args) throws IOException {
//        File file = new File("startFolder" + "\\" + "1.txt");
//        TrackListDto trackListDto = new TrackListDto(file);
////        String fileName = "1.txt";
//        TrackListReader trackListReader = new TrackListReader();
//        List<Content> contentList = trackListReader.readContent(trackListDto);
//        contentList.forEach(System.out::println);
//        trackListReader.removeLineFromFile(trackListDto, "5");
//        contentList = trackListReader.readContent(trackListDto);
//        contentList.forEach(System.out::println);
//
//        trackListReader.addLineFromFile(trackListDto, "AAA");
//
//        contentList = trackListReader.readContent(trackListDto);
//        contentList.forEach(System.out::println);
//    }

    public class Content {

        private File file;
        private String content;

        public Content(File file, String content) {
            this.file = file;
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public File getFile() {
            return file;
        }

        @Override
        public String toString() {
            return "{" +
                    "content='" + content + '\'' +
                    '}';
        }
    }

}
