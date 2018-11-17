package com.challenge.next.core.context;

public class ContextDriverPoints {

    int originX;

    int originY;

    public ContextDriverPoints(int originX, int originY) {
        this.originX = originX;
        this.originY = originY;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    @Override
    public String toString() {
        return "ContextDriverPoints{" +
                "originX=" + originX +
                ", originY=" + originY +
                '}';
    }

    public boolean isEmpty() {
        return this.isEmpty();
    }
}
