import config.SipConfiguration;
import lombok.Getter;
import messages.RequestHelper;
import messages.Requests;
import utils.Utilities;

import javax.sdp.SdpFactory;
import javax.sip.*;
import javax.sip.message.Request;
import java.util.Properties;

public class SipClient {

    @Getter
    private SipProvider sipProvider;

    @Getter
    private Requests requests;

    @Getter
    private RequestHelper requestHelper;

    private SipConfiguration sipConfiguration;

    public SipClient(SipConfiguration sipConfiguration) {
        this.sipConfiguration = sipConfiguration;
    }

    private void init() throws Exception {
        Properties stackProperties = Utilities.loadProperties("stack.properties");
        SipFactory sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");

        SipStack sipStack = sipFactory.createSipStack(stackProperties);

        ListeningPoint listeningPoint = sipStack.createListeningPoint(sipConfiguration.getClient().getAddress(),
                Integer.parseInt(sipConfiguration.getClient().getPort()), sipConfiguration.getTransport());

        sipProvider = sipStack.createSipProvider(listeningPoint);

        requestHelper = new RequestHelper()
                .setAddressFactory(sipFactory.createAddressFactory())
                .setMessageFactory(sipFactory.createMessageFactory())
                .setHeaderFactory(sipFactory.createHeaderFactory())
                .setSdpFactory(SdpFactory.getInstance())
                .setSipProvider(sipProvider)
                .setSipConfiguration(sipConfiguration);

        requests = new Requests(requestHelper);

        SipListener sipListener = new SipListenerImpl(this);
        sipProvider.addSipListener(sipListener);
    }

    public void sendRequest(Request request) throws SipException {
        ClientTransaction clientTransaction = sipProvider.getNewClientTransaction(request);
        clientTransaction.sendRequest();
    }

    public void start() {
        try {
            init();
            sendRequest(requests.createInitialInvite());
            System.out.println(requests.createInitialInvite());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
