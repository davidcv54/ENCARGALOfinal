package mx.com.encargalo.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constants {
    public static String userSignUp = "user_signup";
    public static String email = "email";
    public static String AUTH_ID = "firebase_id";
    public static String image = "image";
    public static String name = "name";
    public static String fcmId = "fcm_id";
    public static String type = "type";
    public static String PROFILE = "profile";
    public static String mobile = "mobile";
    public static String REFER_CODE = "refer_code";
    public static String FRIENDS_CODE = "friends_code";
    public static  String URL_BASE = "http://192.168.101.85/APIEncargalo/";

    public static boolean isNetworkAvailable(Activity activity) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
