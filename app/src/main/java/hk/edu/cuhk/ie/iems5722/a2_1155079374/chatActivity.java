package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static hk.edu.cuhk.ie.iems5722.a2_1155079374.MainActivity.ChatroomName;
import static hk.edu.cuhk.ie.iems5722.a2_1155079374.MainActivity.ROOMID;
import static hk.edu.cuhk.ie.iems5722.a2_1155079374.MainActivity.UserID;
import static hk.edu.cuhk.ie.iems5722.a2_1155079374.MainActivity.UserName;
import static hk.edu.cuhk.ie.iems5722.a2_1155079374.MainActivity.chatData;

public class chatActivity extends AppCompatActivity {
    RecyclerView chatlist;
    private Socket socket;
    DisplayDataAdapter displayDataAdapter;
    List<Message> chats = new ArrayList<>();
    ImageButton sendbtn;
    TextView chatroomname;
    EditText input;
    int currentpage = 1;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        updateUI();
        String Chat = ChatroomName;
        Chat = Chat.replace(UserName,"");
        Chat = Chat.replace("&","");
        Chat =  Chat.replace(" ","");
        chatroomname = findViewById(R.id.chatroomname);
        sendbtn = findViewById(R.id.send);
        input = findViewById(R.id.input);
        chatlist = findViewById(R.id.chatlist);
        chats.addAll(chatData.getMessages());
        displayDataAdapter = new DisplayDataAdapter(this,chats,this);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        chatlist.setLayoutManager(llm);
        chatlist.setAdapter(displayDataAdapter);
        chatroomname.setText(Chat);
        try{
            socket = IO.socket("http://18.216.200.107:8001");
//            socket = IO.socket("http://3.136.86.187:8001");
            socket.on(Socket.EVENT_CONNECT, onConnectSuccess);
            socket.on("join_room", onJoinRoom);
            socket.on("leave_room", onJoinRoom);
            socket.on("messages", onTextUpdate);
            socket.on("join", onConnectSuccess);
            socket.on("receive_message", onTextUpdate2);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        chatlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(-1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    ++currentpage;
                    if(currentpage<=chatData.getTotal_page()) {
                        serverConnectionService.getChatroommessage(ROOMID, currentpage,
                        new OnRequestListener2() {
                            @Override
                            public void success(TempOperationResponse2 response) {
                                chatData = response.getData();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (chatData.getMessages() != null)
                                            chats.addAll(chatData.getMessages());
//                        else
//                            if(chatData.getMessage()!=null) {
//                                Log.d("log","lg");
//                                chats.addAll(chatData.getMessage());
//                            }
//                                        Collections.sort(chats, new Comparator<Message>() {
//                                            @Override
//                                            public int compare(Message o1, Message o2) {
//                                                return Integer.valueOf(o1.getId()).compareTo(o2.getId());
//                                            }
//                                        });
                                        displayDataAdapter.update(chats);
                                        displayDataAdapter.notifyDataSetChanged();
                                        input.setText("");
                                    }
                                });
//                Log.d("Log", chatData.getMessages().get(0).getName());
                            }

                            @Override
                            public void fail(String reason) {
                                Log.d("Log", "fail");
                            }
                        });
                    }
                }
            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
               updateUI();
                if(!input.getText().toString().equals("")){
                    Message tmpchat = new Message();
                    tmpchat.setMessage(input.getText().toString());
                    tmpchat.setMessage_time(sdf_time.format(new Date()));
                    tmpchat.setChatroom_id(String.valueOf(ROOMID));
                    tmpchat.setId(String.valueOf(chatData.getMessages().size()));
                    tmpchat.setUser_id(String.valueOf(UserID));
                    tmpchat.setName(UserName);
//                    socket.emit("my event", input.getText().toString());
                    serverConnectionService.sendMessage(tmpchat, new OnRequestListener() {
                        @Override
                        public void success(TempOperationResponse response) {
                            if(response.getStatus().equals("OK"))
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    chats.clear();
//                                    chats.addAll(chatData.getMessages());
//                                    chats.add(0,tmpchat);
//                                    displayDataAdapter.update(chats);
//                                    displayDataAdapter.notifyDataSetChanged();
                                    input.setText("");
                                }
                            });
                            else
                                Log.d("Log","Fail : "+response.getRequest());
                        }

                        @Override
                        public void fail(String reason) {
                            Log.d("log", "fail");
                        }
                    });
                }
            }
        });
    }

    public void refresh(View view){
        serverConnectionService.getChatroommessage(ROOMID, 1, new OnRequestListener2() {
            @Override
            public void success(TempOperationResponse2 response) {
                chatData = response.getData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chats.clear();
                        currentpage = 1;
                        if(chatData.getMessages()!=null)
                            chats.addAll(chatData.getMessages());
//                        else
//                            if(chatData.getMessage()!=null) {
//                                Log.d("log","lg");
//                                chats.addAll(chatData.getMessage());
//                            }
                        displayDataAdapter.update(chats);
                        displayDataAdapter.notifyDataSetChanged();
                        input.setText("");
                    }
                });
//                Log.d("Log", chatData.getMessages().get(0).getName());
            }

            @Override
            public void fail(String reason) {
                Log.d("Log","fail");
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this, ServerConnectionService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    public void back(View view) {
        socket.off();
        socket.disconnect();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void updateUI() {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });
    }
    private Emitter.Listener onConnectSuccess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Intent intent = getIntent();
            String intentstring = intent.getDataString();
            Log.d("test", "Test");
            Log.d("test", "test"+intentstring);
            String chatroom_id = intent.getStringExtra("chatroom_id");
            JSONObject roomjson = new JSONObject();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        roomjson.put("chatroom_id",ROOMID);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                    socket.emit("join",roomjson);
                    Log.d("test", "Connected");
                }
            } ) ;
        }
    } ;
    private Emitter.Listener onTextUpdate = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call (Object... args) {
            try {
                JSONObject data = (JSONObject) args[0];
                final String text = data.getString("message");
                String time = data.getString("time");
                String name = data.getString("name");
                String user_id = data.getString("user_id");
                String chatroom_id = data.getString("chatroom_id");
                if (Integer.parseInt(chatroom_id) == ROOMID) {
//                    if(!user_id.equals(String.valueOf(UserID))) {
                        Message tmpchat = new Message();
                        tmpchat.setMessage(text);
                        tmpchat.setMessage_time(time);
                        tmpchat.setChatroom_id(String.valueOf(ROOMID));
//                        tmpchat.setId(String.valueOf(chatData.getMessages().size()));
                        tmpchat.setUser_id(user_id);
                        tmpchat.setName(name);
                        chats.add(0,tmpchat);
//                        NotificationChannel notiChannel = new NotificationChannel("001","test",NotificationManager.IMPORTANCE_DEFAULT);
//                        Intent intent = new Intent(chatActivity.this,chatActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("chatroom ID", ROOMID);
//                        PendingIntent pendingIntent = PendingIntent.getActivity(chatActivity.this, 1, intent, 0);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(chatActivity.this)
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentTitle("Message from:" + ROOMID)
                                .setContentText(text);

                        // Obtain NotificationManager system service in order to show the notification
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        {
                            String channelId = "Your_channel_id";
                            NotificationChannel channel = new NotificationChannel(
                                    channelId,
                                    "Channel human readable title",
                                    NotificationManager.IMPORTANCE_HIGH);
                            notificationManager.createNotificationChannel(channel);
                            mBuilder.setChannelId(channelId);
                        }

                        notificationManager.notify(0, mBuilder.build());
//                        notificationManager.notify(1, mBuilder.build());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayDataAdapter.update(chats);
                                displayDataAdapter.notifyDataSetChanged();
                                Log.d("test", "broadcast" + text);
                            }
                        });
                    }
//                }
            } catch(JSONException e){
                    e.printStackTrace();
                }
        }
    } ;

    private Emitter.Listener onTextUpdate2 = new Emitter.Listener() {
        @Override
        public void call (Object... args) {
            try{
                JSONObject data = (JSONObject) args[0];
                final String text = data.getString("message");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("test", "broadcast2"+text);
                    }
                } ) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    } ;

    private Emitter.Listener onJoinRoom = new Emitter.Listener() {
        @Override
        public void call (Object... args) {
            try{
                JSONObject data = (JSONObject) args[0];
                String socketchatroom = data.getString("chatroom_id");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("test", socketchatroom);
                    }
                } ) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    } ;




}
