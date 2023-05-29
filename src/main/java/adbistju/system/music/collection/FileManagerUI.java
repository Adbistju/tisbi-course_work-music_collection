package adbistju.system.music.collection;

import adbistju.system.music.collection.cdi.DIControl;
import adbistju.system.music.collection.cdi.Instance;
import adbistju.system.music.collection.cdi.PostConstruct;
import adbistju.system.music.collection.fileSysten.FileSystemButtonGenerator;
import adbistju.system.music.collection.fileSysten.TrackListReader;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class FileManagerUI implements PostConstruct {

    private ViewGenerator viewGenerator;
    private TrackListReader trackListReader;
    private TextField textField;
    private ScrollPane fileManagerScrollPane;
    private FileSystemButtonGenerator fileSystemButtonGenerator;

    public Panel fileManager() {
        Panel panel = new Panel();
        panel.setMinSize(300, 300);
        panel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3)");
        panel.getStylesheets().add(0, "style.css");

        String path = trackListReader.getCurrentFolder();
        textField.setText(path);

        Button search = new Button("search");
        EventHandler<MouseEvent> relesedsearch = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                viewGenerator.recreateFileManager();
            }
        };
        search.addEventFilter(MouseEvent.MOUSE_RELEASED, relesedsearch);

        Button button = new Button("prev");
        EventHandler<MouseEvent> relesed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                textField.setText(trackListReader.getPrevFolder());
                String path = textField.getCharacters().toString();
                textField.setText(path);
                viewGenerator.recreateFileManager();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, relesed);

        HBox hBox = new HBox(button, textField, search, fileSystemButtonGenerator.createSavePlayList());
        panel.setTop(hBox);
        viewGenerator.recreateFileManager();
        panel.setCenter(fileManagerScrollPane);

        return panel;
    }

    @Override
    public void construct() {
        this.viewGenerator = (ViewGenerator) DIControl.getInstance("viewGenerator");
        this.trackListReader = (TrackListReader) DIControl.getInstance("trackListReader");
        this.textField = (TextField) ((Instance) DIControl.getInstance("textPath")).getData();
        this.fileManagerScrollPane = (ScrollPane) ((Instance) DIControl.getInstance("fileManagerScrollPane")).getData();
        this.fileSystemButtonGenerator = (FileSystemButtonGenerator) DIControl.getInstance("fileSystemButtonGenerator");
    }
}
