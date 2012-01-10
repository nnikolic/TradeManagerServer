package hibernate.entityBeans;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import util.EntityObject;

@Entity
@Table(name = "stockdocument", catalog = "diplomski_rad_db")
public class StockDocument implements Serializable, EntityObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1125770939330604435L;

	private Integer ID;
	
	private String note, documentCode;
	
	private Warehouse warehouseIn, warehouseOut;
	
	private CompanyCode companyCode;
	
	private SalesPrice salesPrice;
	
	private DocumentType documentType;
	
	private BusinessPartner businessPartner;
	
	private Date creationTime, paymentDay;
	
	private Set<StockDocumentItem> items = null;
	
	private Set<StockDocumentPayment> payments = null;
	
	private Boolean canceled = Boolean.FALSE;
	
	public StockDocument(){
		items = new HashSet<StockDocumentItem>();
		payments = new HashSet<StockDocumentPayment>();
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getID() {
		return ID;
	}
	
	public void setID(Integer iD) {
		ID = iD;
	}
	
	@Column(name="document_code", unique=true, nullable=false, length=50)
	public String getDocumentCode() {
		return documentCode;
	}
	
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}
	
	@Column(name="note", nullable=true)
	@Lob
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.EAGER)
	public Warehouse getWarehouseIn() {
		return warehouseIn;
	}
	
	public void setWarehouseIn(Warehouse warehouseIn) {
		this.warehouseIn = warehouseIn;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.EAGER)
	public Warehouse getWarehouseOut() {
		return warehouseOut;
	}
	
	public void setWarehouseOut(Warehouse warehouseOut) {
		this.warehouseOut = warehouseOut;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.EAGER)
	public CompanyCode getCompanyCode() {
		return companyCode;
	}
	
	public void setCompanyCode(CompanyCode companyCode) {
		this.companyCode = companyCode;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.EAGER)
	public SalesPrice getSalesPrice() {
		return salesPrice;
	}
	
	public void setSalesPrice(SalesPrice salesPrice) {
		this.salesPrice = salesPrice;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.EAGER)
	public DocumentType getDocumentType() {
		return documentType;
	}
	
	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.EAGER)
	public BusinessPartner getBusinessPartner() {
		return businessPartner;
	}
	
	public void setBusinessPartner(BusinessPartner businessPartner) {
		this.businessPartner = businessPartner;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<StockDocumentItem> getItems() {
		return items;
	}
	
	public void setItems(Set<StockDocumentItem> items) {
		this.items = items;
	}
	
	public Date getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
	public Date getPaymentDay() {
		return paymentDay;
	}
	
	public void setPaymentDay(Date paymentDay) {
		this.paymentDay = paymentDay;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<StockDocumentPayment> getPayments() {
		return payments;
	}
	
	public void setPayments(Set<StockDocumentPayment> payments) {
		this.payments = payments;
	}
	
	@Column(name = "canceled", nullable = false)
	public Boolean getCanceled() {
		return canceled;
	}
	
	public void setCanceled(Boolean canceled) {
		this.canceled = canceled;
	}
	
	@Transient
	public BigDecimal getTotalPrice(){
		BigDecimal price = new BigDecimal("0.00");
		price.setScale(2, BigDecimal.ROUND_CEILING);
		
		for(StockDocumentItem si: getItems()){
			price = price.add(new BigDecimal((si.getBasicPrice() + si.getPdvPrice())));
		}
		
		return price;
	}
	
	@Transient
	public BigDecimal getTotalPayments(){
		BigDecimal totalPayments = new BigDecimal("0.00");
		totalPayments.setScale(2, BigDecimal.ROUND_CEILING);
		
		if(getPayments()!=null){
			for(StockDocumentPayment pay: getPayments()){
				totalPayments = totalPayments.add(new BigDecimal(pay.getAmount()));
			}
		}
		
		return totalPayments;
	}
	
	@Transient
	public Boolean getPaid() {
		BigDecimal diff = new BigDecimal("0.00");
		diff.setScale(2, BigDecimal.ROUND_CEILING);
		diff = getTotalPrice().subtract(getTotalPayments());
		return Math.abs(diff.doubleValue()) <= 1;
	}
	
	@Override
	@Transient
	public String getName() {
		return "";
	}
}
