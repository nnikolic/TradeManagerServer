package hibernate.entityBeans;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Version;

import util.EntityObject;

@Entity
@Table(name = "product", catalog = "diplomski_rad_db")
public class Product implements Serializable, EntityObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2260167226842426676L;

	private Integer ID, version;
	private String name, description, code;
	private ProductFamily family;
	private ProductGroup group;
	private ProductType type;
	private Double packageSize;
	private ProductCharacteristics characteristics;
	private MeasureUnit unit;
	
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
	
	@Column(name = "description", unique = false, nullable = true)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "package_size", nullable = false)
	public Double getPackageSize() {
		return packageSize;
	}
	
	public void setPackageSize(Double packageSize) {
		this.packageSize = packageSize;
	}
	
	@Column(name = "code", unique = true, nullable = false)
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	public ProductFamily getFamily() {
		return family;
	}
	
	public void setFamily(ProductFamily family) {
		this.family = family;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	public ProductGroup getGroup() {
		return group;
	}
	
	public void setGroup(ProductGroup group) {
		this.group = group;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	public ProductType getType() {
		return type;
	}
	
	public void setType(ProductType type) {
		this.type = type;
	}
	
	@Version
	public Integer getVersion() {
		return version;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@PreRemove
	public void onDelete(){
		if(getType().getProducts().contains(this))
			getType().getProducts().remove(this);
	}
	
	@OneToOne(cascade={}, fetch = FetchType.EAGER)
	public ProductCharacteristics getCharacteristics() {
		return characteristics;
	}
	
	public void setCharacteristics(ProductCharacteristics characteristics) {
		this.characteristics = characteristics;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch=FetchType.EAGER)
	public MeasureUnit getUnit() {
		return unit;
	}
	
	public void setUnit(MeasureUnit unit) {
		this.unit = unit;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof Product)
			return getID()==((Product)arg0).getID();
		return super.equals(arg0);
	}
}
