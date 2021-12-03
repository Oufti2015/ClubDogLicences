package sst.licences.report.members;

import lombok.Getter;

import java.time.LocalDate;

public enum ListType {
    ALL_MEMBERS("Tous les membres", "index.html"),
    ACTIVE_MEMBERS("Membres Actifs", "index-2.html"),
    INACTIVE_MEMBERS("Membres Inactifs", "index-3.html"),
    THIS_YEAR_MEMBERS("Membres affiliés en %YEAR%", "index-4.html"),
    NEXT_YEAR_MEMBERS("Membres affiliés en %YEAR+1%", "index-5.html"),
    UNPAID_MEMBERS("Membres non-affiliés", "index-6.html"),
    COMITY_MEMBERS("Membres du comité", "index-7.html");

    private final String label;
    @Getter
    private final String filename;

    ListType(String label, String filename) {
        this.label = label;
        this.filename = filename;
    }

    public String getLabel() {
        int year = LocalDate.now().getYear();
        String result = label.replace("%YEAR%", "" + year);
        result = result.replace("%YEAR+1%", "" + (year + 1));
        return result;
    }
}
