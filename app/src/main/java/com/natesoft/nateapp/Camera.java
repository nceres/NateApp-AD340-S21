package com.natesoft.nateapp;

public final class Camera {

    private String type;
    private String description;
    private String imageUrl;
    private Double[] coordinates;

    public Camera() {
    }

    public Camera(String type, String description, String imageUrl, Double[] coordinates) {
        this.type = type;
        this.description = description;
        this.imageUrl = imageUrl;
        this.coordinates = coordinates;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Double[] getCoordinates() { return coordinates; }

    public void setCoordinates(Double[] coordinates) { this.coordinates = coordinates; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}
