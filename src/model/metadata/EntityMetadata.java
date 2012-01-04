package model.metadata;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.dom.DeferredCommentImpl;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class EntityMetadata implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4195563891149261611L;

	private String xmlString;

	private List<EntityField> fields = null;
	private String entityName, entityClassPath, viewForm, insertForm, searchForm, detailPrintTemplate, listPrintTemplate;
	private List<ChildMetadata> childrenList = null;
	private List<SiblingMetadata> siblings = null;
	
	private boolean customGui=false, customDelete=false, printable = false, listPrintable = false, editable = true, addable = true, deletable = true;
	
	private int inputPanelsCount;
	
	public EntityMetadata(){
		fields = new ArrayList<EntityField>();
	}
	public EntityMetadata(String xmlString){
		fields = new ArrayList<EntityField>();
		childrenList = new ArrayList<ChildMetadata>();
		siblings = new ArrayList<SiblingMetadata>();
		this.xmlString = xmlString;
		parseXmlString();
	}
	
	public String getPrimKeyFieldName(){
		for(EntityField ef: fields){
			if(ef.isPrimaryKey()){
				return ef.getFieldName();
			}
		}
		return null;
	}
	
	public boolean isCustomDelete() {
		return customDelete;
	}
	public void setCustomDelete(boolean customDelete) {
		this.customDelete = customDelete;
	}
	
	public String getPrimKeyFieldDBName(){
		for(EntityField ef: fields){
			if(ef.isPrimaryKey()){
				return ef.getDatabaseName();
			}
		}
		return null;
	}
	
	public ChildMetadata getChildMetadata(String entityClassPath){
		for(ChildMetadata cm : childrenList){
			if(cm.getClassUrl().equals(entityClassPath))
				return cm;
		}
		return null;
	}
	
	public boolean hasSearchFields(){
		for(EntityField ef: fields){
			if(ef.isSearchable()){
				return true;
			}
		}
		return false;
	}
	
	public ChildMetadata getChildMetadataById(String id){
		for(ChildMetadata cm : childrenList){
			if(cm.getId().equals(id))
				return cm;
		}
		return null;
	}
	
	public String getSearchForm() {
		return searchForm;
	}
	
	public void setSearchForm(String searchForm) {
		this.searchForm = searchForm;
	}
	
	public List<ChildMetadata> getChildrenList() {
		return childrenList;
	}
	
	public List<EntityField> getFields() {
		return fields;
	}
	
	public String getEntityName() {
		return entityName;
	}
	
	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
		parseXmlString();
	}
	public String getEntityClassPath() {
		return entityClassPath;
	}
	
	public int getInputPanelsCount() {
		return inputPanelsCount;
	}
	
	public boolean isCustomGui() {
		return customGui;
	}
	
	public void setCustomGui(boolean customGui) {
		this.customGui = customGui;
	}
	
	public String getViewForm() {
		return viewForm;
	}
	
	public String getInsertForm() {
		return insertForm;
	}
	
	public boolean isListPrintable() {
		return listPrintable;
	}
	
	public void setListPrintable(boolean listPrintable) {
		this.listPrintable = listPrintable;
	}
	
	public boolean isPrintable() {
		return printable;
	}
	
	public void setPrintable(boolean printable) {
		this.printable = printable;
	}
	
	public boolean isAddable() {
		return addable;
	}
	
	public void setAddable(boolean addable) {
		this.addable = addable;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isDeletable() {
		return deletable;
	}
	
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}
	
	public String getListPrintTemplate() {
		return listPrintTemplate;
	}
	
	public void setListPrintTemplate(String listPrintTemplate) {
		this.listPrintTemplate = listPrintTemplate;
	}
	
	public String getDetailPrintTemplate() {
		return detailPrintTemplate;
	}
	
	public void setDetailPrintTemplate(String detailPrintTemplate) {
		this.detailPrintTemplate = detailPrintTemplate;
	}
	
	private void parseXmlString(){
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new StringReader(this.xmlString)));
			Document doc = parser.getDocument();
			Element root = doc.getDocumentElement(); 
			Element guiNode = null, persistenceNode=null;
			
			entityName = root.getAttribute("name");
			entityClassPath = root.getAttribute("class_path");
			inputPanelsCount = Integer.parseInt(root.getAttribute("col_count"));
			customDelete = new Boolean(root.getAttribute("custom_delete"));
			editable = new Boolean(root.getAttribute("editable") != "" ? root.getAttribute("editable") : "true");
			addable = new Boolean(root.getAttribute("addable") != "" ? root.getAttribute("addable") : "true");
			deletable = new Boolean(root.getAttribute("deletable") != "" ? root.getAttribute("deletable") : "true");
			printable = new Boolean(root.getAttribute("printable")!=null ? root.getAttribute("printable"): "false");
			listPrintable = new Boolean(root.getAttribute("list_printable")!=null ? root.getAttribute("list_printable"): "false");
			listPrintTemplate = root.getAttribute("list_print_template") != null ? root.getAttribute("list_print_template") : "";
			detailPrintTemplate = root.getAttribute("detail_print_template") != null ? root.getAttribute("detail_print_template") : "";
			if(root.getAttribute("custom_gui")!=""){
				String bool = root.getAttribute("custom_gui");
				customGui = new Boolean(bool);
				viewForm = root.getAttribute("view");
				insertForm = root.getAttribute("insert");
				searchForm = root.getAttribute("search");
			}
			for(int i=0; i<root.getChildNodes().getLength(); i++){
				if (root.getChildNodes().item(i).getNodeName().equals("gui") && guiNode==null) {
					guiNode = (Element) root.getChildNodes().item(i);
				}else if(root.getChildNodes().item(i).getNodeName().equals("persistence") && persistenceNode==null){
					persistenceNode = (Element) root.getChildNodes().item(i);
				}
			}
			
			if(guiNode!=null && persistenceNode!=null){
				NodeList persistenceList = persistenceNode.getElementsByTagName("field");
				NodeList siblingList = persistenceNode.getElementsByTagName("sibling");
				for(int i=0; i<persistenceList.getLength(); i++){
					Node n = persistenceList.item(i);
					if(!n.getNodeName().equals("#text")){
						EntityField ef = new EntityField();
						ef.setFieldName(((Element)n).getAttribute("name"));
						ef.setFieldType(((Element)n).getAttribute("type"));
						ef.setPrimaryKey(Boolean.parseBoolean(((Element)n).getAttribute("primaryKey")));
						ef.setPresistent(Boolean.parseBoolean(((Element)n).getAttribute("persistent")));
						ef.setLookup(Boolean.parseBoolean(((Element)n).getAttribute("lookup")));
						ef.setRelField(Boolean.parseBoolean(((Element)n).getAttribute("relfield")));
						ef.setDatabaseName(((Element)n).getAttribute("dbName"));
						ef.setSearchable(Boolean.parseBoolean(((Element)n).getAttribute("searchable")));
						if(ef.isRelField()){
							ef.setSiblingId(((Element)n).getAttribute("sibling_id"));
						}
						
						ef.setFieldLabel(entityName.toUpperCase()+"."+((Element)n).getAttribute("name").toUpperCase());
						fields.add(ef);
					}
				}
				
				for(int i=0; i<siblingList.getLength(); i++){
					Node n = siblingList.item(i);
					if(!n.getNodeName().equals("#text")){
						siblings.add(new SiblingMetadata((Element)n));
					}
				}
				String popupDisplayListStr = "", displayListStr = "";
				for(int i=0; i<guiNode.getChildNodes().getLength(); i++){
					Node n =guiNode.getChildNodes().item(i);
					EntityField ef = null;
					if(!n.getNodeName().equals("#text") && !(n instanceof DeferredCommentImpl)){
						ef = getFieldByName(((Element)n).getAttribute("name"));
						ef.setGui(true);
						ef.setFieldLabel(entityName.toUpperCase()+"."+((Element)n).getAttribute("name").toUpperCase());
						ef.setFieldRep(((Element)n).getAttribute("field-rep"));
						popupDisplayListStr = ((Element)n).getAttribute("popup-display-list") != null ? ((Element)n).getAttribute("popup-display-list") : "true" ;
						displayListStr = ((Element)n).getAttribute("display-list") != null ? ((Element)n).getAttribute("display-list") : "true" ;
						ef.setPopupDislpayList(new Boolean(popupDisplayListStr));
						ef.setDisplayList(new Boolean(displayListStr));
					}
				}
				childrenList = EntityMetadata.getChildren(persistenceNode);
			}
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public EntityField getFieldByName(String fieldName){
		for(EntityField ef: fields){
			if(ef.getFieldName().equals(fieldName))
				return ef;
		}
		return null;
	}
	
	public SiblingMetadata getSiblingByID(String siblingID){
		for(SiblingMetadata s: siblings){
			if(s.getId().equals(siblingID))
				return s;
		}
		return null;
	}

	public static ArrayList<ChildMetadata> getChildren(Element persistenceNode){
		ArrayList<ChildMetadata> result = new ArrayList<ChildMetadata>();
		NodeList nl = persistenceNode.getElementsByTagName("child");
		ChildMetadata cm = null;
		for(int i=0; i<nl.getLength(); i++){
			Node n = nl.item(i);
			if(!n.getNodeName().equals("#text")){
				cm = new ChildMetadata(((Element)n).getAttribute("file_name"), ((Element)n).getAttribute("name"), ((Element)n).getAttribute("class_path"), ((Element)n).getAttribute("id"), ((Element)n).getAttribute("relId"), ((Element)n).getAttribute("include_in_next_actions"));
				result.add(cm);
			}
		}
		return result;
	}
	
	private String getPerstistenceAttribute(String fildName, String attributeName, Element persistenceNode){
		for(int i=0; i<persistenceNode.getChildNodes().getLength(); i++){
			Node n = persistenceNode.getChildNodes().item(i);
			if(!n.getNodeName().equals("#text")){
				if(((Element)persistenceNode.getChildNodes().item(i)).getAttribute("name").equals(fildName)){
					return ((Element)persistenceNode.getChildNodes().item(i)).getAttribute(attributeName);
				}
			}
		}
		return null;
	}
}
