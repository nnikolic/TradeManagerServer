package hibernate.entityBeans;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import util.EntityObject;

@Entity
@Table(name = "salesprice", catalog = "diplomski_rad_db")
public class SalesPrice implements Serializable, EntityObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4366609772932733601L;

	private Integer ID, baseID;
	
	private String name;
	
	private Boolean oppened = true;
	
	private Date validFrom, validTo;
	
	private boolean dirty;
	
	private Set<SalesPriceItem> salesPriceItems = null;
	
	public SalesPrice(){
		salesPriceItems = new HashSet<SalesPriceItem>();
		dirty = false;
	}
	
	@Transient
	public boolean isDirty() {
		return dirty;
	}
	
	@Transient
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getID() {
		return ID;
	}
	
	public void setID(Integer iD) {
		ID = iD;
	}

	@Column(name = "name", unique = false, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="salespriceid")
	public Set<SalesPriceItem> getSalesPriceItems() {
		return salesPriceItems;
	}
	
	public void setSalesPriceItems(Set<SalesPriceItem> salesPriceItems) {
		this.salesPriceItems = salesPriceItems;
	}
	
	@Column(name = "validfrom", unique = false, nullable = false)
	public Date getValidFrom() {
		return validFrom;
	}
	
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	
	@Column(name = "validto", unique = false, nullable = true)
	public Date getValidTo() {
		return validTo;
	}
	
	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	
	@Column(name = "opened", unique = false, nullable = false)
	public Boolean getOppened() {
		return oppened;
	}
	
	public void setOppened(Boolean oppened) {
		this.oppened = oppened;
	}
	
	@Column(name = "baseid", unique = false, nullable = true)
	public Integer getBaseID() {
		return baseID;
	}
	
	public void setBaseID(Integer baseID) {
		this.baseID = baseID;
	}
}
