package es.marcosaguirre.videogame.common;

import lombok.Data;

@Data
public class Message {

	private String message;
	private Integer code;
	private String type;
	private String origin;
	
	public Message(String message) {
		this.message = message;
	}
	
	public Message(Integer code, String message) {
		this.message = message;
		this.code = code;
	}
	
	public Message(Integer code, String message, String origin) {
		this.message = message;
		this.code = code;
		this.origin = origin;
	}
	
	public Message(Integer code, String message, String type, String origin) {
		this.message = message;
		this.code = code;
		this.origin = origin;
		this.type = type;
	}
	
}
