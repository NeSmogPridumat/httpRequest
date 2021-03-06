package com.dreamteam.httprequest.ObjectList;

import com.dreamteam.httprequest.MainActivity;

public class ObjectListRouter {

  private MainActivity activity;

  public ObjectListRouter(MainActivity activity){
    this.activity = activity;
  }

  public void showUserObjectList(String id){
    activity.openUser(id);
  }

  public void showGroup(String id, int rules){
    activity.openGroup(id, rules);
  }
}
