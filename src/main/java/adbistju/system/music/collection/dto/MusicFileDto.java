package adbistju.system.music.collection.dto;

import java.io.File;

public class MusicFileDto implements GuiFileDto {

    private File file;
    private String name;

    public MusicFileDto(File file) {
        this.file = file;
        this.name = file.getName();
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

}
