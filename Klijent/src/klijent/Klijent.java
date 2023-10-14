package klijent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Klijent {

    private static final String targetURL = "http://localhost:8080/CentralniServer/resources";

    private static void postRequest(String parameter, String urlParameter) {
        try {
            URL url = new URL(targetURL + urlParameter);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(parameter);
            out.flush();
            out.close();

            con.getResponseCode();
        } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getRequest(String urlParameter) {
        try {
            URL url = new URL(targetURL + urlParameter);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content.toString();

        } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    private static String getRequestwithParam(String urlParameter, String parameter) {
        try {
            URL url = new URL(targetURL + urlParameter + parameter);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            return content.toString();

        } catch (IOException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    private static void kreirajGrad(String naziv) {
        String parameter = naziv + "#";
        postRequest(parameter, "/grad");
    }

    private static void kreirajKorisnika(String korIme, String sifra, String ime, String prezime, String adresa, int idgrad, int iznos) {
        String parameter = korIme + "#" + sifra + "#" + ime + "#" + prezime + "#" + adresa + "#" + idgrad + "#" + iznos;
        postRequest(parameter, "/korisnik/kreirajKorisnika");
    }

    private static String dohvatiGradove() {
        String content = getRequest("/grad");

        if (!content.equals("")) {
            StringBuilder sb = new StringBuilder();
            StringTokenizer st = new StringTokenizer(content, "@");
            sb.append("IDGrad   Naziv\n");
            while (st.hasMoreTokens()) {
                StringTokenizer stn = new StringTokenizer(st.nextToken(), "#");
                sb.append(String.format("%-8s %-15s\n", stn.nextToken(), stn.nextToken()));
            }
            return sb.toString();
        }
        return "null";
    }

    private static String dohvatiKorisnike() {
        String content = getRequest("/korisnik/dohvatiKorisnike");

        if (!content.equals("")) {
            StringBuilder sb = new StringBuilder();
            StringTokenizer st = new StringTokenizer(content, "@");
            sb.append("ID   KorisnickoIme   Sifra   Ime     Prezime   Adresa      Grad     Novac\n");
            while (st.hasMoreTokens()) {
                StringTokenizer stn = new StringTokenizer(st.nextToken(), "#");
                sb.append(String.format("%-3s  %-14s  %-6s  %-7s  %-9s  %-10s  %-8s  %s\n",
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken()));
            }
            return sb.toString();
        }
        return "null";
    }

    private static void dodajNovac(int idkorisnika, int iznos) {
        String parameter = idkorisnika + "#" + iznos + "#";
        postRequest(parameter, "/korisnik/dodajnovac");
    }

    private static void promeniAdresuGrad(int idkorisnika, String adresa, int idgrad) {
        String parameter = idkorisnika + "#" + adresa + "#" + idgrad + "#";
        postRequest(parameter, "/korisnik/promeniAdresuGrad");
    }

    private static void kreirajKategoriju(String kategorija, int potkategorija) {
        String parameter = kategorija + "#" + potkategorija + "#";
        postRequest(parameter, "/kategorija");
    }

    private static String dohvatiKategorije() {
        String content = getRequest("/kategorija");

        if (!content.equals("")) {
            StringBuilder sb = new StringBuilder();
            StringTokenizer st = new StringTokenizer(content, "@");
            sb.append("IDKat   Naziv          Natkategorija\n");
            while (st.hasMoreTokens()) {
                StringTokenizer stn = new StringTokenizer(st.nextToken(), "#");
                sb.append(String.format("%-8s %-15s %-12s\n",
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken()));
            }
            return sb.toString();
        }
        return "null";
    }

    private static void kreirajArtikal(String naziv, String opis, double cena, int popust, int kategorija, int korisnik) {
        String parameter = naziv + "#" + opis + "#" + cena + "#" + popust + "#" + kategorija + "#" + korisnik + "#";
        postRequest(parameter, "/artikal/kreirajArtikal");
    }

    private static void promeniCenuArtikla(int idArtikal, double iznos) {
        String parameter = idArtikal + "#" + iznos + "#";
        postRequest(parameter, "/artikal/promeniCenuArtikla");
    }

    private static void postaviPopustArtikla(int idArtikal, int popust) {
        String parameter = idArtikal + "#" + popust + "#";
        postRequest(parameter, "/artikal/postaviPopustArtikla");
    }

    private static String dohvatiArtikleZaKorisnika(int idKorisnik) {
        String content = getRequestwithParam("/artikal/", idKorisnik + "");

        if (!content.equals("")) {
            StringBuilder sb = new StringBuilder();
            StringTokenizer st = new StringTokenizer(content, "@");
            sb.append("IdArtikal   Naziv          Opis           Ocena   Cena    Popust   Kategorija   Korisnik\n");
            while (st.hasMoreTokens()) {
                StringTokenizer stn = new StringTokenizer(st.nextToken(), "#");
                sb.append(String.format("%-12s %-15s %-14s %-7s %-9s %-8s %-12s %s\n",
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken()));
            }
            return sb.toString();
        }
        return "null";
    }

    private static void dodajArtikalUKorpu(int idKorisnik, int idArtikal, int kolicina) {
        String parameter = idKorisnik + "#" + idArtikal + "#" + kolicina + "#";
        postRequest(parameter, "/artikal/dodajArtikalUKorpu");
    }

    private static void izbrisiArtikalIzKorpe(int idKorisnik, int idArtikal, int kolicina) {
        String parameter = idKorisnik + "#" + idArtikal + "#" + kolicina + "#";
        postRequest(parameter, "/artikal/izbrisiArtikalIzKorpe");
    }

    private static String dohvatiSadrzajKorpeZaKorisnika(int idKorisnik) {
        String content = getRequest("/artikal/korpa/" + idKorisnik);

        if (!content.equals("")) {
            StringBuilder sb = new StringBuilder();
            StringTokenizer st = new StringTokenizer(content, "@");
            sb.append("IdArtikla   Naziv    Kolicina    Jedinicna cena \n");
            while (st.hasMoreTokens()) {
                StringTokenizer stn = new StringTokenizer(st.nextToken(), "#");
                sb.append(String.format("%-8s %-15s %-12s %-12s \n",
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken()));
            }
            return sb.toString();
        }
        return "null";
    }

    private static void srediNovac(int idKorisnik) {
        String content = getRequest("/transakcija/novac/" + idKorisnik);
        double iznos = Double.parseDouble(content);
        System.out.println(iznos);

        String parameter = idKorisnik + "#" + iznos + "#";
        postRequest(parameter, "/korisnik/srediNovac");
    }

    ;
        
     
    private static void placanje(int idKorisnik) {
        String parameter = idKorisnik + "#";
        postRequest(parameter, "/transakcija/placanje");

        srediNovac(idKorisnik);
    }

    private static String dohvatiNarudzbineZaKorisnika(int idKorisnik) {
        String content = getRequest("/transakcija/dohvatiNarudzbineZaKorisnika/" + idKorisnik);

        if (!content.equals("")) {
            StringBuilder sb = new StringBuilder();
            StringTokenizer st = new StringTokenizer(content, "@");
            sb.append("IDNarudzbina   UkupnaCena  Vreme         Grad       Adresa\n");
            while (st.hasMoreTokens()) {
                StringTokenizer stn = new StringTokenizer(st.nextToken(), "#");
                sb.append(String.format("%-15s %-12s %-14s %-10s %s\n",
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken()));
            }
            return sb.toString();
        }
        return "null";
    }

    private static String dohvatiNarudzbine() {
        String content = getRequest("/transakcija/sveNarudzbine");

        if (!content.equals("")) {
            StringBuilder sb = new StringBuilder();
            StringTokenizer st = new StringTokenizer(content, "@");
            sb.append("IDNarudzbina   UkupnaCena  Vreme         Grad       Adresa         IdKorisnik\n");
            while (st.hasMoreTokens()) {
                StringTokenizer stn = new StringTokenizer(st.nextToken(), "#");
                sb.append(String.format("%-15s %-12s %-14s %-10s %-14s %s\n",
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken()));
            }
            return sb.toString();
        }
        return "null";
    }

    private static String dohvatiTransakcije() {
        String content = getRequest("/transakcija/sveTransakcije");

        if (!content.equals("")) {
            StringBuilder sb = new StringBuilder();
            StringTokenizer st = new StringTokenizer(content, "@");
            sb.append("IDTransakcija   IdNarudzbina  Suma         Vreme\n");
            while (st.hasMoreTokens()) {
                StringTokenizer stn = new StringTokenizer(st.nextToken(), "#");
                sb.append(String.format("%-15s %-12s %-14s %-10s\n",
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken(),
                        stn.nextToken()));
            }
            return sb.toString();
        }
        return "null";
    }

    // GUI
    String zahtevi[] = {
        "Kreiranje grada (Naziv)",
        "Kreiranje korisnika (Korisnicko ime,Sifra,Ime,Prezime,Adresa,Grad,Iznos)",
        "Dodavanje novca korisniku (IdKorisnik,iznos)",
        "Promena adrese i grada za korisnika (IdKorisnik,IdGrad,Adresa)",
        "Dohvati sve gradove",
        "Dohvati sve korisnike",
        "Kreiranje kategorije (Kategorija,Natkategorija)",
        "Dohvati sve kategorije",
        "Kreiranje artikla (Naziv,Opis,Iznos,Popust,IdKategorija,IdKorisnik)",
        "Menjanje cene artikla (IdArtikal,Iznos)",
        "Postavi popust (IdArtikal,Popust)",
        "Dohvati sve artikle za korisnika (IdKorisnik)",
        "Dodaj artikal u korpu korisnika (IdKorisnik,IdArtikal,Iznos)",
        "Izbrisi artikal iz korpe korisnika (IdKorisnik,IdArtikal,Iznos)",
        "Dohvati sadrzaj korpe korisnika (IdKorisnik)",
        "Placanje (IdKorisnik)",
        "Dohvati sve narudzbine za korisnika (IdKorisnik)",
        "Dohvati sve narudzbine",
        "Dohvati sve transakcije"

    };

    String trenutniZahtev = "";

    JFrame frame = new JFrame();
    JPanel panel = new JPanel(new BorderLayout());

    public Klijent() {

        frame.setVisible(true);
        frame.setResizable(true);
        frame.setBounds(180, 150, 1200, 700);
        frame.add(panel);

        JPanel panelUpis = new JPanel(new GridLayout(1, 2));
        panel.add(panelUpis, BorderLayout.NORTH);
        JPanel panelZahtevi = new JPanel(new GridLayout(19, 1));
        JPanel panelPodaci = new JPanel(new GridLayout(20, 1));
        panelUpis.add(panelZahtevi);
        panelUpis.add(panelPodaci);
        JPanel panelIspis = new JPanel(new BorderLayout());
        panel.add(panelIspis, BorderLayout.SOUTH);

        JTextArea textArea = new JTextArea();
        panelIspis.add(textArea, BorderLayout.CENTER);

        ButtonGroup group = new ButtonGroup();

        for (int i = 0; i < 19; i++) {

            JRadioButton button = new JRadioButton(zahtevi[i]);
            group.add(button);
            panelZahtevi.add(button);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    trenutniZahtev = button.getText() + "";
                }
            });
        }

        JPanel paneIdGrada = new JPanel(new GridLayout(1, 2));
        JLabel labelaIdGrada = new JLabel("IdGrada ");
        JTextField textIdGrada = new JTextField(15);
        paneIdGrada.add(labelaIdGrada);
        paneIdGrada.add(textIdGrada);
        panelPodaci.add(paneIdGrada);

        JPanel panelNaziv = new JPanel(new GridLayout(1, 2));
        JLabel labelaNaziv = new JLabel("Naziv");
        JTextField textNaziv = new JTextField();
        panelNaziv.add(labelaNaziv);
        panelNaziv.add(textNaziv);
        panelPodaci.add(panelNaziv);

        JPanel paneIdKorisnik = new JPanel(new GridLayout(1, 2));
        JLabel labelaIdKorisnik = new JLabel("IdKorisnik ");
        JTextField textIdKorisnik = new JTextField(15);
        paneIdKorisnik.add(labelaIdKorisnik);
        paneIdKorisnik.add(textIdKorisnik);
        panelPodaci.add(paneIdKorisnik);

        JPanel panelKorisnickoIme = new JPanel(new GridLayout(1, 2));
        JLabel labelaKorisnickoIme = new JLabel("Korisnicko ime: ");
        JTextField textKorisnickoIme = new JTextField();
        panelKorisnickoIme.add(labelaKorisnickoIme);
        panelKorisnickoIme.add(textKorisnickoIme);
        panelPodaci.add(panelKorisnickoIme);

        JPanel panelSifra = new JPanel(new GridLayout(1, 2));
        JLabel labelaSifra = new JLabel("Sifra: ");
        JTextField textSifra = new JTextField();
        panelSifra.add(labelaSifra);
        panelSifra.add(textSifra);
        panelPodaci.add(panelSifra);

        JPanel panelIme = new JPanel(new GridLayout(1, 2));
        JLabel labelaIme = new JLabel("Ime: ");
        JTextField textIme = new JTextField();
        panelIme.add(labelaIme);
        panelIme.add(textIme);
        panelPodaci.add(panelIme);

        JPanel panelPrezime = new JPanel(new GridLayout(1, 2));
        JLabel labelaPrezime = new JLabel("Prezime: ");
        JTextField textPrezime = new JTextField();
        panelPrezime.add(labelaPrezime);
        panelPrezime.add(textPrezime);
        panelPodaci.add(panelPrezime);

        JPanel panelAdresa = new JPanel(new GridLayout(1, 2));
        JLabel labelaAdresa = new JLabel("Adresa: ");
        JTextField textAdresa = new JTextField();
        panelAdresa.add(labelaAdresa);
        panelAdresa.add(textAdresa);
        panelPodaci.add(panelAdresa);

        JPanel panelIznos = new JPanel(new GridLayout(1, 2));
        JLabel labelaIznos = new JLabel("Iznos: ");
        JTextField textIznos = new JTextField();
        panelIznos.add(labelaIznos);
        panelIznos.add(textIznos);
        panelPodaci.add(panelIznos);

        JPanel panelIdKategorija = new JPanel(new GridLayout(1, 2));
        JLabel labelaIdKategorija = new JLabel("IdKategorija: ");
        JTextField textIdKategorija = new JTextField();
        panelIdKategorija.add(labelaIdKategorija);
        panelIdKategorija.add(textIdKategorija);
        panelPodaci.add(panelIdKategorija);

        JPanel panelKategorija = new JPanel(new GridLayout(1, 2));
        JLabel labelaKategorija = new JLabel("Kategorija: ");
        JTextField textKategorija = new JTextField();
        panelKategorija.add(labelaKategorija);
        panelKategorija.add(textKategorija);
        panelPodaci.add(panelKategorija);

        JPanel panelNatkategorija = new JPanel(new GridLayout(1, 2));
        JLabel labelaNatkategorija = new JLabel("Natkategorija: ");
        JTextField textNatkategorija = new JTextField();
        panelNatkategorija.add(labelaNatkategorija);
        panelNatkategorija.add(textNatkategorija);
        panelPodaci.add(panelNatkategorija);

        JPanel panelOpis = new JPanel(new GridLayout(1, 2));
        JLabel labelaOpis = new JLabel("Opis: ");
        JTextField textOpis = new JTextField();
        panelOpis.add(labelaOpis);
        panelOpis.add(textOpis);
        panelPodaci.add(panelOpis);

        JPanel panelPopust = new JPanel(new GridLayout(1, 2));
        JLabel labelaPopust = new JLabel("Popust: ");
        JTextField textPopust = new JTextField();
        panelPopust.add(labelaPopust);
        panelPopust.add(textPopust);
        panelPodaci.add(panelPopust);

        JPanel panelIdArtikal = new JPanel(new GridLayout(1, 2));
        JLabel labelaIdArtikal = new JLabel("IdArtikal: ");
        JTextField textIdArtikal = new JTextField();
        panelIdArtikal.add(labelaIdArtikal);
        panelIdArtikal.add(textIdArtikal);
        panelPodaci.add(panelIdArtikal);

        JButton izvrsi = new JButton("IZVRSI");
        panelPodaci.add(izvrsi);
        izvrsi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                switch (trenutniZahtev) {
                    case "Kreiranje grada (Naziv)":
                        String nazivG = textNaziv.getText();
                        kreirajGrad(nazivG);
                        break;
                    case "Kreiranje korisnika (Korisnicko ime,Sifra,Ime,Prezime,Adresa,Grad,Iznos)":
                        String korIme = textKorisnickoIme.getText();
                        String sifra = textSifra.getText();
                        String ime = textIme.getText();
                        String prezime = textPrezime.getText();
                        String adresa = textAdresa.getText();
                        int idgrad = Integer.parseInt(textIdGrada.getText());
                        int iznos = 0;
                        if (!textIznos.getText().isEmpty()) {
                            iznos = Integer.parseInt(textIznos.getText());
                        }
                        kreirajKorisnika(korIme, sifra, ime, prezime, adresa, idgrad, iznos);
                        break;

                    case "Dohvati sve gradove":
                        String sviGradovi = dohvatiGradove();
                        textArea.setText(sviGradovi);
                        break;

                    case "Dohvati sve korisnike":
                        String sviKorisnici = dohvatiKorisnike();
                        textArea.setText(sviKorisnici);
                        break;

                    case "Dodavanje novca korisniku (IdKorisnik,iznos)":
                        int idkorisnika = Integer.parseInt(textIdKorisnik.getText());
                        int iznos1 = Integer.parseInt(textIznos.getText());
                        dodajNovac(idkorisnika, iznos1);
                        break;

                    case "Promena adrese i grada za korisnika (IdKorisnik,IdGrad,Adresa)":
                        int idkorisnika1 = Integer.parseInt(textIdKorisnik.getText());
                        String adresa1 = textAdresa.getText();
                        int idgrad1 = Integer.parseInt(textIdGrada.getText());
                        promeniAdresuGrad(idkorisnika1, adresa1, idgrad1);
                        break;

                    case "Kreiranje kategorije (Kategorija,Natkategorija)":
                        String kategorija = textKategorija.getText();
                        int natkategorija = 0;
                        if (!textNatkategorija.getText().isEmpty()) {
                            natkategorija = Integer.parseInt(textNatkategorija.getText());
                        }

                        kreirajKategoriju(kategorija, natkategorija);
                        break;
                    case "Dohvati sve kategorije":
                        String sveKategorije = dohvatiKategorije();
                        textArea.setText(sveKategorije);
                        break;
                    case "Kreiranje artikla (Naziv,Opis,Iznos,Popust,IdKategorija,IdKorisnik)":
                        String naziv3 = textNaziv.getText();
                        String opis = "null";
                        if (!textOpis.getText().isEmpty()) {
                            opis = textOpis.getText();
                        }

                        double cena = Double.parseDouble(textIznos.getText());

                        int popust = 0;
                        if (!textPopust.getText().isEmpty()) {
                            popust = Integer.parseInt(textPopust.getText());
                        }

                        int kategorija3 = Integer.parseInt(textIdKategorija.getText());
                        int korisnik3 = Integer.parseInt(textIdKorisnik.getText());

                        kreirajArtikal(naziv3, opis, cena, popust, kategorija3, korisnik3);
                        break;

                    case "Menjanje cene artikla (IdArtikal,Iznos)":
                        int idartikla2 = Integer.parseInt(textIdArtikal.getText());
                        double iznos2 = Double.parseDouble(textIznos.getText());
                        promeniCenuArtikla(idartikla2, iznos2);
                        break;

                    case "Postavi popust (IdArtikal,Popust)":
                        int idartikla3 = Integer.parseInt(textIdArtikal.getText());
                        int iznos3 = Integer.parseInt(textPopust.getText());
                        postaviPopustArtikla(idartikla3, iznos3);
                        break;

                    case "Dohvati sve artikle za korisnika (IdKorisnik)":
                        int idkorisnik = Integer.parseInt(textIdKorisnik.getText());
                        String ret = dohvatiArtikleZaKorisnika(idkorisnik);
                        textArea.setText(ret);
                        break;

                    case "Dodaj artikal u korpu korisnika (IdKorisnik,IdArtikal,Iznos)":
                        int idkorisnik4 = Integer.parseInt(textIdKorisnik.getText());
                        int idartikal4 = Integer.parseInt(textIdArtikal.getText());
                        int kolicina4 = Integer.parseInt(textIznos.getText());
                        dodajArtikalUKorpu(idkorisnik4, idartikal4, kolicina4);

                        break;

                    case "Izbrisi artikal iz korpe korisnika (IdKorisnik,IdArtikal,Iznos)":
                        int idkorisnik5 = Integer.parseInt(textIdKorisnik.getText());
                        int idartikal5 = Integer.parseInt(textIdArtikal.getText());
                        int kolicina5 = Integer.parseInt(textIznos.getText());
                        izbrisiArtikalIzKorpe(idkorisnik5, idartikal5, kolicina5);
                        break;

                    case "Dohvati sadrzaj korpe korisnika (IdKorisnik)":
                        int idkorisnik6 = Integer.parseInt(textIdKorisnik.getText());
                        String mojakorpa = dohvatiSadrzajKorpeZaKorisnika(idkorisnik6);
                        textArea.setText(mojakorpa);
                        break;

                    case "Placanje (IdKorisnik)":
                        int idkorisnik7 = Integer.parseInt(textIdKorisnik.getText());
                        placanje(idkorisnik7);
                        break;
                    case "Dohvati sve narudzbine za korisnika (IdKorisnik)":
                        int idkorisnik8 = Integer.parseInt(textIdKorisnik.getText());
                        String mojeNarudzbine = dohvatiNarudzbineZaKorisnika(idkorisnik8);
                        textArea.setText(mojeNarudzbine);
                        break;
                    case "Dohvati sve narudzbine":
                        String sveNarudzbine = dohvatiNarudzbine();
                        textArea.setText(sveNarudzbine);
                        break;
                    case "Dohvati sve transakcije":
                        String sveTransakcije = dohvatiTransakcije();
                        textArea.setText(sveTransakcije);
                        break;

                }

                textIdGrada.setText("");
                textNaziv.setText("");
                textIdKorisnik.setText("");
                textKorisnickoIme.setText("");
                textSifra.setText("");
                textIme.setText("");
                textPrezime.setText("");
                textAdresa.setText("");
                textIznos.setText("");
                textIdKategorija.setText("");
                textKategorija.setText("");
                textNatkategorija.setText("");
                textOpis.setText("");
                textPopust.setText("");
                textIdArtikal.setText("");

            }
        });

    }

    public static void main(String[] args) {
        new Klijent();
    }

}
