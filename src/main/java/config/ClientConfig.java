package config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClientConfig {

    private String name;
    private String extension;
    private String password;
    private String address;
    private String port;
}
