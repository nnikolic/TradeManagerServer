package hibernate.remotes;

import javax.ejb.Remote;

import model.metadata.EntityMetadata;
import util.ServerResponse;

@Remote
public interface SessionBeanRemote {
	public ServerResponse invalidate();
	public ServerResponse login(String username, String password, EntityMetadata metadata);
}
