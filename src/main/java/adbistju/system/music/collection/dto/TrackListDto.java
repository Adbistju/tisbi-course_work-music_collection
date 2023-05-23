package adbistju.system.music.collection.dto;

import java.io.File;

public class TrackListDto implements GuiFileDto {

    private File file;
    private String name;

    public TrackListDto(File file) {
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
