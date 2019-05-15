package com.dreamteam.httprequest.Event.Interactor;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dreamteam.httprequest.Data.ConstantConfig;
import com.dreamteam.httprequest.Event.Entity.AnswerQuestion.AnswerQuestion;
import com.dreamteam.httprequest.Event.Protocols.EventFromHTTPManagerInterface;
import com.dreamteam.httprequest.Event.Protocols.EventPresenterInterface;
import com.dreamteam.httprequest.HTTPConfig;
import com.dreamteam.httprequest.HTTPManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static android.support.constraint.Constraints.TAG;

public class EventInteractor implements EventFromHTTPManagerInterface {

    private EventPresenterInterface delegate;
    private HTTPConfig httpConfig= new HTTPConfig();
    private HTTPManager httpManager = HTTPManager.get();

    private ConstantConfig constantConfig = new ConstantConfig();


    public EventInteractor(EventPresenterInterface delegate){
        this.delegate = delegate;
    }

    @Override
    public void response(byte[] byteArray, String type) {
        if (type.equals(constantConfig.ANSWER_FOR_EVENT_TYPE)){
            Log.i("Вроде","Что-то ок");
            eventAnswer();
        } else if (type.equals(constantConfig.RESULT_TO_QUESTION_TYPE)){
            answerServerToQuestion();
        }
    }

    private void eventAnswer(){
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                delegate.answerEvent();

            }
        };
        mainHandler.post(myRunnable);
    }

    @Override
    public void error(Throwable t) {
        String title = null;
        String description  = null;
        if (t instanceof SocketTimeoutException) {
            title = "Ошибка соединения с сервером";
            description = "Проверте соединение с интернетом. Не удается подключится с серверу";
        }
        if (t instanceof NullPointerException) {
            title = "Объект не найден";
            description = "";
        }
        Handler mainHandler = new Handler(Looper.getMainLooper());
        final String finalTitle = title;
        final String finalDescription = description;
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                delegate.error(finalTitle, finalDescription);
            }
        };
        mainHandler.post(myRunnable);
        Log.e(TAG, "Failed server" + t.toString());
    }

    @Override
    public void errorHanding(int resposeCode) {

    }

    public void answerEvent (final AnswerQuestion eventResponse){
        final String path = httpConfig.serverURL + httpConfig.SERVER_SETTER + httpConfig.EVENT + httpConfig.USER;
        new Thread(
                new Runnable() {//---------------------------------------------------------------запуск в фоновом потоке
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        String jsonObject = gson.toJson(eventResponse);
                        try {
                            httpManager.postRequest(path, jsonObject, constantConfig.ANSWER_FOR_EVENT_TYPE,
                                    EventInteractor.this);//----------отправка в HTTPManager
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }

    public void resultToQuestion (final AnswerQuestion answerQuestion){
        final String path = httpConfig.serverURL + httpConfig.SERVER_SETTER + httpConfig.EVENT + httpConfig.USER;
        new Thread(
                new Runnable() {//---------------------------------------------------------------запуск в фоновом потоке
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        String jsonObject = gson.toJson(answerQuestion);
                        try {
                            httpManager.postRequest(path, jsonObject, constantConfig.RESULT_TO_QUESTION_TYPE,
                                    EventInteractor.this);//----------отправка в HTTPManager
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }

    private void answerServerToQuestion(){
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                delegate.answerServerToQuestion();
            }
        };
        mainHandler.post(myRunnable);
    }

}
