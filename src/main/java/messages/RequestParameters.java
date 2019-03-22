package messages;

import config.SipConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.sdp.SdpFactory;
import javax.sip.SipProvider;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class RequestParameters {

    private SipConfiguration sipConfiguration;
    private AddressFactory addressFactory;
    private MessageFactory messageFactory;
    private HeaderFactory headerFactory;
    private SipProvider sipProvider;
    private SdpFactory sdpFactory;
}
