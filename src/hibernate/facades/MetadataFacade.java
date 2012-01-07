package hibernate.facades;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;

import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import util.ServerResponse;

import hibernate.remotes.MetadataFacadeRemote;
import hibernate.sessionBeans.LogUtil;

@Stateless
public class MetadataFacade implements MetadataFacadeRemote {

	public static String METADATA_URL="C:\\Users\\Nenad\\Documents\\Raf\\Java\\Diplomski\\TradeManagerMetadata\\src\\";
//	public static String METADATA_URL="C:\\Users\\Milan\\workspace\\TradeManagerMetadata\\src\\";
//	public static String METADATA_URL="C:\\nik_data\\metadata\\TradeManagerMetadata\\src\\";

	@Override
	public ServerResponse getEntityXml(String filename) {
		ServerResponse response = new ServerResponse();
		try {
			File file = new File(MetadataFacade.METADATA_URL+"entities\\"+filename);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			OutputFormat format = new OutputFormat(doc);
			StringWriter stringOut = new StringWriter();
			XMLSerializer serial = new XMLSerializer(stringOut, format);
			serial.serialize(doc);
			response.setSeverity(ServerResponse.INFO);
			response.setResponseCode("Metadata.EntityLoadSuccessful");
			response.setData(stringOut.toString());
			LogUtil.log("metadata ("+filename+") loaded", Level.INFO, null);
		} catch (ParserConfigurationException e) {
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Metadata.EntityLoadFailed");
			LogUtil.log("metadata ("+filename+") load failed", Level.SEVERE, e);
		} catch (SAXException e) {
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Metadata.EntityLoadFailed");
			LogUtil.log("metadata ("+filename+") load failed", Level.SEVERE, e);
		} catch (IOException e) {
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Metadata.EntityLoadFailed");
			LogUtil.log("metadata ("+filename+") load failed", Level.SEVERE, e);
		}
		return response;
	}



	@Override
	public ServerResponse getMenuXml() {
		ServerResponse response = new ServerResponse();
		try {
			File file = new File(MetadataFacade.METADATA_URL+"menu\\Menu.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			OutputFormat format = new OutputFormat(doc);
			StringWriter stringOut = new StringWriter();
			XMLSerializer serial = new XMLSerializer(stringOut, format);
			serial.serialize(doc);
			response.setSeverity(ServerResponse.INFO);
			response.setResponseCode("Metadata.EntityLoadSuccessful");
			response.setData(stringOut.toString());
			LogUtil.log("metadata Menu loaded", Level.INFO, null);
		} catch (ParserConfigurationException e) {
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Metadata.EntityLoadFailed");
			LogUtil.log("metadata Menu load failed", Level.SEVERE, e);
		} catch (SAXException e) {
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Metadata.EntityLoadFailed");
			LogUtil.log("metadata Menu load failed", Level.SEVERE, e);
		} catch (IOException e) {
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Metadata.EntityLoadFailed");
			LogUtil.log("metadata Menu load failed", Level.SEVERE, e);
		}
		return response;
	}
}
