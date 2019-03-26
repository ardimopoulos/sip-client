package session;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;

@Data
@NoArgsConstructor
public class SipSession {

    private ClientTransaction ctx;
    private String dialogId;
    private Dialog dialog;
}
