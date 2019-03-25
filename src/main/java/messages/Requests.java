package messages;

import config.ClientConfig;
import config.SipConfiguration;
import lombok.AllArgsConstructor;
import utils.Utilities;

import javax.sip.InvalidArgumentException;
import javax.sip.address.Address;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static javax.sip.message.Request.*;

@AllArgsConstructor
public class Requests {

    private RequestHelper requestHelper;

    public Request createInitialInvite() throws ParseException, InvalidArgumentException {
        FromHeader fromHeader = requestHelper.createFromHeader();
        ToHeader toHeader = requestHelper.createToHeader();

        ViaHeader viaHeader = requestHelper.createViaHeader();
        viaHeader.setRPort();

        List<ViaHeader> viaHeaderList = new ArrayList<>();
        viaHeaderList.add(viaHeader);

        MaxForwardsHeader maxForwardsHeader = requestHelper.getHeaderFactory().createMaxForwardsHeader(70);

        CallIdHeader callIdHeader = requestHelper.getSipProvider().getNewCallId();

        CSeqHeader cSeqHeader = requestHelper.getHeaderFactory().createCSeqHeader(1L, INVITE);

        ContentTypeHeader contentTypeHeader = requestHelper.getHeaderFactory().createContentTypeHeader("application", "sdp");

        Request request = requestHelper.getMessageFactory()
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


        String contact = Utilities.getSipAddress(
                requestHelper.getSipConfiguration().getClient().getExtension(),
                requestHelper.getSipConfiguration().getClient().getAddress(),
                requestHelper.getSipConfiguration().getClient().getPort()
        );

        Address contactAddress = requestHelper.getAddressFactory().createAddress(contact);
        ContactHeader contactHeader = requestHelper.getHeaderFactory().createContactHeader(contactAddress);

        request.addHeader(contactHeader);

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

        return request;
    }


}
