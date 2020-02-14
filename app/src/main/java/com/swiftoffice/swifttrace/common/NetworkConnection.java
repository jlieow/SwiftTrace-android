package com.swiftoffice.swifttrace.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 *Internet check while Api calls
 */
public class NetworkConnection {
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

}
