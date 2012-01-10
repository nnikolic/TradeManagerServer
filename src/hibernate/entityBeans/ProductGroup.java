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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;

import util.EntityObject;

@Entity
@Table(name = "productgroup", catalog = "diplomski_rad_db")
public class ProductGroup implements Serializable, EntityObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = -187902550764714826L;
	private Integer ID, version;
	private String name, description;
	private Set<Product> products; 
//	private ProductFamily family;
//	private Set<ProductType> types = null;
	
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
	
	public String getDescription() {
		return description;
	}
	
	@Column(name = "description", unique = false, nullable = false)
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
	
	//@JoinColumn(name="family_id")
//	@ManyToOne(fetch = FetchType.EAGER, optional=true)
//	public ProductFamily getFamily() {
//		return family;
//	}
//	
//	public void setFamily(ProductFamily family) {
//		this.family = family;
//	}
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
//	@Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
//	public Set<ProductType> getTypes() {
//		return types;
//	}
//	
//	public void setTypes(Set<ProductType> types) {
//		try {
//			if (types!=null && types.size()>0) {
//				Iterator<ProductType> iter = types.iterator();
//				while (iter.hasNext()) {
//					iter.next().setGroup(this);
//				}
//			}
//		} catch (Exception e) {
//		}
//		this.types = types;
//	}
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "group")
	@Cascade(value={org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REMOVE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<Product> getProducts() {
		return products;
	}
	
	public void setProducts(Set<Product> products) {
//		try {
//			if (products!=null && products.size()>0) {
//				Iterator<Product> iter = products.iterator();
//				while (iter.hasNext()) {
//					iter.next().setGroup(this);
//				}
//			}
//		} catch (Exception e) {
//		}
		this.products = products;
	}
	
//	@PreRemove
//	public void onDelete(){
//		if(getFamily().getGroups().contains(this))
//			getFamily().getGroups().remove(this);
//	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof ProductGroup)
			return getID()==((ProductGroup)arg0).getID();
		return super.equals(arg0);
	}
}
