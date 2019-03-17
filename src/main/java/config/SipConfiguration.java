package config;

import lombok.Getter;

import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.isNull;

@Getter
public class SipConfiguration {

    private static SipConfiguration instance = null;

    private SipClient client;
    private SipServer server;
    private String transport;

    private SipConfiguration() {
        Properties properties = new Properties();
        try (InputStream configFile = SipConfiguration.class.getResourceAsStream("/application.properties")) {
            properties.load(configFile);

            client = new SipClient(
                    properties.getProperty("client.name"),
                    properties.getProperty("client.number"),
                    properties.getProperty("client.password"),
                    properties.getProperty("client.address"),
                    properties.getProperty("client.port")
            );

            server = new SipServer(
                    properties.getProperty("server.name"),
                    properties.getProperty("server.address")
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
