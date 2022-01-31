package sst.licences.config;

import lombok.Getter;

public class DataFiles {
    public DataFiles(String members, String comite) {
        this.members = members;
        this.comite = comite;
    }

    public DataFiles() {
    }

    @Getter
    private String members;
    @Getter
    private String comite;
}
