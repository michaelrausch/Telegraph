package nz.rausch.contact.configuration.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import nz.rausch.contact.configuration.exception.ConfigLoadException;

import java.io.File;
import java.io.IOException;

public class YAMLConfigLoader<E> extends ConfigLoader<E> {
    @Override
    public ConfigLoader<E> load(Class<E> clazz) throws ConfigLoadException {
        E configuration;
        File file = new File(getConfigFile());
        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        try {
            configuration = om.readValue(file, clazz);
        } catch (IOException e) {
            throw new ConfigLoadException("Could not read file " + file.getAbsolutePath());
        }

        setConfiguration(configuration);
        return this;
    }
}
