
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

@Path("artikal")
public class Artikal {
    
    @Resource(lookup = "jms/__defaultConnectionFactory")
    public ConnectionFactory connectionFactory;
    
    @Resource(lookup = "requestTopic")
    public Topic requestTopic;
    
    @Resource(lookup = "responseTopic")
    public Topic responseTopic;
    
    public static JMSContext context;
    
    @POST
    @Path("kreirajArtikal")
    public Response kreirajArtikal(String zahtev){
        
        // ako context nije napravljen, pravimo ga
        if(context==null) context = connectionFactory.createContext();
        
        // kreiramo poruku koju saljemo podsistemu
        ObjectMessage objMsg = context.createObjectMessage(zahtev);
        try {
            objMsg.setIntProperty("rbrZahtev", 6); //redni broj zahteva, da bi podsistem znao koji zahtev prihvata
        } catch (JMSException ex) {
            Logger.getLogger(Artikal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // kreiramo producer koji salje nasu poruku ka podsistemu
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        return Response.status(Response.Status.OK).build();  
    }
    
    @POST
    @Path("promeniCenuArtikla")
    public Response promeniCenuArtikla(String zahtev){
        
        // ako context nije napravljen, pravimo ga
        if(context==null) context = connectionFactory.createContext();
        
        // kreiramo poruku koju saljemo podsistemu
        ObjectMessage objMsg = context.createObjectMessage(zahtev);
        try {
            objMsg.setIntProperty("rbrZahtev", 7); //redni broj zahteva, da bi podsistem znao koji zahtev prihvata
        } catch (JMSException ex) {
            Logger.getLogger(Artikal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // kreiramo producer koji salje nasu poruku ka podsistemu
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        return Response.status(Response.Status.OK).build();  
    }
    
    @POST
    @Path("postaviPopustArtikla")
    public Response postaviPopustArtikla(String zahtev){
        
        // ako context nije napravljen, pravimo ga
        if(context==null) context = connectionFactory.createContext();
        
        // kreiramo poruku koju saljemo podsistemu
        ObjectMessage objMsg = context.createObjectMessage(zahtev);
        try {
            objMsg.setIntProperty("rbrZahtev", 8); //redni broj zahteva, da bi podsistem znao koji zahtev prihvata
        } catch (JMSException ex) {
            Logger.getLogger(Artikal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // kreiramo producer koji salje nasu poruku ka podsistemu
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        return Response.status(Response.Status.OK).build();  
    }
    
    @GET
    @Path("{idKorisnik}")
    public Response dohvatiArtikleZaKorisnika(@PathParam("idKorisnik") int idKorisnik) {
         // ako context nije napravljen, pravimo ga
          if(context==null) context = connectionFactory.createContext();
        System.out.println("resursi.Artikal.dohvatiArtikleZaKorisnika()  "  + idKorisnik);
        ObjectMessage objMsg=context.createObjectMessage(""+idKorisnik + "#");
        try {
            objMsg.setIntProperty("rbrZahtev", 15);
        } catch (JMSException ex) {
            Logger.getLogger(Artikal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        JMSConsumer consumer = context.createConsumer(responseTopic, "rbrZahtev=15", false);
        TextMessage txtMsg=(TextMessage) consumer.receive();
        
        try {
            String sadrzajMsg = txtMsg.getText();
            System.out.println("sadrzajMsg  " + sadrzajMsg);
            return Response.status(Response.Status.OK).entity(sadrzajMsg).build();
        } catch (JMSException ex) {
            Logger.getLogger(Artikal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.EXPECTATION_FAILED).build();
       
    }
    
    
    @POST
    @Path("dodajArtikalUKorpu")
    public Response dodajArtikalUKorpu(String zahtev) {

        // ako context nije napravljen, pravimo ga
        if (context == null) {
            context = connectionFactory.createContext();
        }

        // kreiramo poruku koju saljemo podsistemu
        ObjectMessage objMsg = context.createObjectMessage(zahtev);
        try {
            objMsg.setIntProperty("rbrZahtev", 9); //redni broj zahteva, da bi podsistem znao koji zahtev prihvata
        } catch (JMSException ex) {
            Logger.getLogger(Artikal.class.getName()).log(Level.SEVERE, null, ex);
        }

        // kreiramo producer koji salje nasu poruku ka podsistemu
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);

        return Response.status(Response.Status.OK).build();
    }
    
    @POST
    @Path("izbrisiArtikalIzKorpe")
    public Response izbrisiArtikalIzKorpe(String zahtev) {

        // ako context nije napravljen, pravimo ga
        if (context == null) {
            context = connectionFactory.createContext();
        }

        // kreiramo poruku koju saljemo podsistemu
        ObjectMessage objMsg = context.createObjectMessage(zahtev);
        try {
            objMsg.setIntProperty("rbrZahtev", 10); //redni broj zahteva, da bi podsistem znao koji zahtev prihvata
        } catch (JMSException ex) {
            Logger.getLogger(Artikal.class.getName()).log(Level.SEVERE, null, ex);
        }

        // kreiramo producer koji salje nasu poruku ka podsistemu
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);

        return Response.status(Response.Status.OK).build();
    }
    
    
     @GET
    @Path("korpa/{idKorisnik}")
    public Response dohvatiSadrzajKorpeZaKorisnika(@PathParam("idKorisnik") int idKorisnik) {
         // ako context nije napravljen, pravimo ga
          if(context==null) context = connectionFactory.createContext();
        System.out.println("resursi.Artikal.dohvatiSadrzajKorpeZaKorisnika()  "  + idKorisnik);
        ObjectMessage objMsg=context.createObjectMessage(""+idKorisnik + "#");
        try {
            objMsg.setIntProperty("rbrZahtev", 16);
        } catch (JMSException ex) {
            Logger.getLogger(Artikal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JMSProducer producer = context.createProducer();
        producer.send(requestTopic, objMsg);
        
        JMSConsumer consumer = context.createConsumer(responseTopic, "rbrZahtev=16", false);
        TextMessage txtMsg=(TextMessage) consumer.receive();
        
        try {
            String sadrzajMsg = txtMsg.getText();
            System.out.println("sadrzajMsg  " + sadrzajMsg);
            return Response.status(Response.Status.OK).entity(sadrzajMsg).build();
        } catch (JMSException ex) {
            Logger.getLogger(Artikal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.EXPECTATION_FAILED).build();
       
    }

    
    
    
    
}
