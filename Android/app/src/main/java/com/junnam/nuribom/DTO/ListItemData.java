package com.junnam.nuribom.DTO;

/**
 * Created by hong on 2016. 10. 2..
 */
public class ListItemData {

    private int idx;
    private int type;
    private String name;
    private double distance;
    private String addr;
    private float rate;

    public ListItemData(int idx, int type, String name, double distance, String addr, float rate) {
        this.idx = idx;
        this.type = type;
        this.name = name;
        this.distance = distance;
        this.addr = addr;
        this.rate = rate;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
