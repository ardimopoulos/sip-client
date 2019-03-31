package messages;

import config.ClientConfig;
import config.SipConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import utils.Utilities;

import javax.sdp.SdpFactory;
import javax.sip.InvalidArgumentException;
import javax.sip.SipProvider;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import java.text.ParseException;
import java.util.Random;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class RequestHandlerHelper {

    private SipConfiguration sipConfiguration;
    private AddressFactory addressFactory;
    private MessageFactory messageFactory;
    private HeaderFactory headerFactory;
    private SipProvider sipProvider;
    private SdpFactory sdpFactory;

    public FromHeader createFromHeader(String extension, String address, String name) throws ParseException {
        SipURI fromURI = addressFactory.createSipURI(extension, address);
        Address fromAddress = addressFactory.createAddress(fromURI);
        fromAddress.setDisplayName(name);

        return headerFactory.createFromHeader(fromAddress, String.valueOf(getTag()));
    }

    public FromHeader createFromHeader(String extension, String address) throws ParseException {
        return createFromHeader(extension, address, extension);
    }

    public FromHeader createFromHeader() throws ParseException {
        return createFromHeader(sipConfiguration.getClient().getExtension(),
                sipConfiguration.getClient().getAddress(),
                sipConfiguration.getClient().getName()
        );
    }

    public ToHeader createToHeader(String extension, String address, String name) throws ParseException {
        SipURI toURI = addressFactory.createSipURI(extension, address);
        Address toAddress = addressFactory.createAddress(toURI);
        toAddress.setDisplayName(name);

        return headerFactory.createToHeader(toAddress, null);
    }

    public ToHeader createToHeader(String extension, String address) throws ParseException {
        return createToHeader(extension, address, extension);
    }

    public ToHeader createToHeader() throws ParseException {
        return createToHeader(sipConfiguration.getServer().getExtension(),
                sipConfiguration.getServer().getAddress(),
                sipConfiguration.getServer().getName()
        );
    }

    public ViaHeader createViaHeader(String address, int port, String transport) throws ParseException, InvalidArgumentException {
        return headerFactory.createViaHeader(address, port, transport, null);
    }

    public ViaHeader createViaHeader() throws ParseException, InvalidArgumentException {
        return createViaHeader(
                sipConfiguration.getClient().getAddress(),
                Integer.parseInt(sipConfiguration.getClient().getPort()),
                sipConfiguration.getTransport()
        );
    }

    public ContactHeader createContactHeader() throws ParseException {
        return createContactHeader(sipConfiguration);
    }

    public ContactHeader createContactHeader(SipConfiguration sipConfiguration) throws ParseException {
        return createContactHeader(sipConfiguration.getClient());
    }

    public ContactHeader createContactHeader(ClientConfig clientConfig) throws ParseException {
        return createContactHeader(clientConfig.getExtension(), clientConfig.getAddress(), clientConfig.getPort());
    }

    public ContactHeader createContactHeader(String extension, String address, String port) throws ParseException {
        String contact = Utilities.getSipAddress(extension, address, port);

        return headerFactory.createContactHeader(addressFactory.createAddress(contact));
    }

    public static int getTag() {
        return new Random().nextInt();
    }
}
