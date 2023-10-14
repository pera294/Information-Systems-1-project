package podsistem2;

import entities.Artikal;
import entities.Kategorija;
import entities.Korisnik;
import entities.Korpa;
import entities.Stavka;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
public class PS2Main {

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

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem2PU");
    ;
    private static final EntityManager em = emf.createEntityManager();

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
        korpa.setIdKorisnik(korisnik);
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

    //Kreiranje kategorije
    private static void zahtev5(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        String naziv = st.nextToken();
        int natkategorija = Integer.parseInt(st.nextToken());

        Kategorija nk = null;
        if (natkategorija != 0) {
            nk = em.createNamedQuery("Kategorija.findByIdKategorija", Kategorija.class).setParameter("idKategorija", natkategorija).getSingleResult();
        }

        EntityTransaction et = em.getTransaction();
        et.begin();

        Kategorija kategorija = new Kategorija();
        kategorija.setNaziv(naziv);
        kategorija.setNatkategorija(nk);

        em.persist(kategorija);
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

        Kategorija kategorija = em.createNamedQuery("Kategorija.findByIdKategorija", Kategorija.class).setParameter("idKategorija", idKat).getSingleResult();
        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("idKorisnik", idKor).getSingleResult();

        EntityTransaction et = em.getTransaction();
        et.begin();

        Artikal artikal = new Artikal();
        artikal.setNaziv(naziv);
        artikal.setOpis(opis);
        artikal.setCena(cena);
        artikal.setPopust(popust);
        artikal.setKategorija(kategorija);
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

        List<Stavka> stavke = em.createNamedQuery("Stavka.findByIdArtikal").setParameter("idArtikal", artikal).getResultList();
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

        List<Stavka> stavke = em.createNamedQuery("Stavka.findByIdArtikal").setParameter("idArtikal", artikal).getResultList();
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

    //Dodaj artikal u korpu korisnika
    private static void zahtev9(String objMsg) {

        StringTokenizer st = new StringTokenizer(objMsg, "#");

        int idKorisnik = Integer.parseInt(st.nextToken()); // idKorisnik = IdKorpa
        int idArtikal = Integer.parseInt(st.nextToken());
        int kolicina = Integer.parseInt(st.nextToken());
        System.out.println("zahtev 9   " + idKorisnik + "  " + idArtikal + "  " + kolicina);

        Korpa korpa = em.createNamedQuery("Korpa.findByIdKorpa", Korpa.class).setParameter("idKorpa", idKorisnik).getSingleResult();
        //System.out.println("nasao korpu " + korpa.getIdKorisnik());

        List<Stavka> mojestavke = em.createNamedQuery("Stavka.findByIdKorpa").setParameter("idKorpa", korpa).getResultList();
        //System.out.println("nasao mojestavke " + mojestavke);

        Artikal artikal = em.createNamedQuery("Artikal.findByIdArtikal", Artikal.class).setParameter("idArtikal", idArtikal).getSingleResult();
        //System.out.println("nasao artikal " + artikal);

        boolean stavkapostoji = false;
        for (Stavka stavka : mojestavke) {
            if (stavka.getIdArtikal().getIdArtikal() == idArtikal) {
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
            stavka.setIdKorpa(korpa);
            stavka.setIdArtikal(artikal);
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
            if (stavka.getIdArtikal().getIdArtikal() == idArtikal) {
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
            //System.out.println("nista nema u korpi");
            return;
        }

        if (korpa.getUkupnaCena().compareTo(korisnik.getNovac()) == 1) { // cena korpe je veca od korisnikovog novca 
            //System.out.println("korisnik nema dovoljno novca");
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

        EntityTransaction et = em.getTransaction();
        et.begin();

        //izbrisi stavke
        List<Stavka> mojestavke = em.createNamedQuery("Stavka.findByIdKorpa").setParameter("idKorpa", korpa).getResultList();
        for (Stavka stavka : mojestavke) {
            em.remove(stavka);
        }

        //smanji novac korisniku
        korisnik.setNovac(korisnik.getNovac().subtract(korpa.getUkupnaCena()));

        //korpa.ukupnaVrednost = 0 
        korpa.setUkupnaCena(BigDecimal.ZERO);

        et.commit();

    }

    //Dohvati sve kategorije
    private static void zahtev14() {

        List<Kategorija> sveKategorije = em.createNamedQuery("Kategorija.findAll").getResultList();
        StringBuilder sb = new StringBuilder();

        for (Kategorija k : sveKategorije) {
            sb.append("@");
            sb.append(k.getIdKategorija());
            sb.append("#");
            sb.append(k.getNaziv());
            sb.append("#");
            if (k.getNatkategorija() != null) {
                sb.append(k.getNatkategorija().getNaziv());
            } else {
                sb.append(k.getNatkategorija());
            }
        }

        JMSContext contextK = getContext();
        JMSProducer producerK = getProducer();

        TextMessage textMsg = contextK.createTextMessage(sb.toString());
        try {
            textMsg.setIntProperty("rbrZahtev", 14);
        } catch (JMSException ex) {
            Logger.getLogger(PS2Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        producerK.send(responseTopic, textMsg);
    }

    //Dohvatanje svih artikala koje prodaje korisnik 
    private static void zahtev15(String objMsg) {
        StringTokenizer st = new StringTokenizer(objMsg, "#");
        int idKorisnik = Integer.parseInt(st.nextToken());
        //System.out.println("podsistem2.PS2Main.zahtev15()   " + idKorisnik);
        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("idKorisnik", idKorisnik).getSingleResult();

        List<Artikal> mojiArtikli = em.createNamedQuery("Artikal.findByIdKorisnik", Artikal.class).setParameter("korisnik", korisnik).getResultList();
        //List<Artikal> mojiArtikli = em.createNamedQuery("Artikal.findAll").getResultList();
        StringBuilder sb = new StringBuilder();

        for (Artikal a : mojiArtikli) {
            sb.append("@");
            sb.append(a.getIdArtikal());
            sb.append("#");
            sb.append(a.getNaziv());
            sb.append("#");
            sb.append(a.getOpis());
            sb.append("#");
            sb.append(a.getOcena());
            sb.append("#");
            sb.append(a.getCena());
            sb.append("#");
            sb.append(a.getPopust());
            sb.append("#");
            sb.append(a.getKategorija().getNaziv());
            sb.append("#");
            sb.append(a.getKorisnik().getIdKorisnik());

            System.out.println(a.getNaziv());
        }

        JMSContext contextA = getContext();
        JMSProducer producerA = getProducer();

        TextMessage textMsg = contextA.createTextMessage(sb.toString());
        try {
            textMsg.setIntProperty("rbrZahtev", 15);
        } catch (JMSException ex) {
            Logger.getLogger(PS2Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        producerA.send(responseTopic, textMsg);
    }

    //Dohvati sadrzaj korpe korisnika
    private static void zahtev16(String objMsg) {
        StringTokenizer st = new StringTokenizer(objMsg, "#");
        int idKorisnik = Integer.parseInt(st.nextToken());
        //System.out.println("podsistem2.PS2Main.zahtev16()   " + idKorisnik);
        Korisnik korisnik = em.createNamedQuery("Korisnik.findByIdKorisnik", Korisnik.class).setParameter("idKorisnik", idKorisnik).getSingleResult();

        Korpa korpa = em.createNamedQuery("Korpa.findByIdKorpa", Korpa.class).setParameter("idKorpa", idKorisnik).getSingleResult();
        //System.out.println("nasao korpu " + korpa.getIdKorisnik());

        List<Stavka> mojestavke = em.createNamedQuery("Stavka.findByIdKorpa").setParameter("idKorpa", korpa).getResultList();
        //System.out.println("nasao mojestavke " + mojestavke);
        StringBuilder sb = new StringBuilder();

        for (Stavka s : mojestavke) {
            sb.append("@");
            sb.append(s.getIdArtikal().getIdArtikal());
            sb.append("#");
            sb.append(s.getIdArtikal().getNaziv());
            sb.append("#");
            sb.append(s.getKolicina());
            sb.append("#");
            sb.append(s.getJedinicnaCena());

        }

        JMSContext contextK = getContext();
        JMSProducer producerK = getProducer();

        TextMessage textMsg = contextK.createTextMessage(sb.toString());
        try {
            textMsg.setIntProperty("rbrZahtev", 16);
        } catch (JMSException ex) {
            Logger.getLogger(PS2Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        producerK.send(responseTopic, textMsg);
    }

    public static void main(String[] args) {

        JMSContext context2 = getContext();
        JMSConsumer consumer = context2.createConsumer(requestTopic,
                "rbrZahtev=2 OR rbrZahtev=3 OR rbrZahtev=4 OR rbrZahtev=5 OR rbrZahtev=6 OR rbrZahtev=7 OR rbrZahtev=8 OR rbrZahtev=9 OR rbrZahtev=10 OR rbrZahtev=11 OR rbrZahtev=14 OR rbrZahtev=15 OR rbrZahtev=16 ", false);

        while (true) {
            try {
                ObjectMessage objMsg = (ObjectMessage) consumer.receive();
                switch (objMsg.getIntProperty("rbrZahtev")) {
                    case 2:
                        zahtev2((String) objMsg.getObject());
                        break;
                    case 3:
                        zahtev3((String) objMsg.getObject());
                        break;
                    case 4:
                        zahtev4((String) objMsg.getObject());
                        break;
                    case 5:
                        zahtev5((String) objMsg.getObject());
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
                    case 14:
                        zahtev14();
                        break;
                    case 15:
                        zahtev15((String) objMsg.getObject());
                        break;
                    case 16:
                        zahtev16((String) objMsg.getObject());
                        break;

                }
            } catch (JMSException ex) {
            }
        }
    }

}
