package au.telegraph.configuration.loader;

import au.telegraph.configuration.exception.ConfigLoadException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

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
            e.printStackTrace();
            throw new ConfigLoadException("Could not read file " + file.getAbsolutePath());
        }

        setConfiguration(configuration);
        return this;
    }
}
