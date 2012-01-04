package exceptions.login;

public class BadUserPassException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1064277922716251706L;
	
	public BadUserPassException(){
	}
	
	public BadUserPassException(String description){
		super(description);
	}

}
