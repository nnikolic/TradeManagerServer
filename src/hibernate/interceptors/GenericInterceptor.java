package hibernate.interceptors;

import hibernate.entityBeans.StockDocument;
import hibernate.entityBeans.StockDocumentItem;
import hibernate.entityBeans.Supplies;
import hibernate.entityBeans.Warehouse;
import hibernate.locales.GenericPersistenceFacadeLocale;
import model.custom.DocumentTypeEnum;
import util.ServerResponse;

public class GenericInterceptor {
	public static String preDelete(Object entity, GenericPersistenceFacadeLocale persistenceFacade){
//		if(entity instanceof StockDocument){
//			StockDocument sd = (StockDocument) entity;
//			return onStockDocumentDelete(sd, persistenceFacade);
//		}
		return "";
	}
	
	private static String onStockDocumentDelete(StockDocument sd, GenericPersistenceFacadeLocale persistenceFacade){
		Warehouse inWh=null, outWh=null;
		if(sd.getDocumentType().getID().intValue()==DocumentTypeEnum.INWARD){
			ServerResponse sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", sd.getWarehouseIn().getID(), new String[]{"supplies"}); 
			inWh = (Warehouse) sr.getData();
			for(StockDocumentItem sdi: sd.getItems()){
				Supplies sp = inWh.getSuppliesByPriuctID(sdi.getSalesPriceItem().getProduct().getID().intValue());
				if(sp==null  || sp.getQuantity()<sdi.getQuantity()){
					return "Hibernate.UndoSuppliesError";
				}else{
					sp.setQuantity(sp.getQuantity()-sdi.getQuantity());
				}
			}
			persistenceFacade.updateEntity(inWh);
		}else if(sd.getDocumentType().getID().intValue()==DocumentTypeEnum.OUTWARD){
			ServerResponse sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", sd.getWarehouseOut().getID(), new String[]{"supplies"}); 
			outWh = (Warehouse) sr.getData();
			for(StockDocumentItem sdi: sd.getItems()){
				Supplies sp = outWh.getSuppliesByPriuctID(sdi.getSalesPriceItem().getProduct().getID().intValue());
				if(sp==null){
					return "Hibernate.UndoSuppliesError";
				}else{
					sp.setQuantity(sp.getQuantity()+sdi.getQuantity());
				}
			}
			persistenceFacade.updateEntity(outWh);
		}else if(sd.getDocumentType().getID().intValue()==DocumentTypeEnum.TRANSFER){
			ServerResponse sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", sd.getWarehouseOut().getID(), new String[]{"supplies"}); 
			outWh = (Warehouse) sr.getData();
			
			sr = persistenceFacade.selectFetchEntity(Warehouse.class, "ID", sd.getWarehouseIn().getID(), new String[]{"supplies"}); 
			inWh = (Warehouse) sr.getData();
			
			for(StockDocumentItem sdi: sd.getItems()){
				Supplies sp = outWh.getSuppliesByPriuctID(sdi.getSalesPriceItem().getProduct().getID().intValue());
				if(sp==null){
					return "Hibernate.UndoSuppliesError";
				}else{
					sp.setQuantity(sp.getQuantity()+sdi.getQuantity());
					Supplies spIn = inWh.getSuppliesByPriuctID(sp.getProduct().getID().intValue());
					if(spIn==null || spIn.getQuantity()<sdi.getQuantity()){
						return "Hibernate.UndoSuppliesError";
					}else{
						spIn.setQuantity(spIn.getQuantity()-sdi.getQuantity());
					}
				}
			}
			persistenceFacade.updateEntity(outWh);
			persistenceFacade.updateEntity(inWh);
		}
		return "";
	}
}
