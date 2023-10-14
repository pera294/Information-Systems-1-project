package entities;

import entities.Korisnik;
import entities.Stavka;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.4.v20190115-rNA", date="2023-07-09T14:43:26")
@StaticMetamodel(Narudzbina.class)
public class Narudzbina_ { 

    public static volatile SingularAttribute<Narudzbina, BigDecimal> ukupnaCena;
    public static volatile SingularAttribute<Narudzbina, Date> vreme;
    public static volatile SingularAttribute<Narudzbina, String> adresa;
    public static volatile ListAttribute<Narudzbina, Stavka> stavkaList;
    public static volatile SingularAttribute<Narudzbina, Integer> idNarudzbina;
    public static volatile SingularAttribute<Narudzbina, Integer> grad;
    public static volatile SingularAttribute<Narudzbina, Korisnik> idKorisnik;

}