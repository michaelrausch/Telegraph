package au.telegraph.configuration.loader;

import au.telegraph.configuration.exception.ConfigLoadException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YAMLConfigLoaderTest {
    private ConfigLoader<TestConfig> loader;

    @Before
    public void setup() {
        loader = new TestConfigLoader();
    }

    @Test
    public void checkLoaderLoadsConfigurationFromYaml() throws Exception {
        String expectedValue = "value";
        loader.load(TestConfig.class);

        String value = loader.getConfig().getKey();
        assertEquals(expectedValue, value);
    }

    @Test(expected = ConfigLoadException.class)
    public void checkLoaderThrowsExceptionWhenConfigFileDoesntExist() throws ConfigLoadException {
        loader.setConfigFile("idonotexist");
        loader.load(TestConfig.class);
    }

    @Test(expected = ConfigLoadException.class)
    public void checkLoaderThrowsExceptionWhenConfigAccessedButNotLoaded() throws ConfigLoadException {
        loader.getConfig();
    }

}