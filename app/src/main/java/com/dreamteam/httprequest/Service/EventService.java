package com.dreamteam.httprequest.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.dreamteam.httprequest.Data.ConstantConfig;
import com.dreamteam.httprequest.Event.Entity.ChangeEvent.EventChoice;
import com.dreamteam.httprequest.Data.HTTPConfig;
import com.dreamteam.httprequest.HTTPManager.HTTPManager;
import com.dreamteam.httprequest.Interfaces.OutputHTTPManagerInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;

public class EventService extends Service implements OutputHTTPManagerInterface {

    private HTTPManager httpManager = HTTPManager.get();
    private HTTPConfig httpConfig = new HTTPConfig();
    private ConstantConfig constantConfig = new ConstantConfig();
    private PendingIntent pi;
    private Handler handler;

    public EventService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        String userID = intent.getStringExtra("userID");
        final String path = httpConfig.serverURL + httpConfig.SERVER_GETTER + httpConfig.EVENT + httpConfig.USER + httpConfig.USER_ID_PARAM + userID;

        new Thread(new Runnable() {
            @Override
            public void run() {
                httpManager.getRequest(path, constantConfig.GET_EVENT_TYPE,  EventService.this);
            }
        }).start();

        pi = intent.getParcelableExtra("Pending");

        handler = new Handler();
        final int delay = 5000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                //TODO путь для получения списка эвентов

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpManager.getRequest(path, constantConfig.GET_EVENT_TYPE,  EventService.this);
                    }
                }).start();

                handler.postDelayed(this, delay);
            }
        }, delay);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void response(byte[] byteArray, String type) {
        if (type.equals(constantConfig.GET_EVENT_TYPE)){

            prepareGetEventsResponse(byteArray);
        }
    }

    private void prepareGetEventsResponse (byte[] byteArray){
        if (byteArray != null){
            final ArrayList<EventChoice> eventArrayList;
            try {
                eventArrayList = createEventsOfBytes(byteArray);
                if (eventArrayList == null){
                    String error = " ";
                } else {
                    final String events = String.valueOf(eventArrayList.size());
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent().putExtra("Events", events);
                            try {
                                pi.send(EventService.this, 1, intent);
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    mainHandler.post(myRunnable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<EventChoice> createEventsOfBytes (byte[] byteArray)throws Exception{

        Gson gson = new Gson();
        String jsonString = new String(byteArray);
        JSONArray jsonArray = new JSONArray(jsonString);
        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }

        ArrayList<EventChoice> events = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            if(!(jsonArray.get(i).equals("null"))) {
                EventChoice event = gson.fromJson((list.get(i)), new TypeToken<EventChoice>() {}.getType());
                if (event.active) {
                    events.add(event);
                }
            }
        }
        return events;
    }

    @Override
    public void error(Throwable t) {

    }

    @Override
    public void errorHanding(int resposeCode, String type) {

    }

    @Override
    public void onDestroy() {
        //при вызове stopService() вызывается onDestroy() сервиса, но вызовы продолжают идти из-за того, что
        // "поток продолжает работать". Чтобы закрыть поток и прекратить запросы на сервер, надо вызвать у handler
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }
}
