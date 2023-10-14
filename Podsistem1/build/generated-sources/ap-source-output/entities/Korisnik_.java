package entities;

import entities.Grad;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.4.v20190115-rNA", date="2023-07-07T17:47:21")
@StaticMetamodel(Korisnik.class)
public class Korisnik_ { 

    public static volatile SingularAttribute<Korisnik, String> ime;
    public static volatile SingularAttribute<Korisnik, String> prezime;
    public static volatile SingularAttribute<Korisnik, BigDecimal> novac;
    public static volatile SingularAttribute<Korisnik, String> adresa;
    public static volatile SingularAttribute<Korisnik, String> sifra;
    public static volatile SingularAttribute<Korisnik, String> korisnickoIme;
    public static volatile SingularAttribute<Korisnik, Grad> grad;
    public static volatile SingularAttribute<Korisnik, Integer> idKorisnik;

}