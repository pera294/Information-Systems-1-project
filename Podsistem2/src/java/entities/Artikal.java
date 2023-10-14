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
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "artikal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Artikal.findAll", query = "SELECT a FROM Artikal a"),
    @NamedQuery(name = "Artikal.findByIdArtikal", query = "SELECT a FROM Artikal a WHERE a.idArtikal = :idArtikal"),
    @NamedQuery(name = "Artikal.findByNaziv", query = "SELECT a FROM Artikal a WHERE a.naziv = :naziv"),
    @NamedQuery(name = "Artikal.findByOpis", query = "SELECT a FROM Artikal a WHERE a.opis = :opis"),
    @NamedQuery(name = "Artikal.findByCena", query = "SELECT a FROM Artikal a WHERE a.cena = :cena"),
    @NamedQuery(name = "Artikal.findByPopust", query = "SELECT a FROM Artikal a WHERE a.popust = :popust"),
    @NamedQuery(name = "Artikal.findByIdKorisnik", query = "SELECT a FROM Artikal a WHERE a.korisnik = :korisnik"),})
public class Artikal implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;
    @Size(max = 45)
    @Column(name = "Opis")
    private String opis;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cena")
    private BigDecimal cena;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idArtikal")
    private List<Stavka> stavkaList;
    @Column(name = "Ocena")
    private Integer ocena;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdArtikal")
    private Integer idArtikal;
    @Column(name = "Popust")
    private Integer popust;
    @JoinColumn(name = "Kategorija", referencedColumnName = "IdKategorija")
    @ManyToOne
    private Kategorija kategorija;
    @JoinColumn(name = "Korisnik", referencedColumnName = "IdKorisnik")
    @ManyToOne
    private Korisnik korisnik;

    public Artikal() {
    }

    public Artikal(Integer idArtikal) {
        this.idArtikal = idArtikal;
    }

    public Artikal(Integer idArtikal, String naziv, BigDecimal cena) {
        this.idArtikal = idArtikal;
        this.naziv = naziv;
        this.cena = cena;
    }

    public Integer getIdArtikal() {
        return idArtikal;
    }

    public void setIdArtikal(Integer idArtikal) {
        this.idArtikal = idArtikal;
    }


    public Integer getPopust() {
        return popust;
    }

    public void setPopust(Integer popust) {
        this.popust = popust;
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
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
        hash += (idArtikal != null ? idArtikal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artikal)) {
            return false;
        }
        Artikal other = (Artikal) object;
        if ((this.idArtikal == null && other.idArtikal != null) || (this.idArtikal != null && !this.idArtikal.equals(other.idArtikal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Artikal[ idArtikal=" + idArtikal + " ]";
    }


    public Integer getOcena() {
        return ocena;
    }

    public void setOcena(Integer ocena) {
        this.ocena = ocena;
    }


    @XmlTransient
    public List<Stavka> getStavkaList() {
        return stavkaList;
    }

    public void setStavkaList(List<Stavka> stavkaList) {
        this.stavkaList = stavkaList;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }
    
}
