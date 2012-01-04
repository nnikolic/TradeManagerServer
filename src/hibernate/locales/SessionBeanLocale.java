package hibernate.locales;

import javax.ejb.Local;

import util.ServerResponse;

@Local
public interface SessionBeanLocale {
	public ServerResponse getKorisnik();
	public ServerResponse isLogin();
}
