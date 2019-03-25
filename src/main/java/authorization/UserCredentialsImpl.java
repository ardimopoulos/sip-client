package authorization;

import gov.nist.javax.sip.clientauthutils.UserCredentials;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCredentialsImpl implements UserCredentials {

    private String userName;
    private String sipDomain;
    private String password;
}
