package config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Properties;

import static utils.Utilities.loadProperties;

@Getter
@AllArgsConstructor
public class SipConfiguration {

    private ClientConfig clientConfig;
    private ServerConfig serverConfig;
    private String transport;

    public SipConfiguration(String propertiesFile) {
        try {
            Properties properties = loadProperties(propertiesFile);

            clientConfig = new ClientConfig(
                    properties.getProperty("client.name"),
                    properties.getProperty("client.extension"),
                    properties.getProperty("client.username"),
                    properties.getProperty("client.password"),
                    properties.getProperty("client.address"),
                    properties.getProperty("client.port")
            );

            serverConfig = new ServerConfig(
                    properties.getProperty("server.name"),
                    properties.getProperty("server.address"),
                    properties.getProperty("server.extension")
            );

            transport = properties.getProperty("transport");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
