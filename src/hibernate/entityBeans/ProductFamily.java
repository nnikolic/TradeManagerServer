package hibernate.entityBeans;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
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
@Table(name = "productfamily", catalog = "diplomski_rad_db")
public class ProductFamily implements Serializable, EntityObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3922493462098121908L;
	private Integer ID, version;
	private String name, description;
	private Set<Product> products = null;
//	private Set<ProductGroup> groups = null;
	
	public ProductFamily(){
//		groups = new HashSet<ProductGroup>();
		products = new HashSet<Product>();
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getID() {
		return ID;
	}
	
	public void setID(Integer id) {
		this.ID = id;
	}
	
	@Column(name = "description", unique = false, nullable = true)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String familyDescription) {
		this.description = familyDescription;
	}
	
	public void setName(String familyName) {
		this.name = familyName;
	}
	
	@Version
	public Integer getVersion() {
		return version;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name = "name", unique = false, nullable = false)
	public String getName() {
		return name;
	}
//, mappedBy = "family"
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "family")
//	@Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
//	public Set<ProductGroup> getGroups() {
//		return groups;
//	}
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "family")
	@Cascade(value={org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REMOVE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<Product> getProducts() {
		return products;
	}
	
//	public void setGroups(Set<ProductGroup> groups) {
//		try {
//			if (groups!=null && groups.size()>0) {
//				Iterator<ProductGroup> iter = groups.iterator();
//				while (iter.hasNext()) {
//					iter.next().setFamily(this);
//				}
//			}
//		} catch (Exception e) {
//		}
//		this.groups = groups;
//	}
	
	public void setProducts(Set<Product> products) {
		try {
			if (products!=null && products.size()>0) {
				Iterator<Product> iter = products.iterator();
				while (iter.hasNext()) {
					iter.next().setFamily(this);
				}
			}
		} catch (Exception e) {
		}
		this.products = products;
	}
}
