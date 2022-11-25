package ua.lviv.iot.greenhouse.enums;

public enum Permission {
    ZOOS_READ("ZOOS_READ"),
    ZOOS_WRITE("ZOOS_WRITE");

    private final String name;

    Permission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
