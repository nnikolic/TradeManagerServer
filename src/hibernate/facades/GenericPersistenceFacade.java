package hibernate.facades;

import hibernate.interceptors.GenericInterceptor;
import hibernate.locales.GenericPersistenceFacadeLocale;
import hibernate.remotes.GenericPersistenceFacadeRemote;
import hibernate.sessionBeans.LogUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import model.metadata.EntityField;
import model.metadata.EntityMetadata;
import model.metadata.FieldTypeEnum;
import model.metadata.SiblingMetadata;
import util.DateUtil;
import util.EntityObject;
import util.ServerResponse;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class GenericPersistenceFacade implements
		GenericPersistenceFacadeRemote, GenericPersistenceFacadeLocale {

//	@Resource
//	private SessionContext ctx;
	
	@Resource
	private UserTransaction userTx;

	@PersistenceContext
	private EntityManager entityManager;

//	public Properties getProperties() {
//		Properties properties = new Properties();
//		properties.put("java.naming.factory.initial",
//				"org.jnp.interfaces.NamingContextFactory");
//		properties.put("java.naming.factory.url.pkgs",
//				"=org.jboss.naming:org.jnp.interfaces");
//		properties.put("java.naming.provider.url", "localhost:1099");
//		return properties;
//	}

	@Override
	public ServerResponse insertEntity(Object entity) {
		LogUtil.log("saving Entity instance", Level.INFO, null);
		ServerResponse response = new ServerResponse();
		try {
			boolean allreadyStarted = userTx.getStatus()==Status.STATUS_ACTIVE;
			if(!allreadyStarted)
				userTx.begin();
			entity = entityManager.merge(entity);
			entityManager.persist(entity);
			if(!allreadyStarted)
				userTx.commit();
			response.setSeverity(ServerResponse.INFO);
			response.setResponseCode("Hibernate.InsertSuccessful");
			response.setData(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (Exception re) {
			try{
				if(userTx.getStatus()!=Status.STATUS_NO_TRANSACTION){
					userTx.rollback();
				}
			}catch(SystemException e){
				e.printStackTrace();
			}
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Hibernate.InsertFailed");
			response.setResponseMessage(re.getMessage());
			LogUtil.log("save failed", Level.SEVERE, re);
		}
		return response;
	}

	@Override
	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public ServerResponse deleteEntity(Object entity, Object id) {
		LogUtil.log("deleting Entity instance", Level.INFO, null);
		ServerResponse response = new ServerResponse();
		try {
			boolean allreadyStarted = userTx.getStatus()==Status.STATUS_ACTIVE;
			if(!allreadyStarted)
				userTx.begin();
			entity = entityManager.getReference(entity.getClass(), id);
			String checkRes = GenericInterceptor.preDelete(entity, this);
			if (!checkRes.equals("")) {
				throw new Exception(checkRes);
			}
			entityManager.remove(entity);
			if(!allreadyStarted)
				userTx.commit();
			response.setSeverity(ServerResponse.INFO);
			response.setResponseCode("Hibernate.DeleteSuccessful");
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (Exception re) {
			try{
				if(userTx.getStatus()!=Status.STATUS_NO_TRANSACTION){
					userTx.rollback();
				}
			}catch(SystemException e){
				e.printStackTrace();
			}
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Hibernate.DeleteFailed");
			response.setResponseMessage(re.getMessage());
			LogUtil.log("delete failed", Level.SEVERE, re);
		}
		return response;
	}

	@Override
	public ServerResponse selectEntity(Class clazz, Object id) {
		LogUtil.log("finding Entity instance with id: " + id.toString(),
				Level.INFO, null);
		ServerResponse response = new ServerResponse();
		try {
			boolean allreadyStarted = userTx.getStatus()==Status.STATUS_ACTIVE;
			if(!allreadyStarted)
				userTx.begin();
			Object instance = entityManager.find(clazz, id);
			if(!allreadyStarted)
				userTx.commit();
			response.setSeverity(ServerResponse.INFO);
			response.setResponseCode("Hibernate.SelectSuccessful");
			response.setData(instance);
		} catch (Exception re) {
			try{
				if(userTx.getStatus()!=Status.STATUS_NO_TRANSACTION){
					userTx.rollback();
				}
			}catch(SystemException e){
				e.printStackTrace();
			}
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Hibernate.SelectFailed");
			response.setResponseMessage(re.getMessage());
			LogUtil.log("find failed", Level.SEVERE, re);
		}
		return response;
	}

	@Override
	public ServerResponse updateEntity(Object entity) {
		LogUtil.log("updating Entity instance", Level.INFO, null);
		ServerResponse response = new ServerResponse();
		try {
			boolean allreadyStarted = userTx.getStatus()==Status.STATUS_ACTIVE;
			if(!allreadyStarted)
				userTx.begin();
			Object instance = entityManager.merge(entity);
			if(!allreadyStarted)
				userTx.commit();
			LogUtil.log("update successful", Level.INFO, null);
			response.setSeverity(ServerResponse.INFO);
			response.setResponseCode("Hibernate.UpdateSuccessful");
			response.setData(instance);
		} catch (Exception re) {
			try{
				if(userTx.getStatus()!=Status.STATUS_NO_TRANSACTION){
					userTx.rollback();
				}
			}catch(SystemException e){
				e.printStackTrace();
			}
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Hibernate.UpdateFailed");
			response.setResponseMessage(re.getMessage());
			LogUtil.log("update failed", Level.SEVERE, re);
		}
		return response;
	}

	@Override
	public ServerResponse selectEntities(Class clazz, int howMany, String orderStr, String orderDirect) {
		LogUtil.log("finding all " + clazz.getName() + " instances",
				Level.INFO, null);
		ServerResponse response = new ServerResponse();
		try {
			String[] orderFields = null;
			orderDirect = (orderDirect != null && !orderDirect.equals("")) ? orderDirect : "DESC";
			if(orderStr != null && !orderStr.equals("")){
				orderFields = orderStr.split(",");
			}
			orderStr = "";
			if(orderFields != null){
				orderStr = "order by ";
				for(String fs: orderFields){
					orderStr+="result."+fs+" ";
				}
				orderStr += orderDirect;
			}
			boolean allreadyStarted = userTx.getStatus()==Status.STATUS_ACTIVE;
			if(!allreadyStarted)
				userTx.begin();
			String queryString = "select result from " + clazz.getName()
					+ " result "+orderStr;
			Query query = entityManager.createQuery(queryString);
			query.setMaxResults(howMany);
			List<Object> result = query.getResultList();
			if(!allreadyStarted)
				userTx.commit();
			LogUtil.log("find all successful", Level.INFO, null);
			response.setSeverity(ServerResponse.INFO);
			response.setResponseCode("Hibernate.FindAllSuccessful");
			response.setData(result);
		} catch (Exception re) {
			try{
				if(userTx.getStatus()!=Status.STATUS_NO_TRANSACTION){
					userTx.rollback();
				}
			}catch(SystemException e){
				e.printStackTrace();
			}
			LogUtil.log("find all failed", Level.SEVERE, re);
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Hibernate.FindAllFailed");
			response.setResponseMessage(re.getMessage());
		}
		return response;
	}

//	@Override
//	public ServerResponse selectEntitiesByProperties(Class clazz,
//			String[] properties, Object[] values) {
//		LogUtil.log("finding Entity " + clazz.getName()
//				+ " instance with properties: " + properties + ", values: "
//				+ values, Level.INFO, null);
//		ServerResponse response = new ServerResponse();
//		try {
//			boolean allreadyStarted = userTx.getStatus()==Status.STATUS_ACTIVE;
//			if(!allreadyStarted)
//				userTx.begin();
//			String queryString = "select model from " + clazz.getName()
//					+ " model where ";
//			for (int i = 0; i < properties.length; i++) {
//				queryString += "model." + properties[i] + " = :"
//						+ properties[i] + " ";
//				if (i < properties.length - 1) {
//					queryString += "AND ";
//				}
//			}
//			LogUtil.log("query: " + queryString, Level.INFO, null);
//			Query query = entityManager.createQuery(queryString);
//			for (int i = 0; i < properties.length; i++) {
//				query.setParameter(properties[i], values[i]);
//			}
//			List<Object> result = query.getResultList();
//			if(!allreadyStarted)
//				userTx.commit();
//			LogUtil.log("find by properties successful", Level.INFO, null);
//			response.setSeverity(ServerResponse.INFO);
//			response.setResponseCode("Hibernate.FindByPropertiesSuccessful");
//			response.setData(result);
//		} catch (Exception re) {
//			try{
//				if(userTx.getStatus()!=Status.STATUS_NO_TRANSACTION){
//					userTx.setRollbackOnly();
//				}
//			}catch(SystemException e){
//				e.printStackTrace();
//			}
//			LogUtil.log("find by property name failed", Level.SEVERE, re);
//			response.setSeverity(ServerResponse.ERROR);
//			response.setResponseCode("Hibernate.FindByPropertiesFailed");
//			response.setResponseMessage(re.getMessage());
//		}
//		return response;
//	}

	@Override
	public ServerResponse selectFetchEntity(Class clazz, String idFieldName,
			Object id, String... fetchArgs) {
		LogUtil.log("finding fetch Entity instance with id: " + id.toString(),
				Level.INFO, null);
		ServerResponse response = new ServerResponse();
		try {
			boolean allreadyStarted = userTx.getStatus()==Status.STATUS_ACTIVE;
			if(!allreadyStarted)
				userTx.begin();
			String fs = "";
			for (String s : fetchArgs) {
				fs += "left join fetch model." + s + " ";
			}
			String queryString = "select model from " + clazz.getName()
					+ " model " + fs + " where model." + idFieldName + " =:id ";
			LogUtil.log("query: " + queryString, Level.INFO, null);
			Query query = entityManager.createQuery(queryString);
			query.setParameter("id", id);
			List<Object> l = query.getResultList();
			if(!allreadyStarted)
				userTx.commit();
			Object instance = (l != null && l.size() > 0) ? l.get(0) : null;
			response.setSeverity(ServerResponse.INFO);
			response.setResponseCode("Hibernate.SelectSuccessful");
			response.setData(instance);
		} catch (Exception re) {
			try{
				if(userTx.getStatus()!=Status.STATUS_NO_TRANSACTION){
					userTx.rollback();
				}
			}catch(SystemException e){
				e.printStackTrace();
			}
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Hibernate.SelectFailed");
			response.setResponseMessage(re.getMessage());
			LogUtil.log("find failed", Level.SEVERE, re);
		}
		return response;
	}

//	@Override
//	public ServerResponse selectFullEntity(Class clazz, String idFieldName,
//			Object id) {
//		LogUtil.log("finding full Entity instance with id: " + id.toString(),
//				Level.INFO, null);
//		ServerResponse response = new ServerResponse();
//		try {
//			boolean allreadyStarted = userTx.getStatus()==Status.STATUS_ACTIVE;
//			if(!allreadyStarted)
//				userTx.begin();
//			String queryString = "select model from " + clazz.getName()
//					+ " model fetch all properties where model." + idFieldName
//					+ " =:id";
//			LogUtil.log("query: " + queryString, Level.INFO, null);
//			Query query = entityManager.createQuery(queryString);
//			query.setParameter("id", id);
//			Object instance = query.getSingleResult();
//			if(!allreadyStarted)
//				userTx.commit();
//			response.setSeverity(ServerResponse.INFO);
//			response.setResponseCode("Hibernate.SelectSuccessful");
//			response.setData(instance);
//		} catch (Exception re) {
//			try{
//				if(userTx.getStatus()!=Status.STATUS_NO_TRANSACTION){
//					userTx.setRollbackOnly();
//				}
//			}catch(SystemException e){
//				e.printStackTrace();
//			}
//			response.setSeverity(ServerResponse.ERROR);
//			response.setResponseCode("Hibernate.SelectFailed");
//			response.setResponseMessage(re.getMessage());
//			LogUtil.log("find failed", Level.SEVERE, re);
//		}
//		return response;
//	}

	@Override
	public ServerResponse searchSelect(EntityMetadata metadata,
			Map<String, Object> searchMap, int maxRowCount, String orderStr, String orderDirect) {
		ServerResponse response = new ServerResponse();
		try {
			String[] orderFields = null;
			orderDirect = (orderDirect != null && !orderDirect.equals("")) ? orderDirect : "DESC";
			if(orderStr != null && !orderStr.equals("")){
				orderFields = orderStr.split(",");
			}
			orderStr = "";
			if(orderFields != null){
				orderStr = "order by ";
				for(String fs: orderFields){
					orderStr+="model."+fs+" ";
				}
				orderStr += orderDirect;
			}
			boolean allreadyStarted = userTx.getStatus()==Status.STATUS_ACTIVE;
			if(!allreadyStarted)
				userTx.begin();
			LogUtil.log("Search map: " + searchMap.toString(), Level.INFO, null);
			String queryString = "from "
					+ metadata.getEntityName() + " as model "; // where
																				// model."+idFieldName+"
																				// =:id";
			Object[] keys = searchMap.keySet().toArray();
			
			if (searchMap.keySet().size() > 0) {
				queryString += "where ";
				String comparator = "";
				for (int i = 0; i < keys.length; i++) {
					comparator = "";
					EntityField ef = metadata
							.getFieldByName(keys[i].toString());
					if(ef!=null){
						String searchStr = (String) searchMap.get(keys[i]);
						Object searchObj = null;
						if (searchStr.startsWith(">=")) {
							comparator = ">=";
						} else if (searchStr.startsWith("<=")) {
							comparator = "<=";
						} else if (searchStr.startsWith("<")) {
							comparator = "<";
						} else if (searchStr.startsWith(">")) {
							comparator = ">";
						} else if (searchStr.startsWith("=")) {
							comparator = "=";
						} else {
							if(!ef.getFieldType().equals(FieldTypeEnum.STRING))
								comparator = "=";
						}
						if (ef.getFieldType().equals(FieldTypeEnum.INTEGER)) {
							if (searchStr.startsWith(">=")
									|| searchStr.startsWith("<=")) {
								searchStr = searchStr.substring(2);
							} else if (searchStr.startsWith(">")
									|| searchStr.startsWith("<")
									|| searchStr.startsWith("=")) {
								searchStr = searchStr.substring(1);
							} 
							searchObj = Integer.parseInt(searchStr);
						} else if (ef.getFieldType().equals(FieldTypeEnum.DOUBLE) || ef.getFieldType().equals(FieldTypeEnum.PRICE)) {
							if (searchStr.startsWith(">=")
									|| searchStr.startsWith("<=")) {
								searchStr = searchStr.substring(2);
							} else if (searchStr.startsWith(">")
									|| searchStr.startsWith("<")
									|| searchStr.startsWith("=")) {
								searchStr = searchStr.substring(1);
							}
							searchObj = Double.parseDouble(searchStr);
						} else if (ef.getFieldType().equals(FieldTypeEnum.DATE)) {
							if (searchStr.startsWith(">=")
									|| searchStr.startsWith("<=")) {
								searchStr = searchStr.substring(2);
							} else if (searchStr.startsWith(">")
									|| searchStr.startsWith("<")
									|| searchStr.startsWith("=")) {
								searchStr = searchStr.substring(1);
							}
							searchObj = new SimpleDateFormat(DateUtil.DATE_FORMAT).parse(searchStr);
						}else if(ef.getFieldType().equals(FieldTypeEnum.BOOLEAN)){ 
							searchObj = new Boolean(searchStr);
						}else {
							if(comparator.equals("")){
								comparator = "like";
								searchObj = "%"+searchStr+"%";
							}else{
								searchObj = searchStr.substring(1);
							}
						}
						searchMap.put((String) keys[i], searchObj);
						String prefix="";
						if(ef.isRelField()){
							SiblingMetadata sm = metadata.getSiblingByID(ef.getSiblingId());
							String sibName = (sm.getSiblingName().charAt(0)+"").toLowerCase()+sm.getSiblingName().substring(1);
							String fieldRealName = sm.getRealFieldName(ef.getFieldName());
							if(!fieldRealName.equals("ID"))
								fieldRealName = (fieldRealName.charAt(0)+"").toLowerCase()+fieldRealName.substring(1);
							prefix = "model."+sibName+"."+fieldRealName;
						}else{
							prefix = "model.";
							if(!ef.getFieldName().equals("ID"))
								prefix +=(ef.getFieldName().charAt(0)+"").toLowerCase()+ef.getFieldName().substring(1);
							else
								prefix += ef.getFieldName();
						}
						
						queryString += prefix+ " "
						+ comparator + " :" + keys[i];
					}else{
						//Parent
						if(keys[i].toString().startsWith("#") && keys[i].toString().endsWith("#")){
							String parentAlias = "p"+i;
							EntityObject parent = (EntityObject) searchMap.get(keys[i]);
							String childName = keys[i].toString().substring(1, keys[i].toString().length()-1);
							queryString+="model in ( select "+childName+" from "+parent.getClass().getSimpleName()+" as "+parentAlias+" left outer join "+parentAlias+"."+childName+" as "+childName+" where "+parentAlias+" = :"+childName+")";
						}
					}
					
					if (i != keys.length - 1) {
						queryString += " and ";
					}
				}
			}
			
			queryString += " "+orderStr;
			
			LogUtil.log("query: " + queryString, Level.INFO, null);

//			Query query = entityManager.createNativeQuery(queryString,
//					Class.forName(metadata.getEntityClassPath()));
			Query query = entityManager.createQuery(queryString);

			query.setMaxResults(maxRowCount);

			for (int i = 0; i < keys.length; i++) {
				Object o = (Object) searchMap.get(keys[i]);
				String key="";
				if(keys[i].toString().startsWith("#") && keys[i].toString().endsWith("#")){
					key = keys[i].toString().substring(1, keys[i].toString().length()-1);
				}else{
					key = keys[i].toString();
				}
				query.setParameter(key, o);
			}
			List<Object> l = query.getResultList();
			if(!allreadyStarted)
				userTx.commit();
			// Object instance = (l!=null && l.size()>0) ? l.get(0):null;
			response.setSeverity(ServerResponse.INFO);
			response.setResponseCode("Hibernate.SearchSuccessful");
			response.setData(l);
		} catch (Exception re) {
			try{
				if(userTx.getStatus()!=Status.STATUS_NO_TRANSACTION){
					userTx.rollback();
				}
			}catch(SystemException e){
				e.printStackTrace();
			}
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Hibernate.SearchFailed");
			response.setResponseMessage(re.getMessage());
			LogUtil.log("find failed", Level.SEVERE, re);
		}

		return response;
	}

	@Override
	public ServerResponse executeQuery(String queryStr) {
		boolean allreadyStarted = false;
		ServerResponse response = new ServerResponse();
		try {
			allreadyStarted = userTx.getStatus()==Status.STATUS_ACTIVE;
			if(!allreadyStarted)
				userTx.begin();
			Query query = entityManager.createQuery(queryStr);
			List<Object> l = query.getResultList();
			response.setSeverity(ServerResponse.INFO);
			response.setResponseCode("Hibernate.QueryExecuted");
			response.setData(l);
		} catch (Exception qe) {
			LogUtil.log("execute query failed", Level.SEVERE, qe);
			response.setSeverity(ServerResponse.ERROR);
			response.setResponseCode("Hibernate.SearchFailed");
			response.setResponseMessage(qe.getMessage());
			try {
				userTx.setRollbackOnly();
			} catch (Exception re) {
				LogUtil.log("execute query failed", Level.SEVERE, re);
			}
		}finally{
			if(!allreadyStarted)
				try {
					userTx.commit();
				} catch (Exception ce) {
					response.setSeverity(ServerResponse.ERROR);
					response.setResponseCode("Hibernate.SearchFailed");
					response.setResponseMessage(ce.getMessage());
					LogUtil.log("execute query failed", Level.SEVERE, ce);
				} 
		}
		return response;
	}
}
