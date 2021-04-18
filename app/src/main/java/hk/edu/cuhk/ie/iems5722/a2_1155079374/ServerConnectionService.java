package hk.edu.cuhk.ie.iems5722.a2_1155079374;


import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static android.content.ContentValues.TAG;


public class ServerConnectionService extends Service {


    String Host = "http://18.216.200.107/api/assgn3";
//    String Host = "http://3.136.86.187/api/a3";
    String get_chatroom = "/get_chatrooms";
    String get_message = "/get_messages?";
    String chatroom_id = "chatroom_id=";
    String Page = "page=";
    String addfriend ="/add_friends";
    String send_message = "/send_messages";
    String login = "/login";
    String register = "/register";
    String user_id = "user_id=";
    String name = "name=";
    String message = "message=";

    private HandlerThread mHandlerThread;
    private RestTemplate restTemplate ;
    public RestTemplate restTemplate(RestTemplate restTemplate) {
        restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        StringHttpMessageConverter converter2 = new StringHttpMessageConverter();
        messageConverters.add(converter2);
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }
    public RestTemplate restTemplate2() {
        restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        List<MediaType> mediaTypes = new ArrayList<>(16);
        mediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        mediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        mediaTypes.add(MediaType.APPLICATION_XML);
        mediaTypes.add(MediaType.APPLICATION_RSS_XML);
        mediaTypes.add(MediaType.APPLICATION_WILDCARD_XML);
        mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.TEXT_XML);
        mediaTypes.add(MediaType.MULTIPART_FORM_DATA);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        messageConverters.add(converter);
        converter.setSupportedMediaTypes(mediaTypes);
        FormHttpMessageConverter converter2 = new FormHttpMessageConverter();
        messageConverters.add(converter2);
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }
//    private JWTTokenHandler jwtTokenHandler = new JWTTokenHandler();

//    private GPSService gpsService = null;

    private boolean isBound = false;
    public void getChatRoom(OnRequestListener onRequestListener){
        sendRequest(Host + get_chatroom, HttpMethod.GET, onRequestListener,true,null,null);
    }

    public void registerUser(String password,String username, OnRequestListener3 onRequestListener){
        String url = Host + register;
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
        map.add("username",password);
        map.add("password",username);
        sendRequest2(url,HttpMethod.POST,onRequestListener,true,null,map);
    }

    public void getChatroommessage(int id, int page,OnRequestListener2 onRequestListener2){
        String url = Host + get_message + chatroom_id + id + "&" + Page + page;
        sendRequest(url, HttpMethod.GET, null,false,onRequestListener2,null);
    }

    public void addFriend(int userid,int peerid,OnRequestListener4 onRequestListener4){
        String url = Host + addfriend;
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
        map.add("id1",String.valueOf(userid));
        map.add("id2",String.valueOf(peerid));
        sendRequest2(url,HttpMethod.POST,null,false,onRequestListener4,map);

    }

    public void login(String username,String password, OnRequestListener3 onRequestListener){
        String url = "http://101.32.75.231/api" + login;
        Log.d("test","login");
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
        map.add("username",password);
        map.add("password",username);
        sendRequest2(url,HttpMethod.POST,onRequestListener,true,null,map);
    }

    public void sendMessage(Message messagesend, OnRequestListener onRequestListener){
        String url = Host + send_message;
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
        map.add("chatroom_id",messagesend.getChatroom_id());
        map.add("user_id",messagesend.getUser_id());
        map.add("name",messagesend.getName());
        map.add("message",messagesend.getMessage());
//        String reqeust = chatroom_id+messagesend.getChatroom_id()+"&"+user_id+messagesend.getUser_id()+"&"+name+messagesend.getName()+"&"+message+messagesend.getMessage();
        Log.d("Log",url);
        sendRequest(url,HttpMethod.POST,onRequestListener,true,null,map);
    }
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBound = true;
//            GPSService.GPSServiceBinder myBinder = (GPSService.GPSServiceBinder) binder;
//            gpsService = myBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Log.i("DemoLog", "ActivityA onServiceDisconnected");
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
//        Intent intent = new Intent(this, GPSService.class);
//        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        unbindService(conn);
    }


   public ServerConnectionService(){
       mHandlerThread = new HandlerThread("HandlerThread");
       mHandlerThread.start();
   }

    public void sendRequest2(String url, HttpMethod method, OnRequestListener3 onRequestListener3,Boolean isChat, OnRequestListener4 onRequestListener2, MultiValueMap<String, Object> map){

        HttpHeaders headers = new HttpHeaders();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(headers);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers2);
        Handler handler = new Handler(mHandlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    if(isChat) {
                        if(method.equals(HttpMethod.GET)) {
                            ResponseEntity<TempOperationResponse3> response = restTemplate(restTemplate).exchange(url, method, entity, TempOperationResponse3.class);
                            if (onRequestListener3 != null) {

                                if (response.getBody().getStatus().equals(ResponseCode.FAILED)) {

                                    onRequestListener3.fail("Error");
                                } else {
                                    onRequestListener3.success(response.getBody());
                                }
                            }
                        }
                        else{
                            RestTemplate restTemplate = new RestTemplate(true);
                            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                                    new MappingJackson2HttpMessageConverter();
                            mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
                                    Arrays.asList(
                                            MediaType.APPLICATION_JSON,
                                            MediaType.APPLICATION_OCTET_STREAM,
                                            MediaType.TEXT_PLAIN,
                                            MediaType.TEXT_XML,
                                            MediaType.TEXT_HTML));
                            restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
                            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                            ResponseEntity<TempOperationResponse3> response = restTemplate.exchange(url,method, request, TempOperationResponse3.class);
                            if (onRequestListener3 != null) {
                                if (response.getBody().getStatus().equals(ResponseCode.FAILED)) {
                                    Log.d("test",response.getBody().getMessage());
                                    onRequestListener3.fail(response.getBody().getMessage());
                                } else {
                                    Log.d("test","login get");
                                    onRequestListener3.success(response.getBody());
                                }
                            }
                        }
                    }
                    else{
                        Log.d("test","enter4");
                        RestTemplate restTemplate = new RestTemplate(true);
                        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                                new MappingJackson2HttpMessageConverter();
                        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
                                Arrays.asList(
                                        MediaType.APPLICATION_JSON,
                                        MediaType.APPLICATION_OCTET_STREAM,
                                        MediaType.TEXT_PLAIN,
                                        MediaType.TEXT_XML,
                                        MediaType.TEXT_HTML));
                        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
                        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                        ResponseEntity<TempOperationResponse4> response = restTemplate.exchange(url,method, request, TempOperationResponse4.class);
                        if (onRequestListener2 != null) {
                            if (response.getBody().getStatus().equals(ResponseCode.FAILED)) {
                                Log.d("test",response.getBody().getMessage());
                                Log.d("test","error here");
                                onRequestListener2.fail(response.getBody().getMessage());
                            } else {
                                Log.d("test","login get");
                                onRequestListener2.success(response.getBody());
                            }
                        }
                    }

                }catch (HttpClientErrorException e){
                    if(onRequestListener3 != null) {
                        if(e.getStatusCode().value() == 404){
                            onRequestListener3.fail("404 Error");
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    if(onRequestListener3 != null) {
                        onRequestListener3.fail("No network");
                    }
                }

            }
        });

    }


    public void sendRequest(String url, HttpMethod method, OnRequestListener onRequestListener,Boolean isChat, OnRequestListener2 onRequestListener2, MultiValueMap<String, Object> map){

        HttpHeaders headers = new HttpHeaders();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(headers);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers2);
        Handler handler = new Handler(mHandlerThread.getLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    if(isChat) {
                        if(method.equals(HttpMethod.GET)) {
                            ResponseEntity<TempOperationResponse> response = restTemplate(restTemplate).exchange(url, method, entity, TempOperationResponse.class);
                            if (onRequestListener != null) {

                                if (response.getBody().getStatus().equals(ResponseCode.FAILED)) {

                                    onRequestListener.fail("Error");
                                } else {
                                    onRequestListener.success(response.getBody());
                                }
                            }
                        }
                        else{
                            RestTemplate restTemplate = new RestTemplate(true);
                            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                                    new MappingJackson2HttpMessageConverter();
                            mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
                                    Arrays.asList(
                                            MediaType.APPLICATION_JSON,
                                            MediaType.APPLICATION_OCTET_STREAM,
                                            MediaType.TEXT_PLAIN,
                                            MediaType.TEXT_XML,
                                            MediaType.TEXT_HTML));
                            restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
                            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                            ResponseEntity<TempOperationResponse> response = restTemplate.exchange(url,method, request, TempOperationResponse.class);
                            if (onRequestListener != null) {

                                if (response.getBody().getStatus().equals(ResponseCode.FAILED)) {

                                    onRequestListener.fail("Error");
                                } else {
                                    onRequestListener.success(response.getBody());
                                }
                            }
                        }
                    }
                    else{
                        ResponseEntity<TempOperationResponse2> response = restTemplate(restTemplate).exchange(url, method, entity, TempOperationResponse2.class);
                        if (onRequestListener2 != null) {

                            if (response.getBody().getStatus().equals(ResponseCode.FAILED)) {

                                onRequestListener2.fail("Error");
                            } else {
                                onRequestListener2.success(response.getBody());
                            }
                        }
                    }

                }catch (HttpClientErrorException e){
                    if(onRequestListener != null) {
                        if(e.getStatusCode().value() == 404){
                            onRequestListener.fail("404 Error");
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    if(onRequestListener != null) {
                            onRequestListener.fail("No network");
                    }
                }

            }
        });

    }





    public class ServerConnectionServiceBinder extends Binder {
        public hk.edu.cuhk.ie.iems5722.a2_1155079374.ServerConnectionService getService(){
            return hk.edu.cuhk.ie.iems5722.a2_1155079374.ServerConnectionService.this;
        }

    }

    private ServerConnectionServiceBinder binder = new ServerConnectionServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("DemoLog", "TestService -> onUnbind, from:" + intent.getStringExtra("from"));
        return false;
    }



}