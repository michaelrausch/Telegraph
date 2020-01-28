package nz.rausch.contact.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class ConfigLoader {
    private AppConfig appConfig;
    private final String CONFIG_FILE = "config.yaml";

    private ConfigLoader() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File("config.yaml");

        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        appConfig = om.readValue(file, AppConfig.class);
    }

    public static ConfigLoader getInstance() throws IOException {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }
}
