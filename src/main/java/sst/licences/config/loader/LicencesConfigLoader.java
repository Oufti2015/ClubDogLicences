package sst.licences.config.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import sst.licences.config.ConfigUtil;
import sst.licences.config.LicencesConfig;

import java.io.File;
import java.io.IOException;

public class LicencesConfigLoader {
    public static LicencesConfig load(String configFile) throws IOException {
        // Loading the YAML file from the /resources folder
        File file = new File(configFile);

        // Instantiating a new ObjectMapper as a YAMLFactory
        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        // Mapping the LicencesConfig from the YAML file to the Employee class
        return om.readValue(file, LicencesConfig.class);
    }

    public static void main(String[] args) {
        try {
            // Printing out the information
            System.out.println(LicencesConfigLoader.load("C:\\Users\\steph\\OneDrive\\Documents\\bca\\membres\\ClubDogLicences.yaml"));
            System.out.println("exportNewMembers=" + ConfigUtil.me().exportNewMembers());
            System.out.println("exportNewMembers=" + ConfigUtil.me().reportPayments());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
