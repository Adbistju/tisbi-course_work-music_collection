package adbistju.system.music.collection;

import adbistju.system.music.collection.cdi.DIControl;
import adbistju.system.music.collection.cdi.PostConstruct;
import adbistju.system.music.collection.musicsystem.VolumeController;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.Locale;

public class VolumeUI implements PostConstruct {

    private static float level = 0;
    private VolumeController volumeController;

    public HBox divVolume() {
        HBox divVolume = new HBox();
        divVolume.getChildren().setAll(sliderVolume());
        divVolume.setAlignment(Pos.CENTER);
        return divVolume;
    }

    /**
     * Слайдер громкости прилоежния.
     * @return
     */
    public Slider sliderVolume() {
        Slider slider = new Slider(0, 100, 50);
        slider.getStylesheets().add(0, "style.css");
        volumeController.volumeControl(50);

        slider.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100.0;
            return String.format(Locale.US, "-slider-track-color: linear-gradient(to right, -slider-filled-track-color 0%%, "
                            + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, slider.valueProperty(), slider.minProperty(), slider.maxProperty()));
        EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                level = (float) slider.getValue();
                volumeController.volumeControl(level);
            }
        };
        slider.addEventFilter(MouseEvent.MOUSE_RELEASED, releasedHandler);
        return slider;
    }

    @Override
    public void construct() {

        this.volumeController = (VolumeController) DIControl.getInstance("volumeController");

    }
}
