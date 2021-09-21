package com.example.attendancesystem.ui;

public class StudentItem {
    private String name;
    private int roll;
    private String status;
    private long sid;

    public StudentItem(String name, int roll) {
        this.name = name;
        this.roll = roll;
        this.status = "";
    }

    public StudentItem(long sid, String name, int roll) {
        this.name = name;
        this.roll = roll;
        this.status = "";
        this.sid = sid;
    }
    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
