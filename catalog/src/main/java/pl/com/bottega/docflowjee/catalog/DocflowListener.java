package pl.com.bottega.docflowjee.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "topic/DocflowEvents"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class DocflowListener implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(DocflowListener.class.toString());
    private static final String EVENT_CLASS = "EVENT_CLASS";

    @Inject
    private ObjectMapper objectMapper;

    public void onMessage(Message rcvMessage) {

    }

}
