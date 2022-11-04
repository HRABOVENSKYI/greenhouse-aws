package ua.lviv.iot.greenhouse.models;

public enum SensorType {
    AIR_SENSOR("AIR_SENSOR"),
    SOIL_SENSOR("SOIL_SENSOR"),
    LUMINOSITY_SENSOR("LUMINOSITY_SENSOR"),
    GENERAL_SENSOR("GENERAL_SENSOR");

    private final String name;

    SensorType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
