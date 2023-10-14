/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "stavka")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stavka.findAll", query = "SELECT s FROM Stavka s"),
    @NamedQuery(name = "Stavka.findByIdStavka", query = "SELECT s FROM Stavka s WHERE s.idStavka = :idStavka"),
    @NamedQuery(name = "Stavka.findByIdArtikal", query = "SELECT s FROM Stavka s WHERE s.idArtikal = :idArtikal"),
    @NamedQuery(name = "Stavka.findByKolicina", query = "SELECT s FROM Stavka s WHERE s.kolicina = :kolicina"),
    @NamedQuery(name = "Stavka.findByIdKorpa", query = "SELECT s FROM Stavka s WHERE s.idKorpa = :idKorpa"),
    @NamedQuery(name = "Stavka.findByJedinicnaCena", query = "SELECT s FROM Stavka s WHERE s.jedinicnaCena = :jedinicnaCena")})
public class Stavka implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Kolicina")
    private int kolicina;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "JedinicnaCena")
    private BigDecimal jedinicnaCena;
    @JoinColumn(name = "IdArtikal", referencedColumnName = "IdArtikal")
    @ManyToOne(optional = false)
    private Artikal idArtikal;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdStavka")
    private Integer idStavka;
    @JoinColumn(name = "IdKorpa", referencedColumnName = "IdKorpa")
    @ManyToOne(optional = false)
    private Korpa idKorpa;

    public Stavka() {
    }

    public Stavka(Integer idStavka) {
        this.idStavka = idStavka;
    }

    public Stavka(Integer idStavka, int kolicina, BigDecimal jedinicnaCena) {
        this.idStavka = idStavka;
        this.kolicina = kolicina;
        this.jedinicnaCena = jedinicnaCena;
    }

    public Integer getIdStavka() {
        return idStavka;
    }

    public void setIdStavka(Integer idStavka) {
        this.idStavka = idStavka;
    }

    public Korpa getIdKorpa() {
        return idKorpa;
    }

    public void setIdKorpa(Korpa idKorpa) {
        this.idKorpa = idKorpa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idStavka != null ? idStavka.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stavka)) {
            return false;
        }
        Stavka other = (Stavka) object;
        if ((this.idStavka == null && other.idStavka != null) || (this.idStavka != null && !this.idStavka.equals(other.idStavka))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Stavka[ idStavka=" + idStavka + " ]";
    }


    public Artikal getIdArtikal() {
        return idArtikal;
    }

    public void setIdArtikal(Artikal idArtikal) {
        this.idArtikal = idArtikal;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public BigDecimal getJedinicnaCena() {
        return jedinicnaCena;
    }

    public void setJedinicnaCena(BigDecimal jedinicnaCena) {
        this.jedinicnaCena = jedinicnaCena;
    }
    
}
