package com.dreamteam.httprequest.User.Presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.dreamteam.httprequest.AddOrEditInfoProfile.InfoProfileData;
import com.dreamteam.httprequest.Data.RequestInfo;
import com.dreamteam.httprequest.MainActivity;
import com.dreamteam.httprequest.User.Protocols.PresenterUserInterface;
import com.dreamteam.httprequest.User.Router;
import com.dreamteam.httprequest.SelectedList.SelectListData;
import com.dreamteam.httprequest.User.Entity.UserData.User;
import com.dreamteam.httprequest.User.Interactor.UserInteractor;
import com.dreamteam.httprequest.User.Protocols.ViewUserInterface;

import java.util.ArrayList;

public class PresenterUser implements PresenterUserInterface {

    public ViewUserInterface delegate;
    public MainActivity activity;
    public Router router;

    private String type = "User";

    UserInteractor userInteractor = new UserInteractor(this);

    public PresenterUser(ViewUserInterface delegate, MainActivity activity){
        this.delegate = delegate;
        this.activity = activity;
        router = new Router(activity);
    }

    @Override
    public void answerGetUser(User user) {
        long threadId = Thread.currentThread().getId();
        Log.i("","Thread # " + threadId + " is doing this task");
        delegate.View(user);
    }

    public void answerGetImage (Bitmap bitmap){
        delegate.ViewImage(bitmap);
    }

    @Override
    public void error(String error) {
        delegate.error(error);
    }

    @Override
    public void answerGetGroups(int groups) {
        delegate.answerGetGroups(groups);
    }

    @Override
    public void openUser() {
        router.openUser();
    }

    public void getUser(String id){
        userInteractor.getUser(id);
    }

    public void postUser(String name, String surname){
        userInteractor.postUser(name, surname);
    }

    public void getUsers (){

    }

    //отправка на объекта на изменение
    public void showEditProfile(User user, Bitmap bitmap){
        InfoProfileData infoProfileData = new InfoProfileData();
        infoProfileData.id = user.id;
        infoProfileData.title = user.content.simpleData.name;
        infoProfileData.description = user.content.simpleData.surname;
        infoProfileData.imageData = bitmap;
        router.showEditInfoProfile(infoProfileData,this, type);
     }

    @Override
    public void showDialog() {

    }

    @Override
    public void answerDialog(int i) {

    }

    @Override
    public void forResult(Bitmap bitmap) {

    }

    @Override
    public void inputSelect(ArrayList<SelectListData> arrayList, String type) {

    }

    //получение измененных данных и отпрака их в Interactor
    @Override
    public void editInfo(InfoProfileData infoProfileData, RequestInfo requestInfo) {
        User user = new User();
        user.id = infoProfileData.id;
        user.content.simpleData.name = infoProfileData.title;
        user.content.simpleData.surname = infoProfileData.description;
        Bitmap bitmap = infoProfileData.imageData;

        userInteractor.putUser(user, bitmap);
    }
}