package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import java.io.Serializable;
import java.util.List;



public class TempOperationResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1914409313002863470L;
	private String status;
	private List<Data> data;
	private  Object request;


	public List<Data> getData() {
		return data;
	}

	public Object getRequest() {
		return request;
	}

	public void setRequest(Object request) {
		this.request = request;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
