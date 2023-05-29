package adbistju.system.music.collection.cdi;

import java.util.HashMap;

public class DIControl {

    public static HashMap<String, PostConstruct> container = new HashMap<>();

    public static Object getInstance(String name) {
        return container.get(name);
    }

    public static Object putInstance(String name, PostConstruct instance) {
        return container.put(name, instance);
    }

    public static void initAll() {
        container.values().forEach(PostConstruct::construct);
    }
}
