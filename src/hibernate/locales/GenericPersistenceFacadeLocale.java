package hibernate.locales;

import java.util.Map;

import javax.ejb.Local;

import model.metadata.EntityMetadata;
import util.ServerResponse;

@Local
public interface GenericPersistenceFacadeLocale {

	public ServerResponse insertEntity(Object entity);
	
	public ServerResponse updateEntity(Object entity);
	
	public ServerResponse deleteEntity(Object entity, Object id);
	
	public ServerResponse selectEntity(Class clazz, Object id);
	
	public ServerResponse selectEntities(Class clazz, int howMany, String orderStr, String orderDirect);
	
	public ServerResponse selectFetchEntity(Class clazz, String idFieldName, Object id, String... fetchArgs); 
	
	public ServerResponse searchSelect(EntityMetadata metadata, Map<String, Object> searchMap, int maxRowCount, String orderStr, String orderDirect);

	public ServerResponse executeQuery(String queryStr);
}
