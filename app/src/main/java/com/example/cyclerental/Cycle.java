package com.example.cyclerental;

public class Cycle {
    private int cycleId;
    private String cname;
    private double cprice;
    private String type;
    private String location;
    private int availability;
    private String cimagePath;

    public Cycle(int cycleId, String cname, double cprice, String type, String location, int availability, String cimagePath) {
        this.cycleId = cycleId;
        this.cname = cname;
        this.cprice = cprice;
        this.type = type;
        this.location = location;
        this.availability = availability;
        this.cimagePath = cimagePath;
    }

    public int getCycleId() {
        return cycleId;
    }

    public String getCname() {
        return cname;
    }

    public double getCprice() {
        return cprice;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public int getAvailability() {
        return availability;
    }

    public String getCimagePath() {
        return cimagePath;
    }
}


