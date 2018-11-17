package com.next.challenge.core.context;

public class ContextPassengerPoints {

    int originX;

    int originY;

    int destX;

    int destY;

    public ContextPassengerPoints(int originX, int originY, int destX, int destY) {
        this.originX = originX;
        this.originY = originY;
        this.destX = destX;
        this.destY = destY;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public int getDestX() {
        return destX;
    }

    public int getDestY() {
        return destY;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    public void setDestX(int destX) {
        this.destX = destX;
    }

    public void setDestY(int destY) {
        this.destY = destY;
    }

    @Override
    public String toString() {
        return "ContextPassengerPoints{" +
                "originX=" + originX +
                ", originY=" + originY +
                ", destX=" + destX +
                ", destY=" + destY +
                '}';
    }

    public boolean isEmpty() {
        return this.isEmpty();
    }
}
