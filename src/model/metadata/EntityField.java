package model.metadata;

import java.io.Serializable;

public class EntityField implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5451944943998511879L;
	private String fieldName = "", fieldType = "", fieldLabel = "", siblingId="", databaseName="";
	private boolean displayList=true, popupDislpayList=true, hidden=false, required = true, isGui=false, editable = true, primaryKey=false, lookup=false, relField=false, presistent = false, searchable = false;
	public String getFieldName() {
		return fieldName;
	}
	public String getFieldLabel() {
		return fieldLabel;
	}
	
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public boolean isSearchable() {
		return searchable;
	}
	
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	public String getFieldType() {
		return fieldType;
	}
	
	public void setFieldRep(String fieldRep) {
		if(fieldRep.equals(FieldRepEnum.DISPLEY)){
			hidden = false;
			editable = false;
			required = false;
		}else if(fieldRep.equals(FieldRepEnum.OPTIONAL)){
			hidden = false;
			editable = true;
			required = false;
		}else if(fieldRep.equals(FieldRepEnum.REQUIRED)){
			hidden = false;
			editable = true;
			required = true;
		}else{
			hidden = true;
			editable = false;
			required = false;
		}
	}
	
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public boolean isRequired() {
		return required;
	}
	
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	
	public boolean isLookup() {
		return lookup;
	}
	
	public void setLookup(boolean lookup) {
		this.lookup = lookup;
	}
	
	public boolean isRelField() {
		return relField;
	}
	
	public void setRelField(boolean relField) {
		this.relField = relField;
	}
	
	public boolean isPresistent() {
		return presistent;
	}
	
	public void setPresistent(boolean presistent) {
		this.presistent = presistent;
	}
	
	public String getSiblingId() {
		return siblingId;
	}
	
	public void setSiblingId(String siblingId) {
		this.siblingId = siblingId;
	}
	
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	public boolean isGui() {
		return isGui;
	}
	public void setGui(boolean isGui) {
		this.isGui = isGui;
	}
	public void setDisplayList(boolean displayList) {
		this.displayList = displayList;
	}
	public boolean isDisplayList() {
		return displayList;
	}
	public void setPopupDislpayList(boolean popupDislpayList) {
		this.popupDislpayList = popupDislpayList;
	}
	public boolean isPopupDislpayList() {
		return popupDislpayList;
	}
}
