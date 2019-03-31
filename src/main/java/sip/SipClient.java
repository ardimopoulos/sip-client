package sip;

import config.SipConfiguration;
import lombok.Getter;
import messages.RequestHandlerHelper;
import messages.RequestHandler;
import session.SipSession;
import utils.Utilities;

import javax.sdp.SdpFactory;
import javax.sip.*;
import javax.sip.message.Request;
import java.util.Properties;

public class SipClient {

    @Getter
    private SipProvider sipProvider;

    @Getter
    private RequestHandler requestHandler;

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

        RequestHandlerHelper requestHandlerHelper = new RequestHandlerHelper()
                .setAddressFactory(sipFactory.createAddressFactory())
                .setMessageFactory(sipFactory.createMessageFactory())
                .setHeaderFactory(sipFactory.createHeaderFactory())
                .setSdpFactory(SdpFactory.getInstance())
                .setSipProvider(sipProvider)
                .setSipConfiguration(sipConfiguration);

        requestHandler = new RequestHandler(requestHandlerHelper);

        SipListener sipListener = new SipListenerImpl(requestHandler);
        sipProvider.addSipListener(sipListener);
    }

    public SipSession updateSession(ClientTransaction clientTransaction) {
        Dialog dialog = clientTransaction.getDialog();
        return new SipSession(clientTransaction, dialog.getDialogId(), dialog);
    }

    public void start() {
        try {
            init();
            Request request = requestHandler.sendInitialInvite();
            System.out.println(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
