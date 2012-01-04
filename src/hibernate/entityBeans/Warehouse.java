package hibernate.entityBeans;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import util.EntityObject;

@Entity
@Table(name = "warehouse", catalog = "diplomski_rad_db")
public class Warehouse implements Serializable, EntityObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2420197927317630775L;

	private Integer ID;
	private String name;
	
	private WarehouseType type;
	
	private Set<Supplies> supplies = null;
	
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
	
	public void setType(WarehouseType type) {
		this.type = type;
	}
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},fetch = FetchType.EAGER)
	public WarehouseType getType() {
		return type;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<Supplies> getSupplies() {
		return supplies;
	}
	
	public void setSupplies(Set<Supplies> supplies) {
		this.supplies = supplies;
	}
	
	@Transient
	public Supplies getSuppliesByPriuctID(int productID){
		for(Supplies sp: supplies){
			if(sp.getProduct().getID().intValue()==productID){
				return sp;
			}
		}
		return null;
	}
}
