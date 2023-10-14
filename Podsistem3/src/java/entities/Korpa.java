/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "korpa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Korpa.findAll", query = "SELECT k FROM Korpa k"),
    @NamedQuery(name = "Korpa.findByIdKorpa", query = "SELECT k FROM Korpa k WHERE k.idKorpa = :idKorpa"),
    @NamedQuery(name = "Korpa.findByUkupnaCena", query = "SELECT k FROM Korpa k WHERE k.ukupnaCena = :ukupnaCena")})
public class Korpa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdKorpa")
    private Integer idKorpa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "UkupnaCena")
    private BigDecimal ukupnaCena;
    @OneToMany(mappedBy = "korpa")
    private List<Stavka> stavkaList;
    @JoinColumns({
        @JoinColumn(name = "IdKorisnik", referencedColumnName = "IdKorisnik"),
        @JoinColumn(name = "IdKorisnik", referencedColumnName = "IdKorisnik")})
    @ManyToOne(optional = false)
    private Korisnik korisnik;

    public Korpa() {
    }

    public Korpa(Integer idKorpa) {
        this.idKorpa = idKorpa;
    }

    public Korpa(Integer idKorpa, BigDecimal ukupnaCena) {
        this.idKorpa = idKorpa;
        this.ukupnaCena = ukupnaCena;
    }

    public Integer getIdKorpa() {
        return idKorpa;
    }

    public void setIdKorpa(Integer idKorpa) {
        this.idKorpa = idKorpa;
    }

    public BigDecimal getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(BigDecimal ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    @XmlTransient
    public List<Stavka> getStavkaList() {
        return stavkaList;
    }

    public void setStavkaList(List<Stavka> stavkaList) {
        this.stavkaList = stavkaList;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKorpa != null ? idKorpa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Korpa)) {
            return false;
        }
        Korpa other = (Korpa) object;
        if ((this.idKorpa == null && other.idKorpa != null) || (this.idKorpa != null && !this.idKorpa.equals(other.idKorpa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Korpa[ idKorpa=" + idKorpa + " ]";
    }
    
}
