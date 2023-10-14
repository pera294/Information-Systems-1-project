package entities;

import entities.Artikal;
import entities.Kategorija;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.4.v20190115-rNA", date="2023-07-08T01:46:54")
@StaticMetamodel(Kategorija.class)
public class Kategorija_ { 

    public static volatile ListAttribute<Kategorija, Kategorija> kategorijaList;
    public static volatile SingularAttribute<Kategorija, String> naziv;
    public static volatile ListAttribute<Kategorija, Artikal> artikalList;
    public static volatile SingularAttribute<Kategorija, Kategorija> natkategorija;
    public static volatile SingularAttribute<Kategorija, Integer> idKategorija;

}