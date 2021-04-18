package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static hk.edu.cuhk.ie.iems5722.a2_1155079374.MainActivity.UserID;
import static hk.edu.cuhk.ie.iems5722.a2_1155079374.MainActivity.userChatList;

public class AddFriendActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    ImageButton addfreind,qrcode;
    ImageView userQRcode;
    TextView errormsg;
    EditText userID;
    private ServerConnectionService serverConnectionService = null;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            ServerConnectionService.ServerConnectionServiceBinder myBinder = (ServerConnectionService.ServerConnectionServiceBinder) binder;
            serverConnectionService = myBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("DemoLog", "ActivityA onServiceDisconnected");
        }
    };
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
//        mScannerView.startCamera();
        setContentView(R.layout.activity_addfriend);
        mScannerView = findViewById(R.id.scan);
        userID = findViewById(R.id.userid);
        errormsg = findViewById(R.id.adderrormsg);
        qrcode = findViewById(R.id.qrcode);
        addfreind = findViewById(R.id.add);
        userQRcode = findViewById(R.id.userqrcode);
        Intent intent = new Intent(this, ServerConnectionService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        addfreind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userID.getText().toString().equals("")){
                    int peerid = -1;
                    try
                    {
                        peerid = Integer.parseInt(userID.getText().toString());
                    }
                    catch (NumberFormatException e)
                    {
                        Toast.makeText(AddFriendActivity.this, "Please input number", Toast.LENGTH_SHORT).show();
                    }
                    if(peerid!=-1)
                        addFriend(peerid);
                }
            }
        });
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userQRcode.setVisibility(View.VISIBLE);
                mScannerView.setVisibility(View.GONE);
                mScannerView.stopCamera();
                String temp ="USER_ID:"+UserID;
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension / 4;

                qrgEncoder = new QRGEncoder(
                        temp, null,
                        QRGContents.Type.TEXT,
                        smallerDimension);
                qrgEncoder.setColorBlack(Color.BLACK);
                qrgEncoder.setColorWhite(Color.WHITE);
                try {
                    bitmap = qrgEncoder.getBitmap();
                    userQRcode.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
    @Override
    public void handleResult(Result rawResult) {
        if(rawResult.getText().contains("USER_ID:")) {
            userID.setText(rawResult.getText().substring(rawResult.getText().indexOf(":")+1));
            mScannerView.stopCamera();
            mScannerView.setVisibility(View.GONE);
            addFriend(Integer.parseInt(rawResult.getText().substring(rawResult.getText().indexOf(":")+1)));
        }

    }
    public void addFriend(int peerid){
        addfreind.setEnabled(false);
        serverConnectionService.addFriend(UserID, peerid, new OnRequestListener4() {
            @Override
            public void success(TempOperationResponse4 response) {
                if(response.getStatus().equals("OK")){
                    userChatList = response.getData().getChat();
                    back();
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addfreind.setEnabled(true);
                            errormsg.setVisibility(View.VISIBLE);
                            errormsg.setText(response.getMessage());
                        }
                    });
                }
            }

            @Override
            public void fail(String reason) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addfreind.setEnabled(true);
                        Log.d("test","error here");
                        errormsg.setVisibility(View.VISIBLE);
                        errormsg.setText(reason);
                    }
                });
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
    public void back(View view) {
        back();
    }
    public void back(){
        mScannerView.stopCamera();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

