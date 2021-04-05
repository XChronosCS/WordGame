package com.example.wordgame;


//add dependencies to your class
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DictionaryRequest extends AsyncTask<String, Integer, String>{
    boolean isWord = Boolean.FALSE;
    final String app_id = "772d6539";
    final String app_key = "7b52239b3b7cdc48ad4d86077b52d597";
    private final String NEXT_MSG = "Create A Word From These Letters";
    private final String NO_EXIST = "This word does not exist. Try again";
    private TextView instruction;
    private int cur_score;
    private TextView score;
    private Context context;
    private ArrayList<String> usedWords;
    private String word;

    public DictionaryRequest(TextView ins, TextView score, Context con, ArrayList<String> uw, int sco, String wo){
        this.instruction = ins;
        this.cur_score = sco;
        this.score = score;
        this.context = con;
        this.usedWords = uw;
        this.word = wo;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }


    public int getCur_score() {
        return cur_score;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try{
            JSONArray js = new JSONArray(result);
            JSONObject ja = js.getJSONObject(0);
            isWord = ja.has("phonetics");
            if(!isWord){
                instruction.setText(NO_EXIST);
            }
            else{
                cur_score += 1;
                score.setText(Integer.toString(cur_score));
                usedWords.add(word);
                instruction.setText(NEXT_MSG);
                GamePageActivity activity = (GamePageActivity) context;
                activity.setScoreVal(cur_score);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            try{
                JSONObject js = new JSONObject(result);
                boolean isNotWord = js.has("title");
                if(isNotWord){
                    instruction.setText(NO_EXIST);
                }
                else{
                    cur_score += 1;
                    score.setText(Integer.toString(cur_score));
                    usedWords.add(word);
                    instruction.setText(NEXT_MSG);
                    GamePageActivity activity = (GamePageActivity) context;
                    activity.setScoreVal(cur_score);
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
                instruction.setText(NO_EXIST);
            }

        }


    }


}
