package sst.licences.config;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Directories {
    public Directories(String working, String export, String report, String download) {
        this.working = working;
        this.export = export;
        this.report = report;
        this.download = download;
    }

    public Directories() {
    }

    @Getter
    private String working;
    @Getter
    private String export;
    @Getter
    private String report;
    @Getter
    private String download;
}
