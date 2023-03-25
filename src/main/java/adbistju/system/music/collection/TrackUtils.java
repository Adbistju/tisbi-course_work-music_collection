package adbistju.system.music.collection;

public class TrackUtils {

    /**
     * Выдает долю запрошенного процента фреймов.
     * @param musicFile файл у которого получаем фреймы
     * @param percent процент фреймов от файла
     * @return фреймы
     */
    public static int convertPercentToFrame(MusicFile musicFile, float percent) {
        long a = (musicFile.getFrameCount() / 100);
        int position = Math.toIntExact((long) (a * percent));
        return position;
    }

}
