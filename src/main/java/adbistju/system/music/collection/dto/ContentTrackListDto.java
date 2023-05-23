package adbistju.system.music.collection.dto;

import java.io.File;

public class ContentTrackListDto implements GuiFileDto {

    private File file;
    private String content;

    public ContentTrackListDto(File file, String content) {
        this.file = file;
        this.content = content;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return content;
    }

}
