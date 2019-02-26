package com.suja.bakingapp.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

/**
 * Created by Suja Manu on 4/17/2018.
 */

public class NetworkUtil  {


    private static boolean online;
    private static String RECIPE_JSON_FILE="RecipeJSON.txt";

    public static String getJSONFromNetwork(String searchUrl) {
        String result = null;
        BufferedReader bReader = null;
        try {

            URL url = new URL(searchUrl);


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


            if (urlConnection.getResponseCode() == 200) {
                InputStream iStream = new BufferedInputStream(urlConnection.getInputStream());
                bReader = new BufferedReader(new InputStreamReader(iStream));

                StringBuilder sBuilder = new StringBuilder();
                String line;
                while ((line = bReader.readLine()) != null) {


                    sBuilder.append(line);
                }
                result = sBuilder.toString();
                //Log.i("result", result);

                urlConnection.disconnect();


            } else {
                return null;

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bReader != null)
                try {
                    bReader.close();
                } catch (IOException exp) {
                    Log.e("Reader close", "", exp);
                }
        }
        return result;
    }

    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (Exception e) {
            online = false;
            return false;
        }
    }

    public static  void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(RECIPE_JSON_FILE, Context.MODE_PRIVATE));
            //context.getAssets().open("config.txt");
            Log.i("networkUtil","Created successfully ");
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.i("NetworkUtil","file written");

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public static String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(RECIPE_JSON_FILE);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
                Log.i("NeworkUtil",ret);

            }
        }
        catch (FileNotFoundException e) {
            Log.e("NeworkUtil", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("NeworkUtil", "Can not read file: " + e.toString());
        }

        return ret;
    }



}
