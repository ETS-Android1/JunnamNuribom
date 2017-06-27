package com.junnam.nuribom.DTO;

/**
 * Created by hong on 2016. 10. 9..
 */
public class MyFavorData {

    private int idx;
    private int type;
    private int type_idx;
    private String member_id;
    private String type_name;
    private String name;

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

    public int getType_idx() {
        return type_idx;
    }

    public void setType_idx(int type_idx) {
        this.type_idx = type_idx;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
