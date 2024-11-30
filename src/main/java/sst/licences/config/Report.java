package sst.licences.config;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Report {

    public Report(String payments) {
        this.payments = payments;
    }

    public Report() {
    }

    @Getter
    private String payments;

    @Getter
    private String members;
}
