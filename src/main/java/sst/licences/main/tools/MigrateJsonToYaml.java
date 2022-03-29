package sst.licences.main.tools;

import sst.licences.container.LicencesContainer;

public class MigrateJsonToYaml {
    public static void main(String[] args) {
        LicencesContainer.load();

        LicencesContainer.me().saveYAML();
    }
}
