package hibernate.remotes.stockdocument;

import javax.ejb.Remote;

import util.ServerResponse;
import hibernate.entityBeans.StockDocument;

@Remote
public interface StockDocumentFacadeRemote {
	public ServerResponse insertStockDocument(StockDocument stockDocument);
	public ServerResponse updateStockDocument(StockDocument stockDocument);
	public ServerResponse deleteStockDocument(StockDocument stockDocument);
}
