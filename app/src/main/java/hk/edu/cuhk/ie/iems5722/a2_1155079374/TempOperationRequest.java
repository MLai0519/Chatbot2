package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import java.io.Serializable;

public class TempOperationRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5319369524589903642L;
	private String chatroom_id;
	private String user_id;
	private String name;
	private String message;

	public String getName() {
		return name;
	}

	public String getUser_id() {
		return user_id;
	}

	public String getMessage() {
		return message;
	}

	public String getChatroom_id() {
		return chatroom_id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setChatroom_id(String chatroom_id) {
		this.chatroom_id = chatroom_id;
	}
}
