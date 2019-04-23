package com.dreamteam.httprequest.GroupList;

import com.dreamteam.httprequest.AddOrEditInfoProfile.InfoProfileData;
import com.dreamteam.httprequest.Interfaces.PresenterInterface;
import com.dreamteam.httprequest.MainActivity;
import com.dreamteam.httprequest.SelectedList.SelectListData;

import java.util.ArrayList;

public class RouterGroupList {
    MainActivity activity;

    public RouterGroupList (MainActivity activity){
        this.activity = activity;
    }

    //показать список групп
    public void showGroupList(){
        activity.openGroupList();
    }

    //показать список с checkBox
    public void showSelectList(ArrayList<SelectListData> selectListData, PresenterInterface delegate, MainActivity activity, String TYPE){
        activity.openSelectList(selectListData,delegate, TYPE);
    }

    public void openGroup(String id){
        activity.openGroup(id);
    }
    public void showAddGroup(InfoProfileData infoProfileData, PresenterInterface delegate, String type){
        activity.openEditProfile(infoProfileData, delegate, type);
    }

}
