package sst.licences.config;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Club {
    public Club(String name) {
        this.name = name;
    }

    public Club() {
    }

    @Getter
    private String name;
}
