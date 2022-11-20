package practica4interfaz;

import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.awt.*;
import javax.swing.UIManager;



public class TalkAgent extends GuiAgent{
    
    VentanaChat vchat;
    boolean packFrame = false;

    @Override
    protected void setup() {
        CyclicBehaviour cb = new TalkBehaviour(this);
        addBehaviour(cb);
        
        vchat = new VentanaChat(this);
       
        if(packFrame){
            vchat.pack();
        }else{
            vchat.validate();
        }
        /*
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = vchat.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        vchat.setLocation((screenSize.width - frameSize.width)/2,(screenSize.height - frameSize.height)/2);
        */
        vchat.setTitle(getLocalName());
        vchat.setVisible(true);
        
        
    }
    
    

    @Override
    protected void onGuiEvent(GuiEvent ge) {
        String receiverName = (String) ge.getParameter(0);
        String msgContent = (String) ge.getParameter(1);
        ACLMessage toSend = new ACLMessage(ACLMessage.INFORM);
        toSend.setContent(msgContent);
        toSend.setPerformative(ACLMessage.INFORM);
        toSend.addReceiver(new AID(receiverName, AID.ISLOCALNAME));
        send(toSend);
    } //End class TalkBehaviour
 
    
    class TalkBehaviour extends CyclicBehaviour {

        public TalkBehaviour(GuiAgent ga) {
            super(ga);
        }

        @Override
        public void action() {
            ACLMessage reply = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
            if(reply != null){
                String content = reply.getContent();
                String sender = reply.getSender().getName();
                vchat.taReceived.append("\n" + sender + ": "+ content);
            }else{
                block();
            }
        } // end action
    }// ende inner class
}
