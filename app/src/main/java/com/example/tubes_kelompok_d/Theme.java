package com.example.tubes_kelompok_d;

import android.content.Context;
import android.content.SharedPreferences;

public class Theme  {
    SharedPreferences tema;
    public Theme (Context context){
        tema = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor = tema.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }
    public boolean loadNightModeState(){
        Boolean state = tema.getBoolean("NightMode",false);
        return state;
    }
}
