package hibernate.entityBeans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import util.EntityObject;

@Entity
@Table(name = "documenttype", catalog = "diplomski_rad_db")
public class DocumentType implements Serializable, EntityObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6344124209389621000L;

	private Integer ID;
	
	//1 - ulaz, -1 izlaz, 0 - unutrasnji prenos
	private Integer stockDirection;
	
	private String name;
	
	@Id
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
	
	public void setStockDirection(Integer stockDirection) {
		this.stockDirection = stockDirection;
	}
	
	@Column(name = "stockdirection", unique = false, nullable = false)
	public Integer getStockDirection() {
		return stockDirection;
	}
	
	@Override
	@Transient
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null || getID() == null || ((DocumentType)obj).getID()==null){
			return super.equals(obj);
		}
		return getID().intValue() == ((DocumentType)obj).getID().intValue();
	}
}
