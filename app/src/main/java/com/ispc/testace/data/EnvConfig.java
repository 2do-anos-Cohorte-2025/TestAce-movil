package com.ispc.testace.data;

import io.github.cdimascio.dotenv.Dotenv;


public class EnvConfig {
    private static  final Dotenv dotenv = Dotenv.load();

    public  static  String getApiBaseUrl(){
        // el default value seria el localhost del emulador android
        return  dotenv.get("API_BASE_URL","http://10.0.2.2:8000/");
    }
}
