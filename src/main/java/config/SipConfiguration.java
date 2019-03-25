package config;

import lombok.Getter;

import java.util.Properties;

import static java.util.Objects.isNull;
import static utils.Utilities.loadProperties;

@Getter
public class SipConfiguration {

    private static SipConfiguration instance = null;

    private ClientConfig client;
    private ServerConfig server;
    private String transport;

    private SipConfiguration() {
        try {
            Properties properties = loadProperties("application.properties");

            client = new ClientConfig(
                    properties.getProperty("client.name"),
                    properties.getProperty("client.extension"),
                    properties.getProperty("client.password"),
                    properties.getProperty("client.address"),
                    properties.getProperty("client.port")
            );

            server = new ServerConfig(
                    properties.getProperty("server.name"),
                    properties.getProperty("server.address"),
                    properties.getProperty("server.extension")
            );

            transport = properties.getProperty("transport");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static SipConfiguration getInstance() {
        if (isNull(instance)) {
            instance = new SipConfiguration();
        }
        return instance;
    }
}
