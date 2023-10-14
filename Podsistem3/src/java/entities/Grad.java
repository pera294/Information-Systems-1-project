/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "grad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grad.findAll", query = "SELECT g FROM Grad g"),
    @NamedQuery(name = "Grad.findByIdGrad", query = "SELECT g FROM Grad g WHERE g.idGrad = :idGrad"),
    @NamedQuery(name = "Grad.findByNaziv", query = "SELECT g FROM Grad g WHERE g.naziv = :naziv")})
public class Grad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdGrad")
    private Integer idGrad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;

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

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
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
    
}
