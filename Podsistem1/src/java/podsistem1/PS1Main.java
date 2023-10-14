package podsistem1;

import entities.Grad;

import entities.Korisnik;
import java.math.BigDecimal;
import java.util.List;
import java.util.StringTokenizer;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class PS1Main {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    public static ConnectionFactory connectionFactory;

    @Resource(lookup = "requestTopic")
    public static Topic requestTopic;

    @Resource(lookup = "responseTopic")
    public static Topic responseTopic;

    private static JMSContext context;
    private static JMSProducer producer;

    private static JMSContext getContext() {
        if (context == null) {
            context = connectionFactory.createContext();
        }
        return context;
    }

    private static JMSProducer getProducer() {
        if (context == null) {
            context = connectionFactory.createContext();
        }
        if (producer == null) {
            producer = context.createProducer();
        }
        return producer;
    }

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem1PU");
    ;
    private static final EntityManager em = emf.createEntityManager();

    //Kreiranje grada
    private static void zahtev1(String objMsg) {

        //System.out.println("Usao u zahtev");
        StringTokenizer st = new StringTokenizer(objMsg, "#");

        String naziv = st.nextToken();

        EntityTransaction et = em.getTransaction();
        et.begin();

        Grad grad = new Grad();
        grad.setNaziv(naziv);

        em.persist(grad);
        et.commit();
    }

    //Kreiranje korisnika
    private static void zahtev2(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        String korIme = st.nextToken();
        String sifra = st.nextToken();
        String ime = st.nextToken();
        String prezime = st.nextToken();
        String adresa = st.nextToken();
        int idgrad = Integer.parseInt(st.nextToken());
        int iznos = Integer.parseInt(st.nextToken());

        Grad grad = (Grad) em.createNamedQuery("Grad.findByIdGrad").setParameter("IdGrad", idgrad).getSingleResult();

        EntityTransaction et = em.getTransaction();
        et.begin();

        Korisnik korisnik = new Korisnik();
        korisnik.setKorisnickoIme(korIme);
        korisnik.setSifra(sifra);
        korisnik.setIme(ime);
        korisnik.setPrezime(prezime);
        korisnik.setAdresa(adresa);
        korisnik.setGrad(grad);
        korisnik.setNovac(BigDecimal.valueOf(iznos));

        em.persist(korisnik);
        et.commit();
    }

    //Dodaj novac korisniku
    private static void zahtev3(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        int idKor = Integer.parseInt(st.nextToken());
        int iznos = Integer.parseInt(st.nextToken());

        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("IdKorisnik", idKor).getSingleResult();

        EntityTransaction et = em.getTransaction();
        et.begin();

        korisnik.setNovac(korisnik.getNovac().add(new BigDecimal(iznos)));

        em.persist(korisnik);
        et.commit();

    }

    //Promeni adresu i grad korisnika
    private static void zahtev4(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        int idKor = Integer.parseInt(st.nextToken());
        String adresa = st.nextToken();
        int idgrad = Integer.parseInt(st.nextToken());

        Grad grad = (Grad) em.createNamedQuery("Grad.findByIdGrad").setParameter("IdGrad", idgrad).getSingleResult();

        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("IdKorisnik", idKor).getSingleResult();

        EntityTransaction et = em.getTransaction();
        et.begin();

        korisnik.setAdresa(adresa);
        korisnik.setGrad(grad);

        em.persist(korisnik);
        et.commit();

    }

    //Dohvatanje svih gradova
    private static void zahtev12() {

        List<Grad> sviGradovi = em.createNamedQuery("Grad.findAll").getResultList();
        StringBuilder sb = new StringBuilder();

        for (Grad g : sviGradovi) {
            sb.append("@");
            sb.append(g.getIdGrad());
            sb.append("#");
            sb.append(g.getNaziv());
        }

        JMSContext contextG = getContext();
        JMSProducer producerG = getProducer();

        TextMessage textMsg = contextG.createTextMessage(sb.toString());
        try {
            textMsg.setIntProperty("rbrZahtev", 12);
        } catch (JMSException ex) {
            Logger.getLogger(PS1Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        producerG.send(responseTopic, textMsg);
    }

    //Dohvatanje svih korisnika
    private static void zahtev13() {

        List<Korisnik> sviKorisnici = em.createNamedQuery("Korisnik.findAll").getResultList();
        StringBuilder sb = new StringBuilder();
        for (Korisnik k : sviKorisnici) {
            sb.append("@");
            sb.append(k.getIdKorisnik());
            sb.append("#");
            sb.append(k.getKorisnickoIme());
            sb.append("#");
            sb.append(k.getSifra());
            sb.append("#");
            sb.append(k.getIme());
            sb.append("#");
            sb.append(k.getPrezime());
            sb.append("#");
            sb.append(k.getAdresa());
            sb.append("#");
            sb.append(k.getGrad().getNaziv());
            sb.append("#");
            sb.append(k.getNovac());
        }

        JMSContext contextG = getContext();
        JMSProducer producerG = getProducer();

        TextMessage textMsg = contextG.createTextMessage(sb.toString());
        try {
            textMsg.setIntProperty("rbrZahtev", 13);
        } catch (JMSException ex) {
            Logger.getLogger(PS1Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        producerG.send(responseTopic, textMsg);
    }

    private static void zahtev31(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        int idKor = Integer.parseInt(st.nextToken());
        BigDecimal iznos = new BigDecimal(st.nextToken());

        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("IdKorisnik", idKor).getSingleResult();

        EntityTransaction et = em.getTransaction();
        et.begin();

        korisnik.setNovac(iznos);

        em.persist(korisnik);
        et.commit();

    }

    public static void main(String[] args) {

        JMSContext context1 = getContext();
        JMSConsumer consumer = context1.createConsumer(requestTopic,
                "rbrZahtev=1 OR rbrZahtev=2 OR rbrZahtev=3 OR rbrZahtev=4 OR  rbrZahtev=12 OR rbrZahtev=13 OR rbrZahtev=31", false);
        while (true) {
            try {
                ObjectMessage objMsg = (ObjectMessage) consumer.receive();
                switch (objMsg.getIntProperty("rbrZahtev")) {
                    case 1:
                        zahtev1((String) objMsg.getObject());
                        break;
                    case 2:
                        zahtev2((String) objMsg.getObject());
                        break;
                    case 3:
                        zahtev3((String) objMsg.getObject());
                        break;
                    case 4:
                        zahtev4((String) objMsg.getObject());
                        break;
                    case 12:
                        zahtev12();
                        break;
                    case 13:
                        zahtev13();
                        break;
                    case 31:
                        zahtev31((String) objMsg.getObject());
                        break;

                }
            } catch (JMSException ex) {
            }
        }
    }

}
