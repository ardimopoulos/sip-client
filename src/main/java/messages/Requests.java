package messages;

import lombok.AllArgsConstructor;
import utils.Utilities;

import javax.sip.InvalidArgumentException;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javax.sip.message.Request.*;

@AllArgsConstructor
public class Requests {

    private RequestParameters requestParameters;

    public Request createInitialInvite() throws ParseException, InvalidArgumentException {
        String fromName = requestParameters.getSipConfiguration().getClient().getName();
        SipURI fromURI = requestParameters.getAddressFactory().createSipURI(fromName, requestParameters.getSipConfiguration().getClient().getAddress());
        Address fromAddress = requestParameters.getAddressFactory().createAddress(fromURI);
        fromAddress.setDisplayName(fromName);
        FromHeader fromHeader = requestParameters.getHeaderFactory().createFromHeader(fromAddress, String.valueOf(getTag()));

        String toName = requestParameters.getSipConfiguration().getServer().getName();
        SipURI toURI = requestParameters.getAddressFactory().createSipURI(toName, requestParameters.getSipConfiguration().getServer().getAddress());
        Address toAddress = requestParameters.getAddressFactory().createAddress(toURI);
        toAddress.setDisplayName(toName);
        ToHeader toHeader = requestParameters.getHeaderFactory().createToHeader(toAddress, null);

        ViaHeader viaHeader = requestParameters.getHeaderFactory().createViaHeader(requestParameters.getSipConfiguration().getClient().getAddress(),
                Integer.parseInt(requestParameters.getSipConfiguration().getClient().getPort()), requestParameters.getSipConfiguration().getTransport(), null);
        viaHeader.setRPort();

        List<ViaHeader> viaHeaderList = new ArrayList<>();
        viaHeaderList.add(viaHeader);

        MaxForwardsHeader maxForwardsHeader = requestParameters.getHeaderFactory().createMaxForwardsHeader(70);

        CallIdHeader callIdHeader = requestParameters.getSipProvider().getNewCallId();

        CSeqHeader cSeqHeader = requestParameters.getHeaderFactory().createCSeqHeader(1L, INVITE);

        ContentTypeHeader contentTypeHeader = requestParameters.getHeaderFactory().createContentTypeHeader("application", "sdp");

        AllowHeader allowHeaderInvite = requestParameters.getHeaderFactory().createAllowHeader(INVITE);
        AllowHeader allowHeaderInfo = requestParameters.getHeaderFactory().createAllowHeader(INFO);
        AllowHeader allowHeaderAck = requestParameters.getHeaderFactory().createAllowHeader(ACK);
        AllowHeader allowHeaderBye = requestParameters.getHeaderFactory().createAllowHeader(BYE);

        Request request = requestParameters.getMessageFactory().createRequest(toURI, INVITE, callIdHeader, cSeqHeader,
                fromHeader, toHeader, viaHeaderList, maxForwardsHeader);

        String contact = Utilities.getSipAddress(
                requestParameters.getSipConfiguration().getClient().getNumber(),
                requestParameters.getSipConfiguration().getClient().getAddress(),
                requestParameters.getSipConfiguration().getClient().getPort()
        );

        Address contactAddress = requestParameters.getAddressFactory().createAddress(contact);
        ContactHeader contactHeader = requestParameters.getHeaderFactory().createContactHeader(contactAddress);

        request.addHeader(contactHeader);
        request.addHeader(allowHeaderAck);
        request.addHeader(allowHeaderBye);
        request.addHeader(allowHeaderInfo);
        request.addHeader(allowHeaderInvite);

        request.setContent("", contentTypeHeader);

        return request;
    }

    private int getTag() {
        return new Random().nextInt();
    }
}
