package com.dreamteam.httprequest.EventList.Protocols;

import com.dreamteam.httprequest.Event.Entity.EventType4.EventType4;

import java.util.ArrayList;

public interface EventListPresenterInterface {

    void error (Throwable t);

    void answerGetEvents (ArrayList<EventType4> eventArrayList);
}
