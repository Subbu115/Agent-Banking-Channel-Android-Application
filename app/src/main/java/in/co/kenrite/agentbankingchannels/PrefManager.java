package in.co.kenrite.agentbankingchannels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PrefManager {
    public static String JWT_TOKEN = "JWT";
    public static String PARAMS_PASSWORD = "password";

    Context context;

    public PrefManager(Context context) {
        this.context = context;
    }

    public void saveLoginDetails(String mobileno, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Mobileno", mobileno);
        editor.putString("Password", password);
        editor.commit();
    }

    public void putString(String key, String value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("in.co.kenrite.agentbankingchannels.file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        Log.d("JWT-SAVED", value);
    }

    public String getString(String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("in.co.kenrite.agentbankingchannels.file", Context.MODE_PRIVATE);
        Log.d("JWT-TAKEN", sharedPreferences.getString(key, ""));
        return sharedPreferences.getString(key, "");
    }


    public String getMobile() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Mobileno", "");
    }

    public boolean isUserLogedOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isMobileEmpty = sharedPreferences.getString("Mobileno", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("Password", "").isEmpty();
        return isMobileEmpty || isPasswordEmpty;
    }
}
