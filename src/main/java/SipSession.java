import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ServerTransaction;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class SipSession {
    private Request request;
    private Response response;
    private Dialog dialog;
    private ClientTransaction ctx;
    private ServerTransaction stx;
}
