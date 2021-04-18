package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import java.io.Serializable;
import java.util.List;


public class TempOperationResponse3 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1914409313002863470L;
	private String status;
	private User data;
	private String message;




	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getData() {
		return data;
	}

	public void setData(User data) {
		this.data = data;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
