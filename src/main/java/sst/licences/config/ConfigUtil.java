package sst.licences.config;

import sst.licences.config.loader.LicencesConfigLoader;
import sst.licences.model.Country;
import sst.licences.model.CountryList;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ConfigUtil {
    private static final ConfigUtil me = new ConfigUtil();

    public static ConfigUtil me() {
        return me;
    }

    private LicencesConfig config;

    private ConfigUtil() {
        String configFile = System.getenv("LicencesConfigFile");
        try {
            config = LicencesConfigLoader.load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /*
     * Default Values
     */
    public Country defaultValuesCountry() {
        Optional<Country> optionalCountry = CountryList.country(config.getDefaultvalues().getCountry());
        return optionalCountry.orElse(CountryList.belgium);
    }

    public String defaultValuesLanguage() {
        return config.getDefaultvalues().getLanguage();
    }

    /*
     * Club
     */
    public String clubName() {
        return config.getClub().getName();
    }

    /*
     * Application
     */
    public String applicationName() {
        return config.getApplication().getName();
    }

    /*
     * Directories
     */
    private String directories(String directory) {
        return (directory.endsWith(File.separator)) ? directory : directory + File.separator;
    }

    public String directoriesWorking() {
        return directories(config.getDirectories().getWorking());
    }

    public String directoriesExport() {
        return directories(config.getDirectories().getExport());
    }

    public String directoriesReport() {
        return directories(config.getDirectories().getReport());
    }

    public String directoriesDownload() {
        return directories(config.getDirectories().getDownload());
    }

    /*
     * Data Files
     */
    public String dataFilesMembers() {
        return directoriesWorking() + config.getDatafiles().getMembers();
    }

    public String dataFilesComite() {
        return directoriesWorking() + config.getDatafiles().getComite();
    }

    /*
     * Export
     */
    public String exportNewMembers() {
        return directoriesExport() + config.getExport().getNewmembers();
    }

    public String exportAllMembers() {
        return directoriesExport() + config.getExport().getAllmembers();
    }

    public String exportAffiliate() {
        return directoriesExport() + config.getExport().getAffiliate();
    }

    public String exportNonAffiliate() {
        return directoriesExport() + config.getExport().getNonaffiliate();
    }

    /*
     * Variables
     */
    public String variablesTestMode() {
        return config.getVariables().getTestmode();
    }

    public String variablesMailPwd() {
        return config.getVariables().getMailpwd();
    }

    /*
     * Report
     */
    public String reportPayments() {
        return directoriesReport() + config.getReport().getPayments();
    }
}