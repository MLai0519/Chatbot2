package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 8701739145135194829L;
    private String id;
    private String chatroom_id;
    private String user_id;
    private String name;
    private String message;
    private String message_time;

    public String getChatroom_id() {
        return chatroom_id;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage_time() {
        return message_time;
    }

    public int getId() {
        if(id!=null)
            return Integer.parseInt(id);
        else
            return 0;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public void setId(String messageID) {
        this.id = messageID;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setName(String username) {
        this.name = username;
    }
}
