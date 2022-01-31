package sst.licences.config;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Variables {
    public Variables(String testmode, String mailpwd) {
        this.testmode = testmode;
        this.mailpwd = mailpwd;
    }

    public Variables() {
    }

    @Getter
    private String testmode;
    @Getter
    private String mailpwd;
}
