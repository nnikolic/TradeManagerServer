package model.metadata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SiblingMetadata implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7934750308817900464L;
	private String  siblingName, siblingClassUrl, id, siblingEntityFilename;
	private Map<String, String> relMap = null;
	
	public SiblingMetadata(Element root){
		relMap = new HashMap<String, String>();
		parseXml(root);
	}
	
	public String getRealFieldName(String key){
		return relMap.get(key);
	}
	
	public void parseXml(Element root){
		relMap.clear();
		
		id = root.getAttribute("id");
		siblingName = root.getAttribute("name");
		siblingClassUrl = root.getAttribute("class_path");
		siblingEntityFilename = root.getAttribute("file_name");
		NodeList list = root.getElementsByTagName("fildrel");
		for(int i=0; i<list.getLength(); i++){
			Node n = list.item(i);

			if(!n.getNodeName().equals("#text")){
				relMap.put(((Element)n).getAttribute("name"), ((Element)n).getAttribute("basename"));
			}
		}
		
	}
	
	public String getSiblingEntityFilename() {
		return siblingEntityFilename;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setSiblingClassUrl(String siblingClassUrl) {
		this.siblingClassUrl = siblingClassUrl;
	}
	
	public String getSiblingClassUrl() {
		return siblingClassUrl;
	}
	
	public void setSiblingName(String siblingName) {
		this.siblingName = siblingName;
	}
	
	public String getSiblingName() {
		return siblingName;
	}
}
