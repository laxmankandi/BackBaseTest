package com.backbasetest.main.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

    public static String convertInputStreamToString(InputStream inputStream){

        BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String eachLine = null;

            try {
                while (((eachLine = bufferedInputStream.readLine()) != null)) {
                    stringBuilder.append(eachLine + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return stringBuilder.toString();


    }
}
