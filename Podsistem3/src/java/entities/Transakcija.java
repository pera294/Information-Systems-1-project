/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "transakcija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t"),
    @NamedQuery(name = "Transakcija.findByIdTransakcija", query = "SELECT t FROM Transakcija t WHERE t.idTransakcija = :idTransakcija"),
    @NamedQuery(name = "Transakcija.findByIdNaruzbina", query = "SELECT t FROM Transakcija t WHERE t.idNaruzbina = :idNaruzbina"),
    @NamedQuery(name = "Transakcija.findBySuma", query = "SELECT t FROM Transakcija t WHERE t.suma = :suma"),
    @NamedQuery(name = "Transakcija.findByVreme", query = "SELECT t FROM Transakcija t WHERE t.vreme = :vreme")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdTransakcija")
    private Integer idTransakcija;
    @Column(name = "IdNaruzbina")
    private Integer idNaruzbina;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Suma")
    private BigDecimal suma;
    @Column(name = "Vreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vreme;

    public Transakcija() {
    }

    public Transakcija(Integer idTransakcija) {
        this.idTransakcija = idTransakcija;
    }

    public Integer getIdTransakcija() {
        return idTransakcija;
    }

    public void setIdTransakcija(Integer idTransakcija) {
        this.idTransakcija = idTransakcija;
    }

    public Integer getIdNaruzbina() {
        return idNaruzbina;
    }

    public void setIdNaruzbina(Integer idNaruzbina) {
        this.idNaruzbina = idNaruzbina;
    }

    public BigDecimal getSuma() {
        return suma;
    }

    public void setSuma(BigDecimal suma) {
        this.suma = suma;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransakcija != null ? idTransakcija.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.idTransakcija == null && other.idTransakcija != null) || (this.idTransakcija != null && !this.idTransakcija.equals(other.idTransakcija))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Transakcija[ idTransakcija=" + idTransakcija + " ]";
    }
    
}
