package sst.licences.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;

public class LicencesConfig {
    public LicencesConfig(DefaultValues defaultvalues, Club club, Application application, Directories directories, DataFiles datafiles, Export export, Variables variables) {
        this.defaultvalues = defaultvalues;
        this.club = club;
        this.application = application;
        this.directories = directories;
        this.datafiles = datafiles;
        this.export = export;
        this.variables = variables;
    }

    public LicencesConfig() {
    }

    @Getter
    private DefaultValues defaultvalues;
    @Getter
    private Club club;
    @Getter
    private Application application;
    @Getter
    private Directories directories;
    @Getter
    private DataFiles datafiles;
    @Getter
    private Export export;
    @Getter
    private Variables variables;
    @Getter
    private Report report;

    @Override
    public String toString() {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        try {
            return om.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "ERROR:" + e.getMessage();
        }
    }
}
