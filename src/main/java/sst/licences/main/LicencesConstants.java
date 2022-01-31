package sst.licences.main;

import sst.licences.config.ConfigUtil;
import sst.licences.model.Country;

public class LicencesConstants {
    public static final String CHARSET_ANSI_CP_1252 = "Cp1252";

    private LicencesConstants() {
    }

    public static final Country DEFAULT_PAYS_CODE = ConfigUtil.me().defaultValuesCountry();
    public static final String DEFAULT_LANGUE = ConfigUtil.me().defaultValuesLanguage();
    public static final String BERGER_CLUB_ARLONAIS = ConfigUtil.me().clubName();
    public static final String APPLICATION_TITLE = ConfigUtil.me().applicationName();
    public static final String ENV_TEST_MODE = ConfigUtil.me().variablesTestMode();
    public static final String ENV_MAIL_PWD = ConfigUtil.me().variablesMailPwd();

    public static final String WORKING_FOLDER = ConfigUtil.me().directoriesWorking();
    public static final String MEMBRES_JSON_FILE = ConfigUtil.me().dataFilesMembers();
    public static final String COMITE_JSON_FILE = ConfigUtil.me().dataFilesComite();
    public static final String CONFIG_YAML_FILE = WORKING_FOLDER + "config.yaml";
    public static final String MEMBRES_YAML_FILE = WORKING_FOLDER + "membres.yaml";
    public static final String PAYMENTS_YAML_FILE = WORKING_FOLDER + "payments.yaml";
    public static final String COMITE_YAML_FILE = WORKING_FOLDER + "comite.yaml";

    public static final String REPORT_FOLDER = ConfigUtil.me().directoriesReport();

    public static final String NEW_MEMBRES_XLSX = ConfigUtil.me().exportNewMembers();
    public static final String ALL_MEMBRES_XLSX = ConfigUtil.me().exportAllMembers();
    public static final String AFFILIATE_MEMBRES_XLSX = ConfigUtil.me().exportAffiliate();
    public static final String NON_AFFILIATE_MEMBRES_XLSX = ConfigUtil.me().exportNonAffiliate();
}