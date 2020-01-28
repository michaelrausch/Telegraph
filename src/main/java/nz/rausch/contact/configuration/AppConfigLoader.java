package nz.rausch.contact.configuration;

import nz.rausch.contact.configuration.loader.YAMLConfigLoader;
import nz.rausch.contact.configuration.models.AppConfig;

public class AppConfigLoader extends YAMLConfigLoader<AppConfig> {
    public AppConfigLoader() {
        setConfigFile("config.yaml");
    }
}
