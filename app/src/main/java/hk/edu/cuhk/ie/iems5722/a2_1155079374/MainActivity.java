package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hk.edu.cuhk.ie.iems5722.a2_1155079374.R;

public class MainActivity extends AppCompatActivity {
    public static int UserID  = -1;
    public static  String ChatroomName = "";
    public static String UserName = "";
    public static List<Data> publicroomList = new ArrayList<>();
    public List<Integer> ChatroomList = new ArrayList<>();
    public static String userChatList = "";
    ConstraintLayout login,register,selectRoom;
    EditText username,password,reguser,regpassword,confirmpassword;
    TextView errormsg,regerrormsg;
    ImageButton registerbtn,sendregisterbtn;
    Button chatroombtn,loginbtn;
    public static boolean islogin = false;
    ImageButton addfreind;
    boolean isBound = false;
    RecyclerView roomlist;
    List<Data> namelist= new ArrayList<>();
    public static Data chatData = new Data();
    public static int ROOMID = -1;
    DisplayroomAdapter displayroomAdapter;
    private ServerConnectionService serverConnectionService = null;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBound = true;
            ServerConnectionService.ServerConnectionServiceBinder myBinder = (ServerConnectionService.ServerConnectionServiceBinder) binder;
            serverConnectionService = myBinder.getService();
            serverConnectionService.getChatRoom(new OnRequestListener() {
                @Override
                public void success(TempOperationResponse response) {
                    Log.d("Log", "Success");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            publicroomList.clear();
                            publicroomList.addAll(response.getData());
                            genChatroom();
//                            displayroomAdapter.update(response.getData());
//                            displayroomAdapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void fail(String reason) {
                    Log.d("Log", "Fail");
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Log.i("DemoLog", "ActivityA onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();
        loginbtn = findViewById(R.id.login_btn);
        login = findViewById(R.id.login);
        registerbtn = findViewById(R.id.register);
        register = findViewById(R.id.registerpanel);
        addfreind = findViewById(R.id.addfriend);
        selectRoom = findViewById(R.id.chatroomselectpanel);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        reguser = findViewById(R.id.regusername);
        regpassword = findViewById(R.id.regpassword);
        errormsg = findViewById(R.id.errormessage);
        regerrormsg = findViewById(R.id.regerrormessage);
        confirmpassword = findViewById(R.id.confirmpassword);
        sendregisterbtn = findViewById(R.id.reg);
        roomlist = findViewById(R.id.chatroomlist);
        displayroomAdapter = new DisplayroomAdapter(this,namelist,this);
        roomlist.setLayoutManager(new LinearLayoutManager(this));
        roomlist.setAdapter(displayroomAdapter);
        if(islogin){
            login.setVisibility(View.GONE);
            selectRoom.setVisibility(View.VISIBLE);
            genChatroom();
        }
        addfreind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!password.getText().toString().equals("")&&!username.getText().toString().equals("")){
                    Log.d("test","Login");
                    hideSystemUI();
                    serverConnectionService.login(password.getText().toString(), username.getText().toString(), new OnRequestListener3() {
                        @Override
                        public void success(TempOperationResponse3 response) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.getStatus().equals("OK")){
                                        Log.d("test","testset");
                                        UserName = username.getText().toString();
                                        islogin = true;
                                        login.setVisibility(View.GONE);
                                        selectRoom.setVisibility(View.VISIBLE);
                                        UserID = response.getData().getid();
                                        userChatList = response.getData().getChatroomlist();
                                        genChatroom();
                                        Log.d("test", response.getData().toString());
                                }
                                     else{
                                        Log.d("test","testset2");
                                        loginbtn.setEnabled(true);
                                        errormsg.setVisibility(View.VISIBLE);
                                        errormsg.setText(response.getMessage());
                                    }
                                }
                            });
                        }

                        @Override
                        public void fail(String reason) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("test","testset3");
                                    errormsg.setVisibility(View.VISIBLE);
                                    errormsg.setText(reason);
                                }
                            });
                        }
                    });
                }
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbtn.setEnabled(false);
                register.setVisibility(View.VISIBLE);
            }
        });
        sendregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reguser.getText().toString().length()<=6){
                    if(regpassword.getText().toString().length()>=8){
                        if(regpassword.getText().toString().equals(confirmpassword.getText().toString())){
                            sendregisterbtn.setEnabled(false);
                            serverConnectionService.registerUser(reguser.getText().toString(),regpassword.getText().toString(),new OnRequestListener3() {

                                @Override
                                public void success(TempOperationResponse3 response) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(response.getStatus().equals("OK")) {
                                                UserName = reguser.getText().toString();
                                                islogin = true;
                                                login.setVisibility(View.GONE);
                                                selectRoom.setVisibility(View.VISIBLE);
                                                UserID = response.getData().getid();
                                                userChatList = response.getData().getChatroomlist();
                                                genChatroom();
                                                hideSystemUI();
                                                Log.d("test", response.getData().toString());
                                            }
                                            else{
                                                sendregisterbtn.setEnabled(true);
                                                regerrormsg.setVisibility(View.VISIBLE);
                                                regerrormsg.setText(response.getMessage());
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void fail(String reason) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            regerrormsg.setVisibility(View.VISIBLE);
                                            regerrormsg.setText(reason);
                                        }
                                    });
                                }
                            });
                        }
                        else{
                            regerrormsg.setVisibility(View.VISIBLE);
                            regerrormsg.setText("Confirm password Wrong");
                        }
                    }
                    else{
                        regerrormsg.setVisibility(View.VISIBLE);
                        regerrormsg.setText("Password must be not less than 8 characters");
                    }
                }
                else{
                    regerrormsg.setVisibility(View.VISIBLE);
                    regerrormsg.setText("User Name must be less than 6 characters");
                }
            }
        });

    }

    public void toChatRoom(int id, String name){
        if(id>0){
            ROOMID = id;
            ChatroomName = name;
            serverConnectionService.getChatroommessage(id, 1, new OnRequestListener2() {
                @Override
                public void success(TempOperationResponse2 response) {
                    chatData = response.getData();
//                    Log.d("Log", chatData.getMessages().get(0).getName());
                    Intent intent = new Intent(MainActivity.this, chatActivity.class);
                    startActivity(intent);
                }

                @Override
                public void fail(String reason) {
                    Log.d("Log","fail");
                }
            });
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED ) {
            Intent intent = new Intent(this, ServerConnectionService.class);
            bindService(intent, conn, BIND_AUTO_CREATE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

    }

    public void openChat(View view) {
        Intent intent = new Intent(this, chatActivity.class);
        startActivity(intent);
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

    }

    private void genChatroom(){
//        serverConnectionService.getChatRoom(new OnRequestListener() {
//            @Override
//            public void success(TempOperationResponse response) {
//                Log.d("Log", "Success");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        publicroomList.clear();
//                        publicroomList.addAll(response.getData());
////                            displayroomAdapter.update(response.getData());
////                            displayroomAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//
//            @Override
//            public void fail(String reason) {
//                Log.d("Log", "Fail");
//            }
//        });
        String tmplist  = userChatList.replace(" ","");
        Log.d("test123",tmplist);
        if (tmplist.contains(",")) {
            while (tmplist.contains(",")) {
                String tmp = tmplist.substring(0, tmplist.indexOf(","));
                tmp = tmp.replace(" ","");
                try {
                    ChatroomList.add(Integer.parseInt(tmp));
                }
                catch (NumberFormatException e){
                    Toast.makeText(this,"done", Toast.LENGTH_SHORT).show();
                }
                tmplist = tmplist.substring(tmplist.indexOf(",") + 1);
            }
        }
        Log.d("test123",tmplist);
        tmplist = tmplist.replace(" ","");
        Log.d("test123",tmplist);
        try {
            ChatroomList.add(Integer.parseInt(tmplist));
        }
        catch (NumberFormatException e){
            Toast.makeText(this,"done", Toast.LENGTH_SHORT).show();
        }
        List<Data> newRoomList = new ArrayList<>();
        for (int id : ChatroomList) {
            for (Data room : publicroomList) {
                if (room.getId() == id) {
                    newRoomList.add(room);
                }
            }
            Log.d("test", String.valueOf(id));
        }
        hideSystemUI();
        ChatroomList.clear();
        displayroomAdapter.update(newRoomList);
        displayroomAdapter.notifyDataSetChanged();
    }
    public void logout(View view){
        selectRoom.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);
        register.setVisibility(View.GONE);
    }

}