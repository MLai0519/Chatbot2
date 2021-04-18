package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1914409313002863470L;
    private int id;
    private String name;
    private List<Message> messages;
//    private List<Message> message;
    private String current_page;
    private int total_page;

    public String getCurrent_page() {
        return current_page;
    }

    public int getId() {
        return id;
    }

//    public void setMessage(List<Message> message) {
//        this.message = message;
//    }
//
//    public List<Message> getMessage() {
//        return message;
//    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> message) {
        this.messages = message;
    }

    public String getName() {
        return name;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }
}
