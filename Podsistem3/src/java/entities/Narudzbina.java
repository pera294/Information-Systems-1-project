/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "narudzbina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Narudzbina.findAll", query = "SELECT n FROM Narudzbina n"),
    @NamedQuery(name = "Narudzbina.findByIdNarudzbina", query = "SELECT n FROM Narudzbina n WHERE n.idNarudzbina = :idNarudzbina"),
    @NamedQuery(name = "Narudzbina.findByUkupnaCena", query = "SELECT n FROM Narudzbina n WHERE n.ukupnaCena = :ukupnaCena"),
    @NamedQuery(name = "Narudzbina.findByVreme", query = "SELECT n FROM Narudzbina n WHERE n.vreme = :vreme"),
    @NamedQuery(name = "Narudzbina.findByGrad", query = "SELECT n FROM Narudzbina n WHERE n.grad = :grad"),
    @NamedQuery(name = "Narudzbina.findByIdKorisnik", query = "SELECT n FROM Narudzbina n WHERE n.idKorisnik = :idKorisnik"),
    @NamedQuery(name = "Narudzbina.findByAdresa", query = "SELECT n FROM Narudzbina n WHERE n.adresa = :adresa")})
public class Narudzbina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdNarudzbina")
    private Integer idNarudzbina;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "UkupnaCena")
    private BigDecimal ukupnaCena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Vreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Grad")
    private int grad;
    @Size(max = 45)
    @Column(name = "Adresa")
    private String adresa;
    @OneToMany(mappedBy = "idNarudzbina")
    private List<Stavka> stavkaList;
    @JoinColumn(name = "IdKorisnik", referencedColumnName = "IdKorisnik")
    @ManyToOne(optional = false)
    private Korisnik idKorisnik;

    public Narudzbina() {
    }

    public Narudzbina(Integer idNarudzbina) {
        this.idNarudzbina = idNarudzbina;
    }

    public Narudzbina(Integer idNarudzbina, BigDecimal ukupnaCena, Date vreme, int grad) {
        this.idNarudzbina = idNarudzbina;
        this.ukupnaCena = ukupnaCena;
        this.vreme = vreme;
        this.grad = grad;
    }

    public Integer getIdNarudzbina() {
        return idNarudzbina;
    }

    public void setIdNarudzbina(Integer idNarudzbina) {
        this.idNarudzbina = idNarudzbina;
    }

    public BigDecimal getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(BigDecimal ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    public int getGrad() {
        return grad;
    }

    public void setGrad(int grad) {
        this.grad = grad;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    @XmlTransient
    public List<Stavka> getStavkaList() {
        return stavkaList;
    }

    public void setStavkaList(List<Stavka> stavkaList) {
        this.stavkaList = stavkaList;
    }

    public Korisnik getIdKorisnik() {
        return idKorisnik;
    }

    public void setIdKorisnik(Korisnik idKorisnik) {
        this.idKorisnik = idKorisnik;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNarudzbina != null ? idNarudzbina.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Narudzbina)) {
            return false;
        }
        Narudzbina other = (Narudzbina) object;
        if ((this.idNarudzbina == null && other.idNarudzbina != null) || (this.idNarudzbina != null && !this.idNarudzbina.equals(other.idNarudzbina))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Narudzbina[ idNarudzbina=" + idNarudzbina + " ]";
    }
    
}
