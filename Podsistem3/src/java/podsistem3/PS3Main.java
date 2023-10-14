/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entities.Artikal;
import entities.Grad;
import entities.Korisnik;
import entities.Korpa;
import entities.Narudzbina;
import entities.Stavka;
import entities.Transakcija;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

/**
 *
 * @author USER
 */
public class PS3Main {

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

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem3PU");

    private static final EntityManager em = emf.createEntityManager();

    //Kreiranje grada
    private static void zahtev1(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");
        String naziv = st.nextToken();

        EntityTransaction et = em.getTransaction();
        et.begin();

        Grad grad = new Grad();
        grad.setNaziv(naziv);

        em.persist(grad);
        et.commit();
    }

    //Kreiranje korisnika i njegove korpe
    private static void zahtev2(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        String korIme = st.nextToken();
        String sifra = st.nextToken();
        String ime = st.nextToken();
        String prezime = st.nextToken();
        String adresa = st.nextToken();
        int idgrad = Integer.parseInt(st.nextToken());
        int iznos = Integer.parseInt(st.nextToken());

        EntityTransaction et = em.getTransaction();
        et.begin();

        Korisnik korisnik = new Korisnik();
        korisnik.setKorisnickoIme(korIme);
        korisnik.setSifra(sifra);
        korisnik.setIme(ime);
        korisnik.setPrezime(prezime);
        korisnik.setAdresa(adresa);
        korisnik.setGrad(idgrad);
        korisnik.setNovac(BigDecimal.valueOf(iznos));

        Korpa korpa = new Korpa();
        korpa.setKorisnik(korisnik);
        korpa.setUkupnaCena(BigDecimal.ZERO);

        em.persist(korisnik);
        em.persist(korpa);
        et.commit();
    }

    //Dodaj novac korisniku
    private static void zahtev3(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        int idKor = Integer.parseInt(st.nextToken());
        int iznos = Integer.parseInt(st.nextToken());

        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("idKorisnik", idKor).getSingleResult();

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

        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("idKorisnik", idKor).getSingleResult();

        EntityTransaction et = em.getTransaction();
        et.begin();

        korisnik.setAdresa(adresa);
        korisnik.setGrad(idgrad);

        em.persist(korisnik);
        et.commit();

    }

    //Kreiranje artikla
    private static void zahtev6(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        String naziv = st.nextToken();
        String opis = st.nextToken();
        if ("null".equals(opis)) {
            opis = null;
        }
        BigDecimal cena = new BigDecimal(st.nextToken());
        int popust = Integer.parseInt(st.nextToken());

        int idKat = Integer.parseInt(st.nextToken());
        int idKor = Integer.parseInt(st.nextToken());

        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("idKorisnik", idKor).getSingleResult();

        EntityTransaction et = em.getTransaction();
        et.begin();

        Artikal artikal = new Artikal();
        artikal.setNaziv(naziv);
        artikal.setOpis(opis);
        artikal.setCena(cena);
        artikal.setPopust(popust);
        artikal.setKategorija(idKat);
        artikal.setKorisnik(korisnik);

        em.persist(artikal);
        et.commit();
    }

    //Promeni cenu artikla
    private static void zahtev7(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        int idArt = Integer.parseInt(st.nextToken());
        BigDecimal iznos = new BigDecimal(st.nextToken());

        Artikal artikal = em.createNamedQuery("Artikal.findByIdArtikal", Artikal.class).setParameter("idArtikal", idArt).getSingleResult();

        EntityTransaction et = em.getTransaction();
        et.begin();
        artikal.setCena(iznos);
        em.persist(artikal);

        List<Stavka> stavke = em.createNamedQuery("Stavka.findByIdArtikalNull").setParameter("idArtikal", artikal).getResultList();
        for (Stavka stavka : stavke) {
            stavka.setJedinicnaCena(artikal.getCena().multiply(new BigDecimal((100 - artikal.getPopust()) * 1.0 / 100).setScale(2, BigDecimal.ROUND_HALF_UP)));
            em.persist(stavka);
        }

        List<Korpa> korpe = em.createNamedQuery("Korpa.findAll").getResultList();
        for (Korpa korpa : korpe) {
            korpa.setUkupnaCena(BigDecimal.ZERO);
            List<Stavka> mojestavke = em.createNamedQuery("Stavka.findByIdKorpa").setParameter("idKorpa", korpa).getResultList();
            for (Stavka stavka : mojestavke) {
                korpa.setUkupnaCena(korpa.getUkupnaCena().add(stavka.getJedinicnaCena().multiply(new BigDecimal(stavka.getKolicina()))));
            }
            em.persist(korpa);
        }

        et.commit();

    }

    //Postavi popust artikla
    private static void zahtev8(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        int idArt = Integer.parseInt(st.nextToken());
        int popust = Integer.parseInt(st.nextToken());

        Artikal artikal = em.createNamedQuery("Artikal.findByIdArtikal", Artikal.class).setParameter("idArtikal", idArt).getSingleResult();

        EntityTransaction et = em.getTransaction();
        et.begin();

        artikal.setPopust(popust);
        em.persist(artikal);

        List<Stavka> stavke = em.createNamedQuery("Stavka.findByIdArtikalNull").setParameter("idArtikal", artikal).getResultList();
        for (Stavka stavka : stavke) {
            stavka.setJedinicnaCena(artikal.getCena().multiply(new BigDecimal((100 - artikal.getPopust()) * 1.0 / 100).setScale(2, BigDecimal.ROUND_HALF_UP)));
            em.persist(stavka);
        }

        List<Korpa> korpe = em.createNamedQuery("Korpa.findAll").getResultList();
        for (Korpa korpa : korpe) {
            korpa.setUkupnaCena(BigDecimal.ZERO);
            List<Stavka> mojestavke = em.createNamedQuery("Stavka.findByIdKorpa").setParameter("idKorpa", korpa).getResultList();
            for (Stavka stavka : mojestavke) {
                korpa.setUkupnaCena(korpa.getUkupnaCena().add(stavka.getJedinicnaCena().multiply(new BigDecimal(stavka.getKolicina()))));
            }
            em.persist(korpa);
        }

        et.commit();

    }

    //DDodaj artikal u korpu korisnika
    private static void zahtev9(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        int idKorisnik = Integer.parseInt(st.nextToken()); // idKorisnik = IdKorpa
        int idArtikal = Integer.parseInt(st.nextToken());
        int kolicina = Integer.parseInt(st.nextToken());
        System.out.println("zahtev 9   " + idKorisnik + "  " + idArtikal + "  " + kolicina);

        Korpa korpa = em.createNamedQuery("Korpa.findByIdKorpa", Korpa.class).setParameter("idKorpa", idKorisnik).getSingleResult();
        System.out.println("nasao korpu " + korpa.getIdKorpa());

        List<Stavka> mojestavke = em.createNamedQuery("Stavka.findByIdKorpa").setParameter("idKorpa", korpa).getResultList();
        System.out.println("nasao mojestavke " + mojestavke);

        Artikal artikal = em.createNamedQuery("Artikal.findByIdArtikal", Artikal.class).setParameter("idArtikal", idArtikal).getSingleResult();
        System.out.println("nasao artikal " + artikal);

        boolean stavkapostoji = false;
        for (Stavka stavka : mojestavke) {
            if (stavka.getArtikal().getIdArtikal() == idArtikal) {
                stavkapostoji = true;
                EntityTransaction et = em.getTransaction();
                et.begin();
                stavka.setKolicina(stavka.getKolicina() + kolicina);
                korpa.setUkupnaCena(korpa.getUkupnaCena().add(stavka.getJedinicnaCena().multiply(new BigDecimal(kolicina))));
                em.persist(stavka);
                em.persist(korpa);
                et.commit();

            }
        }

        if (stavkapostoji == false) {
            EntityTransaction et = em.getTransaction();
            et.begin();
            Stavka stavka = new Stavka();
            stavka.setKorpa(korpa);
            stavka.setArtikal(artikal);
            stavka.setKolicina(kolicina);
            stavka.setJedinicnaCena(artikal.getCena().multiply(new BigDecimal((100 - artikal.getPopust()) * 1.0 / 100).setScale(2)));
            korpa.setUkupnaCena(korpa.getUkupnaCena().add(stavka.getJedinicnaCena().multiply(new BigDecimal(kolicina))));
            em.persist(stavka);
            em.persist(korpa);
            et.commit();

        }
    }

    //Izbrisi artikal iz korpe korisnika
    private static void zahtev10(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        int idKorisnik = Integer.parseInt(st.nextToken()); // idKorisnik = IdKorpa
        int idArtikal = Integer.parseInt(st.nextToken());
        int kolicina = Integer.parseInt(st.nextToken());
        System.out.println("zahtev 10   " + idKorisnik + "  " + idArtikal + "  " + kolicina);

        Korpa korpa = em.createNamedQuery("Korpa.findByIdKorpa", Korpa.class).setParameter("idKorpa", idKorisnik).getSingleResult();
        //System.out.println("nasao korpu " + korpa.getIdKorisnik());

        List<Stavka> mojestavke = em.createNamedQuery("Stavka.findByIdKorpa").setParameter("idKorpa", korpa).getResultList();
        //System.out.println("nasao mojestavke " + mojestavke);

        Artikal artikal = em.createNamedQuery("Artikal.findByIdArtikal", Artikal.class).setParameter("idArtikal", idArtikal).getSingleResult();
        //System.out.println("nasao artikal " + artikal);

        boolean stavkapostoji = false;
        for (Stavka stavka : mojestavke) {
            if (stavka.getArtikal().getIdArtikal() == idArtikal) {
                stavkapostoji = true;
                EntityTransaction et = em.getTransaction();
                et.begin();

                int pravaKolicina = stavka.getKolicina();
                if (kolicina > pravaKolicina) {
                    kolicina = pravaKolicina;
                }

                korpa.setUkupnaCena(korpa.getUkupnaCena().subtract(stavka.getJedinicnaCena().multiply(new BigDecimal(kolicina))));

                stavka.setKolicina(pravaKolicina - kolicina);

                if (stavka.getKolicina() == 0) {
                    System.out.println("remove  stavka");
                    em.remove(stavka);
                    em.flush();
                    em.persist(korpa);
                    et.commit();
                    return;

                }

                em.persist(stavka);
                em.persist(korpa);
                et.commit();

            }
        }

        //nista se ne desava ako stavka ne postoji
    }

    //Placanje
    private static void zahtev11(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        int idKor = Integer.parseInt(st.nextToken());

        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("idKorisnik", idKor).getSingleResult();
        Korpa korpa = em.createNamedQuery("Korpa.findByIdKorpa", Korpa.class).setParameter("idKorpa", idKor).getSingleResult();

        if (korpa.getUkupnaCena().compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("nista nema u korpi");
            return;
        }

        if (korpa.getUkupnaCena().compareTo(korisnik.getNovac()) == 1) { // cena korpe je veca od korisnikovog novca 
            System.out.println("korisnik nema dovoljno novca");
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        LocalDateTime now = LocalDateTime.now();
        String currDateString = dtf.format(now);
        java.util.Date currDate = null;
        try {
            currDate = format.parse(currDateString);
        } catch (Exception e) {
        }

        Narudzbina narudzbina = new Narudzbina();

        narudzbina.setUkupnaCena(korpa.getUkupnaCena());
        narudzbina.setVreme(currDate);
        narudzbina.setGrad(korisnik.getGrad());
        narudzbina.setAdresa(korisnik.getAdresa());
        narudzbina.setIdKorisnik(korisnik);

        EntityTransaction et = em.getTransaction();
        et.begin();

        em.persist(narudzbina);
        em.flush();
        em.refresh(narudzbina);

        System.out.println("Narudzbina " + narudzbina);

        Transakcija transakcija = new Transakcija();
        transakcija.setIdNaruzbina(narudzbina.getIdNarudzbina());
        transakcija.setSuma(korpa.getUkupnaCena());
        transakcija.setVreme(currDate);

        em.persist(transakcija);
        em.flush();
        em.refresh(transakcija);

        //sredi stavke
        List<Stavka> mojestavke = em.createNamedQuery("Stavka.findByIdKorpa").setParameter("idKorpa", korpa).getResultList();
        for (Stavka stavka : mojestavke) {
            stavka.setIdNarudzbina(narudzbina);
            stavka.setKorpa(null);
        }

        //smanji novac korisniku
        korisnik.setNovac(korisnik.getNovac().subtract(korpa.getUkupnaCena()));

        //korpa.ukupnaCena = 0 
        korpa.setUkupnaCena(BigDecimal.ZERO);

        et.commit();

    }

    //Dohvati narudzbine korisnika
    private static void zahtev17(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");
        int idKorisnik = Integer.parseInt(st.nextToken());
        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("idKorisnik", idKorisnik).getSingleResult();

        List<Narudzbina> mojenarudzbine = em.createNamedQuery("Narudzbina.findByIdKorisnik").setParameter("idKorisnik", korisnik).getResultList();
        //System.out.println("nasao mojestavke " + mojestavke);
        StringBuilder sb = new StringBuilder();

        for (Narudzbina n : mojenarudzbine) {
            sb.append("@");
            sb.append(n.getIdNarudzbina());
            sb.append("#");
            sb.append(n.getUkupnaCena());
            sb.append("#");
            sb.append(n.getVreme());
            sb.append("#");
            Grad grad = (Grad) em.createNamedQuery("Grad.findByIdGrad").setParameter("idGrad", n.getGrad()).getSingleResult();
            sb.append(grad.getNaziv());
            sb.append("#");
            sb.append(n.getIdKorisnik().getAdresa());
        }
        JMSContext contextN = getContext();
        JMSProducer producerN = getProducer();

        TextMessage textMsg = contextN.createTextMessage(sb.toString());
        try {
            textMsg.setIntProperty("rbrZahtev", 17);
        } catch (JMSException ex) {
            Logger.getLogger(PS3Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        producerN.send(responseTopic, textMsg);
    }

    //Dohvatanje svih narudzbina
    private static void zahtev18() {

        List<Narudzbina> sveNarudzbina = em.createNamedQuery("Narudzbina.findAll").getResultList();
        StringBuilder sb = new StringBuilder();
        for (Narudzbina n : sveNarudzbina) {
            sb.append("@");
            sb.append(n.getIdNarudzbina());
            sb.append("#");
            sb.append(n.getUkupnaCena());
            sb.append("#");
            sb.append(n.getVreme());
            sb.append("#");
            Grad grad = (Grad) em.createNamedQuery("Grad.findByIdGrad").setParameter("idGrad", n.getGrad()).getSingleResult();
            sb.append(grad.getNaziv());
            sb.append("#");
            sb.append(n.getIdKorisnik().getAdresa());
            sb.append("#");
            sb.append(n.getIdKorisnik().getIdKorisnik());
        }

        JMSContext contextN = getContext();
        JMSProducer producerN = getProducer();

        TextMessage textMsg = contextN.createTextMessage(sb.toString());
        try {
            textMsg.setIntProperty("rbrZahtev", 18);
        } catch (JMSException ex) {
            Logger.getLogger(PS3Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        producerN.send(responseTopic, textMsg);
    }

    // dohvatanje svih transakcija
    private static void zahtev19() {

        List<Transakcija> sveTransakcije = em.createNamedQuery("Transakcija.findAll").getResultList();
        StringBuilder sb = new StringBuilder();
        for (Transakcija t : sveTransakcije) {
            sb.append("@");
            sb.append(t.getIdTransakcija());
            sb.append("#");
            sb.append(t.getIdNaruzbina());
            sb.append("#");
            sb.append(t.getSuma());
            sb.append("#");
            sb.append(t.getVreme());

        }

        JMSContext contextT = getContext();
        JMSProducer producerT = getProducer();

        TextMessage textMsg = contextT.createTextMessage(sb.toString());
        try {
            textMsg.setIntProperty("rbrZahtev", 19);
        } catch (JMSException ex) {
            Logger.getLogger(PS3Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        producerT.send(responseTopic, textMsg);
    }

    private static void zahtev30(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");
        int idKor = Integer.parseInt(st.nextToken());

        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("idKorisnik", idKor).getSingleResult();

        StringBuilder sb = new StringBuilder();
        sb.append(korisnik.getNovac());

        JMSContext contextT = getContext();
        JMSProducer producerT = getProducer();

        TextMessage textMsg = contextT.createTextMessage(sb.toString());
        try {
            textMsg.setIntProperty("rbrZahtev", 30);
        } catch (JMSException ex) {
            Logger.getLogger(PS3Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        producerT.send(responseTopic, textMsg);
    }

    public static void main(String[] args) {

        JMSContext context3 = getContext();
        JMSConsumer consumer = context3.createConsumer(requestTopic,
                "rbrZahtev=1 OR rbrZahtev=2 OR rbrZahtev=3 OR rbrZahtev=4 OR rbrZahtev=6 OR rbrZahtev=7 OR rbrZahtev=8 OR rbrZahtev=9 OR rbrZahtev=10 OR rbrZahtev=11 OR  rbrZahtev=17 OR rbrZahtev=18 OR rbrZahtev=19 OR rbrZahtev=30", false);
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
                    case 6:
                        zahtev6((String) objMsg.getObject());
                        break;
                    case 7:
                        zahtev7((String) objMsg.getObject());
                        break;
                    case 8:
                        zahtev8((String) objMsg.getObject());
                        break;
                    case 9:
                        zahtev9((String) objMsg.getObject());
                        break;
                    case 10:
                        zahtev10((String) objMsg.getObject());
                        break;
                    case 11:
                        zahtev11((String) objMsg.getObject());
                        break;
                    case 17:
                        zahtev17((String) objMsg.getObject());
                        break;
                    case 18:
                        zahtev18();
                        break;
                    case 19:
                        zahtev19();
                        break;
                    case 30:
                        zahtev30((String) objMsg.getObject());
                        break;

                }
            } catch (JMSException ex) {
            }
        }
    }

}
