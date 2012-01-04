package model.metadata;

import java.io.Serializable;

public class ChildMetadata implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5619087982903515599L;
	private String fileName, entityName, classUrl, id, relId;
	private boolean includeInNext;
	
	public ChildMetadata(){
	}

	public ChildMetadata(String fileName, String entityName, String classUrl, String id, String relId, String includeInNextStr) {
		super();
		this.fileName = fileName;
		this.entityName = entityName;
		this.classUrl = classUrl;
		this.id = id;
		this.relId = relId;
		this.includeInNext = includeInNextStr.equals("") ? true: Boolean.parseBoolean(includeInNextStr);
	}
	
	public void setIncludeInNext(boolean includeInNext) {
		this.includeInNext = includeInNext;
	}
	
	public boolean isIncludeInNext() {
		return includeInNext;
	}
	
	public String getRelId() {
		return relId;
	}
	
	public void setRelId(String relId) {
		this.relId = relId;
	}
	
	public String getClassUrl() {
		return classUrl;
	}
	
	public String getEntityName() {
		return entityName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getId() {
		return id;
	}
}
