package adbistju.system.music.collection.fileSysten;

import adbistju.system.music.collection.cdi.DIControl;
import adbistju.system.music.collection.cdi.PostConstruct;
import adbistju.system.music.collection.UIMemento;
import adbistju.system.music.collection.dto.FolderDto;
import adbistju.system.music.collection.dto.GuiFileDto;
import adbistju.system.music.collection.dto.MusicFileDto;
import adbistju.system.music.collection.dto.TrackListDto;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager implements PostConstruct {

    private static final String mp3file = "(.*).(mp3)$";
    private static final String txls = "(.*).(txls)$";

    protected UIMemento uiMemento;
    protected String currentFolder = "startFolder";
    protected String root = "startFolder";

    public FileManager(String currentFolder) {
        this.currentFolder = currentFolder;
        this.root = currentFolder;
    }

    public void setRoot(String root) {
        this.currentFolder = root;
        this.root = root;
    }

    public void setCurrentFolder(String currentFolder) {
        this.currentFolder = currentFolder;
    }

    public String getCurrentFolder() {
        return currentFolder;
    }

    public String getPrevFolder() {
        return Path.of(currentFolder).getParent().toString();
    }

    /**
     * Копировать список
     * @param sourceFileName
     * @param destFileName
     */
    public void copyFile(String sourceFileName, String destFileName) {
        File source = new File(currentFolder + "\\" + sourceFileName);
        File dest = new File(currentFolder + "\\" + destFileName);
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            System.err.println("Произошла ошибка!");
        }
    }

    /**
     * Копировать список
     * @param sourceFileName
     */
    public void copyFile(String sourceFileName) {
        File source = new File(currentFolder + "\\" + sourceFileName);
        String[] arr = sourceFileName.split("\\.");
        File dest = new File(currentFolder + "\\" + arr[arr.length - 2] + " - copy" + "." + arr[arr.length - 1]);
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            System.err.println("Произошла ошибка!");
        }
    }

    /**
     * Удалить список музыки.
     * @param sourceFileName
     */
    public void deleteFile(String sourceFileName) {
        File source = new File(currentFolder + "\\" + sourceFileName);
        FileUtils.deleteQuietly(source);
    }

    /**
     * Создать список.
     * @param fileName
     */
    public void createFile(String fileName) {
        File file = new File(currentFolder + "\\" + fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Произошла ошибка!");
        }
    }

    public void fileContent(String fileName) {
        File file = new File(currentFolder + "\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.err.println("Произошла ошибка!");
        }
    }

    public void changeDirectory(String folderName) {
        if (folderName.equals("/")) {
            this.currentFolder = this.root;
        } else if (folderName.equals("..")) {
            int startLastFolderPosition = this.currentFolder.lastIndexOf("\\");
            this.currentFolder = this.currentFolder.substring(0, startLastFolderPosition);
        } else {
            this.currentFolder = this.currentFolder + "\\" + folderName;
        }
    }

    public void createDir(String fileName) {
        new File(currentFolder + "\\" + fileName).mkdir();
    }

    public List<GuiFileDto> listOfFiles() {
        return listOfFiles(currentFolder);
    }

    public List<GuiFileDto> listOfFiles(String path) {
        List<GuiFileDto> filesList = new ArrayList<>();
        File currentFolderAsFile = new File(path);

        File files[] = currentFolderAsFile.listFiles();

        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                filesList.add(new FolderDto(file));
            } else if (fileName.matches(mp3file)) {
                filesList.add(new MusicFileDto(file));
            } else if (fileName.matches(txls)) {
                filesList.add(new TrackListDto(file));
            }
        }
        setCurrentFolder(path);
        return filesList;
    }

    @Override
    public void construct() {
        this.uiMemento = (UIMemento) DIControl.getInstance("memento");
    }

}