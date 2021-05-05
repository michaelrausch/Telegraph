package au.telegraph.configuration.loader;

import au.telegraph.configuration.exception.ConfigLoadException;

public abstract class ConfigLoader<E> {
    private E configuration;
    private String configFile;

    public E getConfig() throws ConfigLoadException {
        if (configuration == null) throw new ConfigLoadException("Configuration accessed before it has been loaded");
        return configuration;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    protected String getConfigFile() {
        return configFile;
    }

    public abstract ConfigLoader<E> load(Class<E> clazz) throws ConfigLoadException;

    protected void setConfiguration(E configuration) {
        this.configuration = configuration;
    }
}


