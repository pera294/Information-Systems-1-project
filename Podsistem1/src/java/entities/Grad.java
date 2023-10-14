/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "grad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grad.findAll", query = "SELECT g FROM Grad g"),
    @NamedQuery(name = "Grad.findByIdGrad", query = "SELECT g FROM Grad g WHERE g.idGrad = :IdGrad"),
    @NamedQuery(name = "Grad.findByNaziv", query = "SELECT g FROM Grad g WHERE g.naziv = :Naziv")})
public class Grad implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdGrad")
    private Integer idGrad;
    @OneToMany(mappedBy = "grad")
    private List<Korisnik> korisnikList;

    public Grad() {
    }

    public Grad(Integer idGrad) {
        this.idGrad = idGrad;
    }

    public Grad(Integer idGrad, String naziv) {
        this.idGrad = idGrad;
        this.naziv = naziv;
    }

    public Integer getIdGrad() {
        return idGrad;
    }

    public void setIdGrad(Integer idGrad) {
        this.idGrad = idGrad;
    }


    public List<Korisnik> getKorisnikList() {
        return korisnikList;
    }

    public void setKorisnikList(List<Korisnik> korisnikList) {
        this.korisnikList = korisnikList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGrad != null ? idGrad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grad)) {
            return false;
        }
        Grad other = (Grad) object;
        if ((this.idGrad == null && other.idGrad != null) || (this.idGrad != null && !this.idGrad.equals(other.idGrad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Grad[ idGrad=" + idGrad + " ]";
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
}
