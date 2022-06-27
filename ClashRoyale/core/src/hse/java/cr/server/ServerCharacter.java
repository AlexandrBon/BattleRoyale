package hse.java.cr.server;

import hse.java.cr.events.PlayerUpdateEvent.ObjectState;

public class ServerCharacter {
    private int index;
    private float x;
    private float y;
    private final boolean isMine;
    private float speed = 3f;

    public ServerCharacter(int index, float x, float y, boolean isMine) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void addToX(float x) {
        this.x += x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isMine() {
        return isMine;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public ObjectState getState() {
        ObjectState characterState = new ObjectState();
        characterState.index = index;
        characterState.x = x;
        characterState.y = y;
        characterState.isLeft = isMine;
        characterState.speed = speed;
        return characterState;
    }
}
