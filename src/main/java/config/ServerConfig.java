package config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServerConfig {

    private String name;
    private String address;
    private String extension;
}
