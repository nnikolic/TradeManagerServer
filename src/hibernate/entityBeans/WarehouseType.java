package hibernate.entityBeans;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import util.EntityObject;

@Entity
@Table(name = "warehousetype", catalog = "diplomski_rad_db")
public class WarehouseType implements Serializable, EntityObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6090468445110864920L;

	private Integer ID;
	private String name;
	
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
}
