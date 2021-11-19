package sst.licences.main.tools;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;

@Log4j2
public class MigrateUTF8 {

    public static void main(String[] args) {
        LicencesContainer.load();

        LicencesContainer.me().save();
    }
}
