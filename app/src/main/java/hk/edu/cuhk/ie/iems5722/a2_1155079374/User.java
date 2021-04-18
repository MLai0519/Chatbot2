package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import java.io.Serializable;

public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8701739145135194829L;
    private int id;
    private String chatroomlist;


    public int getid() {
        return id;
    }

    public String getChatroomlist() {
        return chatroomlist;
    }

    public void setid(int id) {
        this.id = id;
    }

    public void setChatroomlist(String chatroomlist) {
        this.chatroomlist = chatroomlist;
    }
}
