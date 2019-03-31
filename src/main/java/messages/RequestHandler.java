package messages;

import authorization.AccountManagerImpl;
import gov.nist.javax.sip.SipStackExt;
import gov.nist.javax.sip.clientauthutils.AuthenticationHelper;
import gov.nist.javax.sip.header.CSeq;
import lombok.AllArgsConstructor;

import javax.sip.*;
import javax.sip.header.*;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static javax.sip.message.Request.*;

@AllArgsConstructor
public class RequestHandler {

    private RequestHandlerHelper requestHandlerHelper;

    public Request sendInitialInvite() throws ParseException, InvalidArgumentException, SipException {
        FromHeader fromHeader = requestHandlerHelper.createFromHeader();
        ToHeader toHeader = requestHandlerHelper.createToHeader();

        ViaHeader viaHeader = requestHandlerHelper.createViaHeader();
        viaHeader.setRPort();

        List<ViaHeader> viaHeaderList = new ArrayList<>();
        viaHeaderList.add(viaHeader);

        MaxForwardsHeader maxForwardsHeader = requestHandlerHelper.getHeaderFactory().createMaxForwardsHeader(70);

        CallIdHeader callIdHeader = requestHandlerHelper.getSipProvider().getNewCallId();

        CSeqHeader cSeqHeader = requestHandlerHelper.getHeaderFactory().createCSeqHeader(1L, INVITE);

        ContentTypeHeader contentTypeHeader = requestHandlerHelper.getHeaderFactory().createContentTypeHeader("application", "sdp");

        Request request = requestHandlerHelper.getMessageFactory()
                .createRequest(
                        toHeader.getAddress().getURI(),
                        INVITE,
                        callIdHeader,
                        cSeqHeader,
                        fromHeader,
                        toHeader,
                        viaHeaderList,
                        maxForwardsHeader
                );


        ContactHeader contactHeader = requestHandlerHelper.createContactHeader();

        request.addHeader(contactHeader);

        //TODO: make it method.
        String sdp ="v=0\n" +
                "o=Z 0 0 IN IP4 192.168.1.100\n" +
                "s=Z\n" +
                "c=IN IP4 192.168.1.100\n" +
                "t=0 0\n" +
                "m=audio 8000 RTP/AVP 106 9 3 111 0 8 97 110 112 98 101 100 99 102\n" +
                "a=rtpmap:106 opus/48000/2\n" +
                "a=fmtp:106 minptime=20; cbr=1; maxaveragebitrate=40000; useinbandfec=1\n" +
                "a=rtpmap:111 speex/16000\n" +
                "a=rtpmap:97 iLBC/8000\n" +
                "a=fmtp:97 mode=20\n" +
                "a=rtpmap:110 speex/8000\n" +
                "a=rtpmap:112 speex/32000\n" +
                "a=rtpmap:98 telephone-event/48000\n" +
                "a=fmtp:98 0-16\n" +
                "a=rtpmap:101 telephone-event/8000\n" +
                "a=fmtp:101 0-16\n" +
                "a=rtpmap:100 telephone-event/16000\n" +
                "a=fmtp:100 0-16\n" +
                "a=rtpmap:99 telephone-event/32000\n" +
                "a=fmtp:99 0-16\n" +
                "a=rtpmap:102 G726-32/8000\n" +
                "a=sendrecv";

        request.setContent(sdp, contentTypeHeader);

        ClientTransaction clientTransaction = requestHandlerHelper.getSipProvider().getNewClientTransaction(request);
        clientTransaction.sendRequest();

        return request;
    }

    public Request sendInviteToAuthenticate(Response response, ClientTransaction clientTransaction) throws SipException {
        AuthenticationHelper authenticationHelper = ((SipStackExt) requestHandlerHelper.getSipProvider().getSipStack()).getAuthenticationHelper(
                new AccountManagerImpl(requestHandlerHelper.getSipConfiguration().getClientConfig()),
                SipFactory.getInstance().createHeaderFactory()
        );

        clientTransaction = authenticationHelper.handleChallenge(response, clientTransaction, requestHandlerHelper.getSipProvider(), 5);
        clientTransaction.sendRequest();

        return clientTransaction.getRequest();
    }

    public Request sendInviteToAuthenticate(ResponseEvent responseEvent) throws SipException {
        ClientTransaction clientTransaction = responseEvent.getClientTransaction();
        Response response = responseEvent.getResponse();

        return sendInviteToAuthenticate(response, clientTransaction);
    }

    public Request sendAck(ResponseEvent responseEvent, Dialog dialog) throws SipException, InvalidArgumentException {
        Request request = dialog.createAck(((CSeqHeader) responseEvent.getResponse().getHeader(CSeq.NAME)).getSeqNumber());
        request.addHeader(responseEvent.getClientTransaction().getRequest().getHeader("Contact"));
        dialog.sendAck(request);

        return request;
    }

    public Request sendInfo(String signalValue, String duration, Dialog dialog) throws SipException, ParseException, InterruptedException {
        Request infoRequest = dialog.createRequest(INFO);
        String content = String.format("Signal=%s\r\nDuration=%s", signalValue, duration);
        byte[] bytes = content.getBytes();

        infoRequest.setContent(bytes, requestHandlerHelper.getHeaderFactory()
                .createContentTypeHeader("application", "dtmf-relay"));
        infoRequest.addHeader(requestHandlerHelper.createContactHeader());

        ClientTransaction clientTransaction = requestHandlerHelper.getSipProvider()
                .getNewClientTransaction(infoRequest);
        dialog.sendRequest(clientTransaction);

        return clientTransaction.getRequest();
    }

}
