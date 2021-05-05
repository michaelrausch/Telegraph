package au.telegraph.configuration;

import au.telegraph.configuration.models.AppConfig;
import au.telegraph.configuration.loader.YAMLConfigLoader;

public class AppConfigLoader extends YAMLConfigLoader<AppConfig> {
    public AppConfigLoader() {
        setConfigFile("config.yaml");
    }
}
