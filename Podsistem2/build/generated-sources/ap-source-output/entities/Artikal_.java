package entities;

import entities.Kategorija;
import entities.Korisnik;
import entities.Stavka;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.4.v20190115-rNA", date="2023-07-08T18:15:37")
@StaticMetamodel(Artikal.class)
public class Artikal_ { 

    public static volatile SingularAttribute<Artikal, String> naziv;
    public static volatile ListAttribute<Artikal, Stavka> stavkaList;
    public static volatile SingularAttribute<Artikal, Integer> popust;
    public static volatile SingularAttribute<Artikal, BigDecimal> cena;
    public static volatile SingularAttribute<Artikal, Integer> idArtikal;
    public static volatile SingularAttribute<Artikal, Integer> ocena;
    public static volatile SingularAttribute<Artikal, String> opis;
    public static volatile SingularAttribute<Artikal, Kategorija> kategorija;
    public static volatile SingularAttribute<Artikal, Korisnik> korisnik;

}