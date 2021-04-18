package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import java.io.Serializable;


public class TempOperationResponse2 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1914409313002863470L;
	private String status;
	private Data data;
	private int page;
	private double total_pages;

	public double getTotal_pages() {
		return total_pages;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setTotal_pages(double total_pages) {
		this.total_pages = total_pages;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
