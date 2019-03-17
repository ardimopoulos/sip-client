package utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class Utilities {

    public static Object getConfigurationAsObject(String file, Class className) {
        Object object = null;
        Yaml yaml = new Yaml();

        try {
            InputStream config = Utilities.class.getResourceAsStream("config.yml");
            object = yaml.loadAs(config, className);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }
}
