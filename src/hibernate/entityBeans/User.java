package hibernate.entityBeans;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import util.EntityObject;

@Entity
@Table(name = "user", catalog = "diplomski_rad_db")
public class User implements Serializable, EntityObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2664101936138993603L;
	
	private Integer ID, type;
	private String firstName, lastName, username, password;
	
	/*
	 * type:
	 * 	1 - administrator
	 * 	2 - uprava
	 *  ...
	 */

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getID() {
		return ID;
	}
	
	@Transient
	public String getName() {
		return getFirstName()+" "+getLastName();
	}

	@Column(name = "first_name", unique = false, nullable = true)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name = "last_name", unique = false, nullable = true)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "username", unique = true, nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", unique = false, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "type", unique = false, nullable = false)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setID(Integer iD) {
		ID = iD;
	}	
}
