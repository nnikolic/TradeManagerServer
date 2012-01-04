package hibernate.sessionBeans;

import hibernate.entityBeans.User;
import hibernate.locales.GenericPersistenceFacadeLocale;
import hibernate.remotes.SessionBeanRemote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateful;

import model.metadata.EntityMetadata;
import util.ServerResponse;

@Stateful
public class SessionBean implements SessionBeanRemote{
	private boolean login = false;
	private String username="", password="";
	private User loggedUser = null;
	
	@EJB
	private GenericPersistenceFacadeLocale localePersistence;
	
	public SessionBean(){
		login=false;
		loggedUser = null;
		username = "";
		password = "";
	}
	
	public ServerResponse invalidate(){
		login = false;
		loggedUser = null;
		username = "";
		password = "";
		return null;
	}
	
	public ServerResponse login(String username, String password, EntityMetadata metadata){
		List<User> users = (List<User>) ((ServerResponse)localePersistence.selectEntities(User.class, 100, null, null)).getData();
		this.username = username;
		this.password = password;
		
		ServerResponse response = new ServerResponse();
		
		if(users.size()==0 && username.equals("admin") && password.equals("admin")){
			login = true;
			loggedUser = new User();
			loggedUser.setFirstName("Admin");
			loggedUser.setLastName("Admin");
			loggedUser.setUsername(username);
			loggedUser.setPassword(password);
			loggedUser.setType(1);
			response.setSeverity(ServerResponse.NONE);
			response.setData(loggedUser);
		}else{
			Map<String, Object> loginMap = new HashMap<String, Object>();
			loginMap.put("Username", "="+username);
			loginMap.put("Password", "="+password);
			ServerResponse loadResponse = (ServerResponse)localePersistence.searchSelect(metadata, loginMap, 10, "id", null);
			users = (List<User>) loadResponse.getData();
			if(loadResponse.getSeverity()==ServerResponse.INFO){
				if(users.size() == 1){
					loggedUser = users.get(0);
					response.setSeverity(ServerResponse.NONE);
					response.setData(loggedUser);
					login = true;
				}else{
					loggedUser = null;
					login = false;
					response.setSeverity(ServerResponse.WARN);
					response.setResponseCode("LOGIN.LOGIN_FAILED");
					response.setData(null);
				}
			}else{
				response=loadResponse;
			}
		}
		
		return response;
	}
}
