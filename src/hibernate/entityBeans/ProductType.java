package hibernate.entityBeans;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Iterator;
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
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;

import util.EntityObject;

@Entity
@Table(name = "producttype", catalog = "diplomski_rad_db")
public class ProductType implements Serializable, EntityObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1392577294660849973L;
	
	private Integer ID, version;
	private String name, description;
//	private ProductGroup group;
	private Set<Product> products = null;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getID() {
		return ID;
	}
	
	public void setID(Integer iD) {
		ID = iD;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "name", unique = false, nullable = false)
	public String getName() {
		return name;
	}
	
	@Column(name = "description", unique = false, nullable = true)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Version
	public Integer getVersion() {
		return version;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	public ProductGroup getGroup() {
//		return group;
//	}
//	
//	public void setGroup(ProductGroup group) {
//		this.group = group;
//	}
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "type")
	@Cascade(value={org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REMOVE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<Product> getProducts() {
		return products;
	}
	
	public void setProducts(Set<Product> products) {
		try {
			if (products!=null && products.size()>0) {
				Iterator<Product> iter = products.iterator();
				while (iter.hasNext()) {
					iter.next().setType(this);
				}
			}
		} catch (Exception e) {
		}
		this.products = products;
	}
	
//	@PreRemove
//	public void onDelete(){
//		if(getGroup().getTypes().contains(this))
//			getGroup().getTypes().remove(this);
//	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof ProductType)
			return getID()==((ProductType)arg0).getID();
		return super.equals(arg0);
	}
}
