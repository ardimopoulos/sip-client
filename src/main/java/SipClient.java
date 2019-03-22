import config.SipConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import messages.RequestParameters;
import messages.Requests;
import utils.Utilities;

import javax.sdp.SdpFactory;
import javax.sip.*;
import javax.sip.message.Request;
import java.util.Properties;

@Slf4j
public class SipClient {

    @Getter
    private SipProvider sipProvider;

    @Getter
    private Requests requests;

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

        RequestParameters requestParameters = new RequestParameters()
                .setAddressFactory(sipFactory.createAddressFactory())
                .setMessageFactory(sipFactory.createMessageFactory())
                .setHeaderFactory(sipFactory.createHeaderFactory())
                .setSdpFactory(SdpFactory.getInstance())
                .setSipProvider(sipProvider)
                .setSipConfiguration(sipConfiguration);



        requests = new Requests(requestParameters);

        SipListener sipListener = new SipListenerImpl(this);
    }

    public void sendRequest(Request request, ResponseEvent responseEvent, SipProvider sipProvider) throws SipException {
        ClientTransaction clientTransaction = sipProvider.getNewClientTransaction(request);
        clientTransaction.sendRequest();
    }

    private void sendInitialRequest(Request request, SipProvider sipProvider) throws SipException {
        sendRequest(request, null, sipProvider);
    }

    public void start() {
        try {
            init();
            sendInitialRequest(requests.createInitialInvite(), sipProvider);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
