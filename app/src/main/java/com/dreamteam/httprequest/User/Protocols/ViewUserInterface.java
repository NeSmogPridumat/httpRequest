package com.dreamteam.httprequest.User.Protocols;

import android.graphics.Bitmap;

import com.dreamteam.httprequest.User.Entity.UserData.User;

public interface ViewUserInterface {
    void View(User user);

    void ViewImage(Bitmap bitmap);

    void  error (String title, String description);

    void answerGetGroups(int groups);
}
