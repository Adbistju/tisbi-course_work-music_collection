package adbistju.system.music.collection;

import adbistju.system.music.collection.cdi.DIControl;
import adbistju.system.music.collection.cdi.PostConstruct;
import adbistju.system.music.collection.musicsystem.Player;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import static javafx.scene.media.MediaPlayer.Status.PLAYING;

public class SliderTrackUi implements PostConstruct {

    private Player player;
    private AtomicBoolean clickSlider = new AtomicBoolean(false);
    private AtomicBoolean playerIsWork = new AtomicBoolean(true);

    /**
     * Слайдер трека.
     * @return
     */
    public Slider sliderTrack() {
        Slider slider = new Slider(0, 100, 50);
        slider.getStylesheets().add(0, "style.css");

        slider.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100.0;
            return String.format(Locale.US, "-slider-track-color: linear-gradient(to left, -slider-filled-track-color 0%%, "
                            + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    100 - percentage, 100 - percentage);
        }, slider.valueProperty(), slider.minProperty(), slider.maxProperty()));

        EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                player.rewind(slider.getValue());
                clickSlider.set(false);
            }
        };
        slider.addEventFilter(MouseEvent.MOUSE_RELEASED, releasedHandler);

        EventHandler<MouseEvent> pressedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                clickSlider.set(true);
            }
        };
        slider.addEventFilter(MouseEvent.MOUSE_PRESSED, pressedHandler);

        Thread thread = new Thread(() -> {
            long time = System.currentTimeMillis();
            long delta = 500;
            while (playerIsWork.get()) {
                if ((time + delta) - System.currentTimeMillis() <= 0) {
                    time = System.currentTimeMillis();
                    if (player.getStatus() == PLAYING && !clickSlider.get()) {
                        slider.setValue(player.getCurrentTime().toMillis() / player.getTrack().getMedia().getDuration().toMillis() * 100);
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        return slider;
    }

    @Override
    public void construct() {

        this.player = (Player) DIControl.getInstance("player");

    }

    public void setPlayerIsWorkFalse() {
        this.playerIsWork.set(false);
    }
}
