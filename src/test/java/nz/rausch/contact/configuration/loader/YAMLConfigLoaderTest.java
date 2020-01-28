package nz.rausch.contact.configuration.loader;

import nz.rausch.contact.configuration.exception.ConfigLoadException;
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

    private class TestConfigLoader extends YAMLConfigLoader<TestConfig> {
        public TestConfigLoader() {
            setConfigFile("src/test/resources/testconfig.yaml");
        }
    }

    public static class TestConfig {
        private String key;

        public TestConfig() {

        }

        public TestConfig(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}