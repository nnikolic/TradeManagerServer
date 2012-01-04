package hibernate.entityBeans;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import util.EntityObject;
@Entity
@Table(name = "stockdocumentpayment", catalog = "diplomski_rad_db")
public class StockDocumentPayment implements Serializable, EntityObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -739214641449106584L;
	private Integer ID;
	private Double amount;
	private Date paymentDate;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getID() {
		return ID;
	}
	
	public void setID(Integer iD) {
		ID = iD;
	}
	
	@Column(name = "amount", unique = false, nullable = false)
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Transient
	public String getName() {
		return "";
	}

	@Column(name = "paymentdate", unique = false, nullable = false)
	public Date getPaymentDate() {
		return paymentDate;
	}
	
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
}
