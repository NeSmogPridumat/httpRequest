package com.dreamteam.httprequest.User;

import com.dreamteam.httprequest.AddOrEditInfoProfile.Data.InfoProfileData;
import com.dreamteam.httprequest.Interfaces.PresenterInterface;
import com.dreamteam.httprequest.MainActivity;
import com.dreamteam.httprequest.ObjectList.ObjectData;
import com.dreamteam.httprequest.User.Entity.UserData.User;
import java.util.ArrayList;

public class Router {

    private MainActivity activity;

    public Router (MainActivity activity){
        this.activity = activity;
    }

    public void showEditInfoProfile(InfoProfileData infoProfileData, PresenterInterface delegate, String type){
        activity.openEditProfile(infoProfileData,null, delegate, type);
    }

    public void showUser (User user){
        activity.showUser(user);
    }

    public void openProfile(){
        activity.openProfile();
    }

    public void openGroupList(ArrayList<ObjectData> objectDataArrayList, PresenterInterface delegate, String type){
        activity.openObjectList(objectDataArrayList, delegate, type);
    }
}
