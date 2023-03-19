package sst.licences.main.tools.migrate;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

@Log4j2
public class Migration {
    public static void main(String[] args) {
        LicencesContainer.load();

        for (Membre m : LicencesContainer.me().membres()) {

        }

        LicencesContainer.me().saveJSON();
    }
}
