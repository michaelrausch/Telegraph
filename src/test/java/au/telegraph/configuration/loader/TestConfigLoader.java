package au.telegraph.configuration.loader;

public class TestConfigLoader extends YAMLConfigLoader<TestConfig> {
    public TestConfigLoader() {
        setConfigFile("src/test/resources/testconfig.yaml");
    }
}