
package resursi;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("kategorija")
public class Kategorija {
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    public ConnectionFactory connectionFactory;
    
    @Resource(lookup = "requestTopic")
    public Topic requestTopic;
    
    @Resource(lookup = "responseTopic")
    public Topic responseTopic;
    
    public static JMSContext context;
    
    @POST
    public Response kreirajKategoriju(String zahtev){
        
        // ako context nije napravljen, pravimo ga
        if(context==null) context = connectionFactory.createContext();
        
        // kreiramo poruku koju saljemo podsistemu
        ObjectMessage objMsg = context.createObjectMessage(zahtev);
        try {
            objMsg.setIntProperty("rbrZahtev", 5); //redni broj zahteva, da bi podsistem znao koji zahtev prihvata
        } catch (JMSException ex) {
            Logger.getLogger(Kategorija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // kreiramo producer koji salje nasu poruku ka podsistemu
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        return Response.status(Response.Status.OK).build();  
    }
    
    @GET 
    public Response dohvatiKategorije(){
        
        if(context==null) context = connectionFactory.createContext();
        
        ObjectMessage objMsg=context.createObjectMessage();
        try {
            objMsg.setIntProperty("rbrZahtev", 14);
        } catch (JMSException ex) {
            Logger.getLogger(Kategorija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        JMSConsumer consumer = context.createConsumer(responseTopic, "rbrZahtev=14", false);
        TextMessage txtMsg=(TextMessage) consumer.receive();
        
        try {
            String sadrzajMsg = txtMsg.getText();
            return Response.status(Response.Status.OK).entity(sadrzajMsg).build();
        } catch (JMSException ex) {
            Logger.getLogger(Kategorija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.EXPECTATION_FAILED).build();
        
    }
    
}
