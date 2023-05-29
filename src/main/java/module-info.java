module adbistju.system.musiccollection {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.jfoenix;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires mp3agic;
    requires jlayer;
    requires java.desktop;
    requires commons.io;

    opens adbistju.system.music.collection to javafx.fxml;
    exports adbistju.system.music.collection;
    exports adbistju.system.music.collection.musicsystem;
    opens adbistju.system.music.collection.musicsystem to javafx.fxml;
    exports adbistju.system.music.collection.cdi;
    opens adbistju.system.music.collection.cdi to javafx.fxml;
}