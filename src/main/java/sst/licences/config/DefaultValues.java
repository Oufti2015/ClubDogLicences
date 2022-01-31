package sst.licences.config;

import lombok.Getter;
import lombok.ToString;

@ToString
public class DefaultValues {
    public DefaultValues(String language, String country) {
        this.language = language;
        this.country = country;
    }

    public DefaultValues() {
    }

    @Getter
    private String language;
    @Getter
    private String country;
}
