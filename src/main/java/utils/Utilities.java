package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.isNull;

public class Utilities {

    public static Properties loadProperties(String file) throws IOException {
        Properties properties = new Properties();

        try(InputStream inputStream = Utilities.class.getResourceAsStream("/" + file)) {
            properties.load(inputStream);
        }

        return properties;
    }

    public static String getSipAddress(String number, String ip, String port) {
        if (isNull(port)) {
            return "<sip:" + number + "@" + ip +">";
        }
        return "<sip:" + number + "@" + ip + ":" + port + ">";
    }
}
