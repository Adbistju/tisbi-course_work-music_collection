package adbistju.system.music.collection;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

/**
 * Класс контороля громкости всего приложения.
 */
public class VolumeController {

    public VolumeController() {

    }

    /**
     * Установить громкость приложения.
     *
     * @param percentLevel - громкость в процентах.
     */
    public void volumeControl(float percentLevel) {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixers) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getTargetLineInfo();
            for (Line.Info lineInfo : lineInfos) {
                Line line = null;
                boolean opened = true;
                try {
                    line = mixer.getLine(lineInfo);
                    opened = line.isOpen() || line instanceof Clip;
                    if (!opened) {
                        line.open();
                    }
                    FloatControl volControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                    volControl.setValue(floatVolumeLevel(volControl, percentLevel));
                } catch (LineUnavailableException | IllegalArgumentException e) {

                } finally {
                    if (line != null && !opened) {
                        line.close();
                    }
                }
            }
        }
    }

    private float floatVolumeLevel(FloatControl volControl, float percentLevel) {
        float mr = volControl.getMaximum() - volControl.getMinimum();
        return mr / 100 * percentLevel;
    }

}
