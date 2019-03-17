package utils;

import static java.util.Objects.isNull;

public class Utilities {
    public static String getSipAddress(String number, String ip, String port) {
        if (isNull(port)) {
            return "<sip:" + number + "@" + ip +">";
        }
        return "<sip:" + number + "@" + ip + ":" + port + ">";
    }
}
