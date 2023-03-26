module adbistju.system.musiccollection {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires mp3agic;
    requires jlayer;
    requires java.desktop;

    opens adbistju.system.music.collection to javafx.fxml;
    exports adbistju.system.music.collection;
}