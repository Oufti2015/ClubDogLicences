package sst.licences.config;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Export {
    public Export(String newmembers, String allmembers, String affiliate, String nonaffiliate) {
        this.newmembers = newmembers;
        this.allmembers = allmembers;
        this.affiliate = affiliate;
        this.nonaffiliate = nonaffiliate;
    }

    public Export() {
    }

    @Getter
    private String newmembers;
    @Getter
    private String allmembers;
    @Getter
    private String affiliate;
    @Getter
    private String nonaffiliate;

}
