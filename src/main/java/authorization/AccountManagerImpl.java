package authorization;

import config.ClientConfig;
import gov.nist.javax.sip.clientauthutils.AccountManager;
import gov.nist.javax.sip.clientauthutils.UserCredentials;

import javax.sip.ClientTransaction;

public class AccountManagerImpl implements AccountManager {

    private ClientConfig clientConfig;

    public AccountManagerImpl(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public UserCredentials getCredentials(ClientTransaction clientTransaction, String s) {
        return new UserCredentialsImpl(clientConfig.getUsername(), "nist.gov", clientConfig.getPassword());
    }
}
