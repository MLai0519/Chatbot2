package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import java.io.Serializable;
import java.util.List;

public class LoginData implements Serializable {
    private List<User> login_data;

    public List<User> getLogin_data() {
        return login_data;
    }

    public void setLogin_data(List<User> login_data) {
        this.login_data = login_data;
    }
}
