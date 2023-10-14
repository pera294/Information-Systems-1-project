package entities;

import entities.Artikal;
import entities.Korpa;
import entities.Narudzbina;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.4.v20190115-rNA", date="2023-07-09T16:44:39")
@StaticMetamodel(Stavka.class)
public class Stavka_ { 

    public static volatile SingularAttribute<Stavka, BigDecimal> jedinicnaCena;
    public static volatile SingularAttribute<Stavka, Korpa> korpa;
    public static volatile SingularAttribute<Stavka, Artikal> artikal;
    public static volatile SingularAttribute<Stavka, Integer> kolicina;
    public static volatile SingularAttribute<Stavka, Integer> idStavka;
    public static volatile SingularAttribute<Stavka, Narudzbina> idNarudzbina;

}