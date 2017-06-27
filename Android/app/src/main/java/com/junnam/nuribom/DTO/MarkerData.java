package com.junnam.nuribom.DTO;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hong on 2016. 9. 23..
 */
public class MarkerData {

    private int idx;
    private int type; // 1 = hospital 2 = center 3 = emergency 4 = dentist
    private String name;
    private LatLng locaton;
    private String addr;
    private float rate;

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

    public LatLng getLocaton() {
        return locaton;
    }

    public void setLocaton(LatLng locaton) {
        this.locaton = locaton;
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
