package entities;

import entities.Korisnik;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.4.v20190115-rNA", date="2023-07-07T17:47:21")
@StaticMetamodel(Grad.class)
public class Grad_ { 

    public static volatile SingularAttribute<Grad, String> naziv;
    public static volatile SingularAttribute<Grad, Integer> idGrad;
    public static volatile ListAttribute<Grad, Korisnik> korisnikList;

}