package sst.licences.model;


import lombok.Getter;
import lombok.Setter;

public class Country {
    @Getter
    private String code;
    @Setter
    private String name;

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
