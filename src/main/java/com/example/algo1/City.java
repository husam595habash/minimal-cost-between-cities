package com.example.algo1;

import java.util.Arrays;

public class City {

    private String city;
    private int numberOfaccessCities;
    private String[] accessCities;
    private int[] hotelCosts;
    private int[] petrolCosts;
    private int stage;

    public City(String city, int numberOfaccessCities, String[] accessCities, int[] hotelCosts, int[] petrolCosts , int stage) {
        super();
        this.city = city;
        this.numberOfaccessCities = numberOfaccessCities;
        this.accessCities = accessCities;
        this.hotelCosts = hotelCosts;
        this.petrolCosts = petrolCosts;
        this.stage = stage;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNumberOfaccessCities() {
        return numberOfaccessCities;
    }

    public void setNumberOfaccessCities(int numberOfaccessCities) {
        this.numberOfaccessCities = numberOfaccessCities;
    }

    public String[] getaccessCities() {
        return accessCities;
    }

    public void setaccessCities(String[] accessCities) {
        this.accessCities = accessCities;
    }

    public int[] getHotelCosts() {
        return hotelCosts;
    }

    public void setHotelCosts(int[] hotelCosts) {
        this.hotelCosts = hotelCosts;
    }

    public int[] getPetrolCosts() {
        return petrolCosts;
    }

    public void setPetrolCosts(int[] petrolCosts) {
        this.petrolCosts = petrolCosts;
    }

    public String[] getAccessCities() {
        return accessCities;
    }

    public void setAccessCities(String[] accessCities) {
        this.accessCities = accessCities;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    @Override
    public String toString() {
        return "City [city=" + city + ", numberOfaccessCities=" + numberOfaccessCities + ", accessCities="
                + Arrays.toString(accessCities) + ", hotelCosts=" + Arrays.toString(hotelCosts) + ", petrolCosts="
                + Arrays.toString(petrolCosts) + "]";
    }

    public String getAccessCity(int location) {
        return this.accessCities[location];
    }

    public int[] getTotalCosts() {
        int[] totalCosts = new int[numberOfaccessCities];
        for (int i = 0; i < numberOfaccessCities; i++)
            totalCosts[i] = petrolCosts[i] + hotelCosts[i];
        return totalCosts;

    }

}
