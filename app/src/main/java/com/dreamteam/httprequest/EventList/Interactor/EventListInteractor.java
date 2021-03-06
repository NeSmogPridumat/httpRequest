package com.dreamteam.httprequest.EventList.Interactor;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;

import com.dreamteam.httprequest.Data.ConstantConfig;
import com.dreamteam.httprequest.Event.Entity.EventType4.EventType4;
import com.dreamteam.httprequest.EventList.Protocols.EventListFromHTTPManagerInterface;
import com.dreamteam.httprequest.EventList.Protocols.EventListPresenterInterface;
import com.dreamteam.httprequest.Data.HTTPConfig;
import com.dreamteam.httprequest.HTTPManager.HTTPManager;
import com.dreamteam.httprequest.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class EventListInteractor implements EventListFromHTTPManagerInterface {

    private HTTPConfig httpConfig = new HTTPConfig ();
    private HTTPManager httpManager = HTTPManager.get();
    private ConstantConfig constantConfig = new ConstantConfig();

    private EventListPresenterInterface delegate;

    public EventListInteractor (EventListPresenterInterface delegate){
        this.delegate = delegate;
    }

    //======================================REQUESTS====================================//

    public void getEvents(String userID){
        final String path = httpConfig.serverURL + httpConfig.SERVER_GETTER + httpConfig.EVENT
                + httpConfig.USER + httpConfig.USER_ID_PARAM + userID;
        //TODO путь для получения списка эвентов

        new Thread(new Runnable() {
            @Override
            public void run() {
                httpManager.getRequest(path, constantConfig.GET_EVENT_TYPE,
                        EventListInteractor.this);
            }
        }).start();
    }

    //=======================================ANSWERS========================================//

    @Override
    public void response(byte[] byteArray, String type) {
        if (type.equals(constantConfig.GET_EVENT_TYPE)){
            prepareGetEventsResponse(byteArray);
        }
    }

    @Override
    public void error(final Throwable t) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                delegate.error(t);
            }
        };
        mainHandler.post(myRunnable);
    }

    private void prepareGetEventsResponse (byte[] byteArray){
        if (byteArray != null){
            final ArrayList<EventType4> eventArrayList;
            try {
                eventArrayList = createEventsOfBytes(byteArray);
                if (eventArrayList == null){
                    delegate.error(new NullPointerException());
                } else {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            delegate.answerGetEvents(eventArrayList);
                        }
                    };
                    mainHandler.post(myRunnable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void errorHanding(int responseCode, String type) {
    }

    //====================================SUPPORT METHODS===================================//

    private ArrayList<EventType4> createEventsOfBytes (byte[] byteArray)throws Exception{

        Gson gson = new Gson();
        String jsonString = new String(byteArray);
        JSONArray jsonArray = new JSONArray(jsonString);
        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }

        ArrayList<EventType4> events = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            if(!(jsonArray.get(i).equals("null"))) {
                EventType4 event = gson.fromJson((list.get(i)), new TypeToken<EventType4>() {}.getType());
                events.add(event);
            }
        }
        return events;
    }

}
