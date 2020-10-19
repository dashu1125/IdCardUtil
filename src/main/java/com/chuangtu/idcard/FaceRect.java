package com.chuangtu.idcard;

public class FaceRect {
    private Object size;

    private Object center;

    private Long angle;

    public FaceRect() {
    }

    public FaceRect(Object size, Point center, Long angle) {
        this.size = size;
        this.center = center;
        this.angle = angle;
    }

    public Object getSize() {
        return size;
    }

    public void setSize(Object size) {
        this.size = size;
    }

    public Object getCenter() {
        return center;
    }

    public void setCenter(Object center) {
        this.center = center;
    }

    public Long getAngle() {
        return angle;
    }

    public void setAngle(Long angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "FaceRect{" +
                "size=" + size +
                ", center=" + center +
                ", angle=" + angle +
                '}';
    }
}
