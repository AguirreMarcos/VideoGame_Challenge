package es.marcosaguirre.videogame.common;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Response<T> {
	private T data;
	private List<Message> messages;
	
	public void addMessage (Message message) {
		if(this.messages == null) {
			this.messages = new ArrayList<>();	
		}
		this.messages.add(message);
	}
}
