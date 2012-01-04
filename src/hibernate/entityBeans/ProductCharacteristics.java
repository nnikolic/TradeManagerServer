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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import util.EntityObject;

@Entity
@Table(name = "productcharacteristics", catalog = "diplomski_rad_db")
@OnDelete(action=OnDeleteAction.CASCADE)
public class ProductCharacteristics implements Serializable, EntityObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2214328614702694358L;
	
	private Integer ID;
	
	private String description;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getID() {
		return ID;
	}
	
	public void setID(Integer iD) {
		ID = iD;
	}
	
	@Column(name = "description", unique = false, nullable = true)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	@Transient
	public String getName() {
		return this.getClass().getName();
	}
}
