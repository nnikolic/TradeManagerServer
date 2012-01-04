package hibernate.facades.salesprice;

import hibernate.entityBeans.SalesPrice;
import hibernate.entityBeans.SalesPriceItem;
import hibernate.locales.GenericPersistenceFacadeLocale;
import hibernate.remotes.salesprice.SalesPriceFacadeRemote;
import hibernate.sessionBeans.LogUtil;

import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import util.ServerResponse;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class SalesPriceFacade implements SalesPriceFacadeRemote{

	@Resource 
	private UserTransaction userTx;

	@EJB
	GenericPersistenceFacadeLocale persistenceFacade;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public ServerResponse updateSalesPrice(SalesPrice sp) {
		LogUtil.log("updating SalesPrice instance", Level.INFO, null);
		ServerResponse response = null;
		try{
			userTx.begin();
			Query query = entityManager.createQuery("select result from hibernate.entityBeans.StockDocument result where result.salesPrice =:salesPrices");
			query.setMaxResults(2);
			query.setParameter("salesPrices", sp);
			if(sp.getID()!=null && query.getResultList().size()==0){
				response = persistenceFacade.updateEntity(sp);
			}else{
				if(sp.getID()==null){
					response = persistenceFacade.insertEntity(sp);
					if(response.getSeverity()!=ServerResponse.INFO){
						return response;
					}
					sp = (SalesPrice) response.getData();
					sp.setBaseID(sp.getID());
					response = persistenceFacade.updateEntity(sp);
				}else{
					SalesPrice oldSp = (SalesPrice) persistenceFacade.selectEntity(SalesPrice.class, sp.getID()).getData();
					oldSp.setOppened(false);
					response = persistenceFacade.updateEntity(oldSp);
					if(response.getSeverity()!=ServerResponse.INFO){
						return response;
					}
					sp.setID(-1);
					for(SalesPriceItem spi: sp.getSalesPriceItems()){
						spi.setID(null);
					}
					response = persistenceFacade.insertEntity(sp);
				}
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
			LogUtil.log("delete failed", Level.SEVERE, e);
		}
		
		return response;
	}

}
