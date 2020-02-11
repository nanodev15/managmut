/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.persistence.Transient;

/**
 *
 * @author elommarcarnold
 */
@Entity
@Table(name = "loanrecautom", catalog = "mutuelle", schema = "")
@NamedQueries({
    @NamedQuery(name = "Loanrecautom.findAll", query = "SELECT l FROM Loanrecautom l"),
    @NamedQuery(name = "Loanrecautom.findByIdloanrecautom", query = "SELECT l FROM Loanrecautom l WHERE l.idloanrecautom = :idloanrecautom"),
    @NamedQuery(name = "Loanrecautom.findByFirstpaydate", query = "SELECT l FROM Loanrecautom l WHERE l.firstpaydate = :firstpaydate"),
    @NamedQuery(name = "Loanrecautom.findByFrequency", query = "SELECT l FROM Loanrecautom l WHERE l.frequency = :frequency"),
    @NamedQuery(name = "Loanrecautom.findByNbfreq", query = "SELECT l FROM Loanrecautom l WHERE l.nbfreq = :nbfreq"),
    @NamedQuery(name = "Loanrecautom.findByMonthlypayment", query = "SELECT l FROM Loanrecautom l WHERE l.monthlypayment = :monthlypayment"),
    @NamedQuery(name = "Loanrecautom.findByNominalrate", query = "SELECT l FROM Loanrecautom l WHERE l.nominalrate = :nominalrate"),
    @NamedQuery(name = "Loanrecautom.findByLoanref", query = "SELECT l FROM Loanrecautom l WHERE l.loanref = :loanref"),
    @NamedQuery(name = "Loanrecautom.findByDatecreation", query = "SELECT l FROM Loanrecautom l WHERE l.datecreation = :datecreation"),
    @NamedQuery(name = "Loanrecautom.findByStatut", query = "SELECT l FROM Loanrecautom l WHERE l.statut = :statut")})
public class Loanrecautom implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idloanrecautom")
    private Integer idloanrecautom;
    @Column(name = "firstpaydate")
    @Temporal(TemporalType.DATE)
    private Date firstpaydate;
    @Column(name = "frequency")
    private Integer frequency;
    @Column(name = "nbfreq")
    private Integer nbfreq;
    @Column(name = "monthlypayment")
    private BigInteger monthlypayment;
    @Column(name = "nominalrate")
    private BigInteger nominalrate;
    @Column(name = "loanref")
    private String loanref;
    @Column(name = "Datecreation")
    @Temporal(TemporalType.DATE)
    private Date datecreation;
    @Column(name = "Statut")
    private String statut;

    public Loanrecautom() {
    }

    public Loanrecautom(Integer idloanrecautom) {
        this.idloanrecautom = idloanrecautom;
    }

    public Integer getIdloanrecautom() {
        return idloanrecautom;
    }

    public void setIdloanrecautom(Integer idloanrecautom) {
        Integer oldIdloanrecautom = this.idloanrecautom;
        this.idloanrecautom = idloanrecautom;
        changeSupport.firePropertyChange("idloanrecautom", oldIdloanrecautom, idloanrecautom);
    }

    public Date getFirstpaydate() {
        return firstpaydate;
    }

    public void setFirstpaydate(Date firstpaydate) {
        Date oldFirstpaydate = this.firstpaydate;
        this.firstpaydate = firstpaydate;
        changeSupport.firePropertyChange("firstpaydate", oldFirstpaydate, firstpaydate);
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        Integer oldFrequency = this.frequency;
        this.frequency = frequency;
        changeSupport.firePropertyChange("frequency", oldFrequency, frequency);
    }

    public Integer getNbfreq() {
        return nbfreq;
    }

    public void setNbfreq(Integer nbfreq) {
        Integer oldNbfreq = this.nbfreq;
        this.nbfreq = nbfreq;
        changeSupport.firePropertyChange("nbfreq", oldNbfreq, nbfreq);
    }

    public BigInteger getMonthlypayment() {
        return monthlypayment;
    }

    public void setMonthlypayment(BigInteger monthlypayment) {
        BigInteger oldMonthlypayment = this.monthlypayment;
        this.monthlypayment = monthlypayment;
        changeSupport.firePropertyChange("monthlypayment", oldMonthlypayment, monthlypayment);
    }

    public BigInteger getNominalrate() {
        return nominalrate;
    }

    public void setNominalrate(BigInteger nominalrate) {
        BigInteger oldNominalrate = this.nominalrate;
        this.nominalrate = nominalrate;
        changeSupport.firePropertyChange("nominalrate", oldNominalrate, nominalrate);
    }

    public String getLoanref() {
        return loanref;
    }

    public void setLoanref(String loanref) {
        String oldLoanref = this.loanref;
        this.loanref = loanref;
        changeSupport.firePropertyChange("loanref", oldLoanref, loanref);
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        Date oldDatecreation = this.datecreation;
        this.datecreation = datecreation;
        changeSupport.firePropertyChange("datecreation", oldDatecreation, datecreation);
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        String oldStatut = this.statut;
        this.statut = statut;
        changeSupport.firePropertyChange("statut", oldStatut, statut);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idloanrecautom != null ? idloanrecautom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Loanrecautom)) {
            return false;
        }
        Loanrecautom other = (Loanrecautom) object;
        if ((this.idloanrecautom == null && other.idloanrecautom != null) || (this.idloanrecautom != null && !this.idloanrecautom.equals(other.idloanrecautom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nehemie_mutuelle.Loanrecautom[ idloanrecautom=" + idloanrecautom + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
