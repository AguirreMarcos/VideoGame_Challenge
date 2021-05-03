package es.marcosaguirre.videogame.common;

public class BaseException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;
	private String message;
	private String origin;
	
	public BaseException(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public BaseException(int code, String message, String origin) {
		this.code = code;
		this.message = message;
		this.origin = origin;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
