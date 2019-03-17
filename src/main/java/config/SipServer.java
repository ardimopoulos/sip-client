package config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SipServer {

    private String name;
    private String address;
}
