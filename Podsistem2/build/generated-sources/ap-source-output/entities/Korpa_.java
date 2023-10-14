package entities;

import entities.Korisnik;
import entities.Stavka;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.4.v20190115-rNA", date="2023-07-08T16:37:55")
@StaticMetamodel(Korpa.class)
public class Korpa_ { 

    public static volatile SingularAttribute<Korpa, Integer> idKorpa;
    public static volatile SingularAttribute<Korpa, BigDecimal> ukupnaCena;
    public static volatile ListAttribute<Korpa, Stavka> stavkaList;
    public static volatile SingularAttribute<Korpa, Korisnik> idKorisnik;

}