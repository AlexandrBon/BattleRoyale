package hse.java.cr.events;

public class PlayerUpdateEvent {
    public static class ObjectState {
        public int index;
        public float x;
        public float y;
        public boolean isLeft;
        public float speed = 1f;
    }
    public ObjectState[] objectStates;

    public PlayerUpdateEvent() {

    }
}