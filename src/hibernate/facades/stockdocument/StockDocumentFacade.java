package hibernate.facades.stockdocument;

import hibernate.entityBeans.StockDocument;
import hibernate.entityBeans.StockDocumentItem;
import hibernate.entityBeans.Supplies;
import hibernate.entityBeans.Warehouse;
import hibernate.locales.GenericPersistenceFacadeLocale;
import hibernate.remotes.stockdocument.StockDocumentFacadeRemote;
import hibernate.sessionBeans.LogUtil;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import model.custom.DocumentTypeEnum;
import util.ServerResponse;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class StockDocumentFacade implements StockDocumentFacadeRemote{

	@EJB
	GenericPersistenceFacadeLocale persistenceFacade;
	
	@Resource
	UserTransaction userTx;
	
	private void supplyProcess(StockDocument stockDocument, ServerResponse response) throws Exception{
		Warehouse inWh=null, outWh=null;
		if(stockDocument.getDocumentType().getID().intValue()==DocumentTypeEnum.INWARD){
			ServerResponse sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", stockDocument.getWarehouseIn().getID(), new String[]{"supplies"}); 
			inWh = (Warehouse) sr.getData();
			for(StockDocumentItem sdi: stockDocument.getItems()){
				Supplies sp = inWh.getSuppliesByPriuctID(sdi.getSalesPriceItem().getProduct().getID().intValue());
				if(sp==null){
					sp = new Supplies();
					sp.setProduct(sdi.getSalesPriceItem().getProduct());
					sp.setQuantity(sdi.getQuantity());
					inWh.getSupplies().add(sp);
				}else{
					sp.setQuantity(sp.getQuantity()+sdi.getQuantity());
				}
			}
			persistenceFacade.updateEntity(inWh);
			
		}else if(stockDocument.getDocumentType().getID().intValue()==DocumentTypeEnum.OUTWARD || stockDocument.getDocumentType().getID().intValue() == DocumentTypeEnum.EXPENSE){
			ServerResponse sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", stockDocument.getWarehouseOut().getID(),  new String[]{"supplies"}); 
			outWh = (Warehouse) sr.getData();
			for(StockDocumentItem sdi: stockDocument.getItems()){
				Supplies sp = outWh.getSuppliesByPriuctID(sdi.getSalesPriceItem().getProduct().getID().intValue());
				if(sp==null){
					sp = new Supplies();
					sp.setProduct(sdi.getSalesPriceItem().getProduct());
					sp.setQuantity(0-sdi.getQuantity());
					outWh.getSupplies().add(sp);
				}else{
					sp.setQuantity(sp.getQuantity()-sdi.getQuantity());
				}
			}
			persistenceFacade.updateEntity(outWh);
		}else if(stockDocument.getDocumentType().getID().intValue()==DocumentTypeEnum.TRANSFER){
			ServerResponse sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", stockDocument.getWarehouseOut().getID(), new String[]{"supplies"}); 
			outWh = (Warehouse) sr.getData();
			
			sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", stockDocument.getWarehouseIn().getID(), new String[]{"supplies"}); 
			inWh = (Warehouse) sr.getData();
			
			for(StockDocumentItem sdi: stockDocument.getItems()){
				Supplies sp = outWh.getSuppliesByPriuctID(sdi.getSalesPriceItem().getProduct().getID().intValue());
				if(sp==null){
					sp = new Supplies();
					sp.setProduct(sdi.getSalesPriceItem().getProduct());
					sp.setQuantity(0-sdi.getQuantity());
					outWh.getSupplies().add(sp);
				}else{
					sp.setQuantity(sp.getQuantity()-sdi.getQuantity());
				}
				Supplies spIn = inWh.getSuppliesByPriuctID(sp.getProduct().getID().intValue());
				if(spIn==null){
					spIn = new Supplies();
					spIn.setProduct(sdi.getSalesPriceItem().getProduct());
					spIn.setQuantity(sdi.getQuantity());
					inWh.getSupplies().add(spIn);
				}else{
					spIn.setQuantity(spIn.getQuantity()+sdi.getQuantity());
				}
			}
			persistenceFacade.updateEntity(inWh);
			persistenceFacade.updateEntity(outWh);
		}
	}
	
	private void undoSupplyProcess(StockDocument stockDocument, ServerResponse response) throws Exception{
		Warehouse inWh=null, outWh=null;
		if(stockDocument.getDocumentType().getID().intValue()==DocumentTypeEnum.INWARD){
			ServerResponse sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", stockDocument.getWarehouseIn().getID(),  new String[]{"supplies"}); 
			inWh = (Warehouse) sr.getData();
			for(StockDocumentItem sdi: stockDocument.getItems()){
				Supplies sp = inWh.getSuppliesByPriuctID(sdi.getSalesPriceItem().getProduct().getID().intValue());
				if(sp!=null){
					sp.setQuantity(sp.getQuantity()-sdi.getQuantity());
				}
			}
			persistenceFacade.updateEntity(inWh);
			
		}else if(stockDocument.getDocumentType().getID().intValue()==DocumentTypeEnum.OUTWARD || stockDocument.getDocumentType().getID().intValue() == DocumentTypeEnum.EXPENSE){
			ServerResponse sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", stockDocument.getWarehouseOut().getID(),  new String[]{"supplies"}); 
			outWh = (Warehouse) sr.getData();
			for(StockDocumentItem sdi: stockDocument.getItems()){
				Supplies sp = outWh.getSuppliesByPriuctID(sdi.getSalesPriceItem().getProduct().getID().intValue());
				if(sp!=null){
					sp.setQuantity(sp.getQuantity()+sdi.getQuantity());
				}
			}
			persistenceFacade.updateEntity(outWh);
		}else if(stockDocument.getDocumentType().getID().intValue()==DocumentTypeEnum.TRANSFER){
			ServerResponse sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", stockDocument.getWarehouseOut().getID(), new String[]{"supplies"}); 
			outWh = (Warehouse) sr.getData();
			
			sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", stockDocument.getWarehouseIn().getID(), new String[]{"supplies"}); 
			inWh = (Warehouse) sr.getData();
			
			for(StockDocumentItem sdi: stockDocument.getItems()){
				Supplies sp = outWh.getSuppliesByPriuctID(sdi.getSalesPriceItem().getProduct().getID().intValue());
				if(sp!=null){
					sp.setQuantity(sp.getQuantity()+sdi.getQuantity());
					Supplies spIn = inWh.getSuppliesByPriuctID(sp.getProduct().getID().intValue());
					if(spIn!=null){
						spIn.setQuantity(spIn.getQuantity()-sdi.getQuantity());
					}
				}
			}
			persistenceFacade.updateEntity(inWh);
			persistenceFacade.updateEntity(outWh);
		}
	}
	
	@Override
	public ServerResponse updateStockDocument(StockDocument stockDocument) {
		LogUtil.log("updating StockDocument instance", Level.INFO, null);
		ServerResponse response = new ServerResponse();
		try {
			userTx.begin();
			StockDocument originalDocument = (StockDocument) persistenceFacade.selectFetchEntity(StockDocument.class, "ID", stockDocument.getID(), new String[]{"items"}).getData();
			
			if(originalDocument.getCanceled() == Boolean.FALSE)
				undoSupplyProcess(originalDocument, response);
			supplyProcess(stockDocument, response);
			
			stockDocument.setCanceled(Boolean.FALSE);
			response = persistenceFacade.updateEntity(stockDocument);
			userTx.commit();
		} catch (Exception e){
			try{
				userTx.rollback();
			}catch(SystemException se){
				e.printStackTrace();
			}
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Hibernate.UpdateFailed");
			response.setResponseMessage(e.getMessage());
			LogUtil.log("Update failed", Level.SEVERE, e);
		}
		return response;
	}

	@Override
	public ServerResponse insertStockDocument(StockDocument stockDocument) {
		LogUtil.log("inserting StockDocument instance", Level.INFO, null);
		ServerResponse response = null;
		try{
			userTx.begin();
			Calendar cal = Calendar.getInstance();
			cal.setTime(stockDocument.getCreationTime());
			int year = cal.get(Calendar.YEAR);
			response = persistenceFacade.executeQuery("select count(model) from StockDocument as model where model.documentType.ID="+stockDocument.getDocumentType().getID()+" and year(model.creationTime) = "+year);
			if(response.getSeverity() == ServerResponse.INFO){
				String prefix = "";
				switch(stockDocument.getDocumentType().getID()){
				case DocumentTypeEnum.INWARD:
					prefix = "U";
					break;
				case DocumentTypeEnum.OUTWARD:
					prefix = "I";
					break;
				case DocumentTypeEnum.TRANSFER:
					prefix = "T";
					break;
				case DocumentTypeEnum.DISCOUNT_BILL:
					prefix = "D";
					break;
				default:
					prefix = "R";
				}
				
				stockDocument.setDocumentCode(prefix+(((Long)((List<Object>)response.getData()).get(0))+1+"/"+year));
				response = persistenceFacade.insertEntity(stockDocument);
				supplyProcess(stockDocument, response);
			}
			userTx.commit();
		}catch (Exception e) {
			try{
				userTx.rollback();
			}catch(SystemException se){
				e.printStackTrace();
			}
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Hibernate.InsertFailed");
			response.setResponseMessage(e.getMessage());
			LogUtil.log("insert failed", Level.SEVERE, e);
		}
		
		return response;
	}

	@Override
	public ServerResponse deleteStockDocument(StockDocument stockDocument) {
		LogUtil.log("deleting StockDocument instance", Level.INFO, null);
		ServerResponse response = new ServerResponse();
		try {
			userTx.begin();
			StockDocument originalDocument = (StockDocument) persistenceFacade.selectFetchEntity(StockDocument.class, "ID", stockDocument.getID(), new String[]{"items"}).getData();
			if(originalDocument.getCanceled() == Boolean.FALSE){
				undoSupplyProcess(originalDocument, response);
				stockDocument.setCanceled(Boolean.TRUE);
				response = persistenceFacade.updateEntity(stockDocument);
			}else{
				response.setSeverity(ServerResponse.INFO);
				response.setResponseCode("StockDocument.DocumentAlreadyCanceled");
			}
			userTx.commit();
		}catch (Exception e) {
			try{
				userTx.rollback();
			}catch(SystemException se){
				e.printStackTrace();
			}
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Hibernate.DeleteFailed");
			response.setResponseMessage(e.getMessage());
			LogUtil.log("Delete failed", Level.SEVERE, e);
		}
		return response;
	}
}
