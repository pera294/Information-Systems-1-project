package entities;

import entities.Artikal;
import entities.Korpa;
import entities.Narudzbina;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.4.v20190115-rNA", date="2023-07-09T14:43:26")
@StaticMetamodel(Korisnik.class)
public class Korisnik_ { 

    public static volatile SingularAttribute<Korisnik, String> ime;
    public static volatile SingularAttribute<Korisnik, String> prezime;
    public static volatile ListAttribute<Korisnik, Korpa> korpaList;
    public static volatile SingularAttribute<Korisnik, BigDecimal> novac;
    public static volatile ListAttribute<Korisnik, Narudzbina> narudzbinaList;
    public static volatile SingularAttribute<Korisnik, String> adresa;
    public static volatile SingularAttribute<Korisnik, String> sifra;
    public static volatile SingularAttribute<Korisnik, String> korisnickoIme;
    public static volatile SingularAttribute<Korisnik, Integer> grad;
    public static volatile ListAttribute<Korisnik, Artikal> artikalList;
    public static volatile SingularAttribute<Korisnik, Integer> idKorisnik;

}