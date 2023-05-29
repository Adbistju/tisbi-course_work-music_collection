package adbistju.system.music.collection.cdi;

public class Instance implements PostConstruct {

    private Object data;

    public Instance(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    @Override
    public void construct() {

    }
}
