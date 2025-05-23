package com.example.algo1;

public class CostEntry {
    private String path;
    private int cost;

    public CostEntry(String path, int cost) {
        this.path = path;
        this.cost = cost;
    }

    public String getPath() {
        return path;
    }

    public int getCost() {
        return cost;
    }

}
