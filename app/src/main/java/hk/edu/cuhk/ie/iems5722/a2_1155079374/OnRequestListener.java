package hk.edu.cuhk.ie.iems5722.a2_1155079374;


public interface OnRequestListener {
    void success(TempOperationResponse response);
    void fail(String reason);
}
