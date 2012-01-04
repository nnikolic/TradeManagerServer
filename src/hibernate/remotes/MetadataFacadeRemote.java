package hibernate.remotes;

import javax.ejb.Remote;

import util.ServerResponse;

@Remote
public interface MetadataFacadeRemote {
	public ServerResponse getEntityXml(String filename);
	public ServerResponse getMenuXml();
}
