package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import java.io.Serializable;
import java.util.List;

public class Friend implements Serializable {
    String chat;
    String name;

    public String getName() {
        return name;
    }

    public String getChat() {
        return chat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
