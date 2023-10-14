
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
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;


@Path("transakcija")
public class Transakcija {
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    public ConnectionFactory connectionFactory;
    
    @Resource(lookup = "requestTopic")
    public Topic requestTopic;
    
    @Resource(lookup = "responseTopic")
    public Topic responseTopic;
    
    public static JMSContext context;
    
    @POST
    @Path("placanje")
    public Response placanje(String zahtev){
        
        // ako context nije napravljen, pravimo ga
        if(context==null) context = connectionFactory.createContext();
        
        // kreiramo poruku koju saljemo podsistemu
        ObjectMessage objMsg = context.createObjectMessage(zahtev);
        try {
            objMsg.setIntProperty("rbrZahtev", 11); //redni broj zahteva, da bi podsistem znao koji zahtev prihvata
        } catch (JMSException ex) {
            Logger.getLogger(Transakcija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // kreiramo producer koji salje nasu poruku ka podsistemu
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        return Response.status(Response.Status.OK).build();  
    }
    
    
    @GET 
    @Path("novac/{idKorisnik}")
    public Response novac(@PathParam("idKorisnik") int idKorisnik){
        
        if(context==null) context = connectionFactory.createContext();
        
         ObjectMessage objMsg=context.createObjectMessage(""+idKorisnik + "#");
        try {
            objMsg.setIntProperty("rbrZahtev", 30);
        } catch (JMSException ex) {
            Logger.getLogger(Transakcija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        JMSConsumer consumer = context.createConsumer(responseTopic, "rbrZahtev=30", false);
        TextMessage txtMsg=(TextMessage) consumer.receive();
        
        try {
            String sadrzajMsg = txtMsg.getText();
            return Response.status(Response.Status.OK).entity(sadrzajMsg).build();
        } catch (JMSException ex) {
            Logger.getLogger(Transakcija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.EXPECTATION_FAILED).build();
        
    }
    
    
    @GET 
    @Path("sveNarudzbine")
    public Response sveNarudzbine(){
        
        if(context==null) context = connectionFactory.createContext();
        
        ObjectMessage objMsg=context.createObjectMessage();
        try {
            objMsg.setIntProperty("rbrZahtev", 18);
        } catch (JMSException ex) {
            Logger.getLogger(Korisnik.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        JMSConsumer consumer = context.createConsumer(responseTopic, "rbrZahtev=18", false);
        TextMessage txtMsg=(TextMessage) consumer.receive();
        
        try {
            String sadrzajMsg = txtMsg.getText();
            return Response.status(Response.Status.OK).entity(sadrzajMsg).build();
        } catch (JMSException ex) {
            Logger.getLogger(Transakcija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.EXPECTATION_FAILED).build();
        
    }
    
    @GET 
    @Path("sveTransakcije")
    public Response sveTransakcije(){
        
        if(context==null) context = connectionFactory.createContext();
        
        ObjectMessage objMsg=context.createObjectMessage();
        try {
            objMsg.setIntProperty("rbrZahtev", 19);
        } catch (JMSException ex) {
            Logger.getLogger(Korisnik.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        JMSConsumer consumer = context.createConsumer(responseTopic, "rbrZahtev=19", false);
        TextMessage txtMsg=(TextMessage) consumer.receive();
        
        try {
            String sadrzajMsg = txtMsg.getText();
            return Response.status(Response.Status.OK).entity(sadrzajMsg).build();
        } catch (JMSException ex) {
            Logger.getLogger(Transakcija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.EXPECTATION_FAILED).build();
        
    }
    
    
    
    
    @GET 
    @Path("dohvatiNarudzbineZaKorisnika/{idKorisnik}")
    public Response dohvatiNarudzbineZaKorisnika(@PathParam("idKorisnik") int idKorisnik){
        
        if(context==null) context = connectionFactory.createContext();
        
         ObjectMessage objMsg=context.createObjectMessage(""+idKorisnik + "#");
        try {
            objMsg.setIntProperty("rbrZahtev", 17);
        } catch (JMSException ex) {
            Logger.getLogger(Transakcija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        JMSConsumer consumer = context.createConsumer(responseTopic, "rbrZahtev=17", false);
        TextMessage txtMsg=(TextMessage) consumer.receive();
        
        try {
            String sadrzajMsg = txtMsg.getText();
            return Response.status(Response.Status.OK).entity(sadrzajMsg).build();
        } catch (JMSException ex) {
            Logger.getLogger(Transakcija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.EXPECTATION_FAILED).build();
        
    }
    
  
    
}
