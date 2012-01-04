package util;

import java.io.Serializable;

public class ServerResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5829932981549894414L;
	public static int NONE = -1;
	public static int INFO = 0;
	public static int WARN = 1;
	public static int ERROR = 2;
	
	private int severity;
	
	private Object data;
	
	private String responseCode, responseMessage;

	public String getResponseMessage() {
		return responseMessage;
	}
	
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
}
