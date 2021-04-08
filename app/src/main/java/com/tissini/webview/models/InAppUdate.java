package com.tissini.webview.models;

import android.app.Activity;
import android.content.IntentSender;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

public class InAppUdate {
    private static final int REQUEST_APP_UPDATE = 55;
    private Activity activity;
    private AppUpdateManager appUpdateManager;

    public InAppUdate(Activity activity){
        this.activity = activity;
    }

    public void getAppUdate(){
        appUpdateManager = AppUpdateManagerFactory.create(this.activity);

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ){
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,this.activity,REQUEST_APP_UPDATE);
                }catch (IntentSender.SendIntentException e){
                    e.printStackTrace();
                }
            }
        });

    }

    public void onResume(){
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo ->{
            if((appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS)){
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,this.activity,REQUEST_APP_UPDATE);
                }catch (IntentSender.SendIntentException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
