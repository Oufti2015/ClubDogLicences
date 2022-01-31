package sst.licences.config;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Application {
    public Application(String name) {
        this.name = name;
    }

    public Application() {
    }

    @Getter
    private String name;
}
