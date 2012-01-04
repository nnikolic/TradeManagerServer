package hibernate.remotes.salesprice;

import hibernate.entityBeans.SalesPrice;

import javax.ejb.Remote;

import util.ServerResponse;

@Remote
public interface SalesPriceFacadeRemote {
	public ServerResponse updateSalesPrice(SalesPrice sp);
}
