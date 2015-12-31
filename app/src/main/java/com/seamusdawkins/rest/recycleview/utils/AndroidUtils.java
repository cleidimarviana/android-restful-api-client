package com.seamusdawkins.rest.recycleview.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AndroidUtils {

    /**
     * Check if there connection
     * @param context
     * @return
     */
    public static boolean thereConnection(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null){
            return  false;
        }

        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        if(info != null){
            for(int i = 0; i < info.length;i++){
                if(info[i].getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }
}
