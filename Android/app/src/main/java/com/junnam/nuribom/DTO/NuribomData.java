package com.junnam.nuribom.DTO;

import java.util.ArrayList;

/**
 * Created by hong on 2016. 9. 20..
 */
public class NuribomData {

    private ArrayList<MarkerData> markerDatas;

    private boolean hospitalVisible;
    private boolean centerVisible;
    private boolean emergencyVisible;
    private boolean dentistVisible;

    public NuribomData() {
        markerDatas = new ArrayList<>();

        hospitalVisible = true;
        centerVisible = true;
        emergencyVisible = true;
        dentistVisible = true;
    }

    public void addData(MarkerData markerData) {
        markerDatas.add(markerData);
    }

    public MarkerData getData(int i) {
        return markerDatas.get(i);
    }

    public int getDataSize() {
        return markerDatas.size();
    }

    public boolean isHospitalVisible() {
        return hospitalVisible;
    }

    public void setHospitalVisible(boolean hospitalVisible) {
        this.hospitalVisible = hospitalVisible;
    }

    public boolean isCenterVisible() {
        return centerVisible;
    }

    public void setCenterVisible(boolean centerVisible) {
        this.centerVisible = centerVisible;
    }

    public boolean isEmergencyVisible() {
        return emergencyVisible;
    }

    public void setEmergencyVisible(boolean emergencyVisible) {
        this.emergencyVisible = emergencyVisible;
    }

    public boolean isDentistVisible() {
        return dentistVisible;
    }

    public void setDentistVisible(boolean dentistVisible) {
        this.dentistVisible = dentistVisible;
    }
}
