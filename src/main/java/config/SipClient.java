package config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(chain = true)
@Getter
public class SipClient {

    private String name;
    private String number;
    private String password;
    private String address;
    private String port;
}
