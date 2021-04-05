package com.example.wordgame;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GamePageActivity extends AppCompatActivity {
    private static final int[] BUTTON_IDS = {
            R.id.letterButton1,
            R.id.letterButton2,
            R.id.letterButton3,
            R.id.letterButton4,
            R.id.letterButton5,
            R.id.letterButton6,
            R.id.letterButton7,
            R.id.letterButton8,
            R.id.letterButton9,
    };

    public void setScoreVal(int scoreVal) {
        this.scoreVal = scoreVal;
    }

    private int scoreVal;
    private ArrayList<String> usedWords;
    private ArrayList<Button> letterButtons;
    private ArrayList<String> alphabet;
    private char[] letters;
    private final String REP_MSG = "You already used the word. Try again";
    private final String NEXT_MSG = "Create A Word From These Letters";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        usedWords = new ArrayList<>();
        scoreVal = 0;
        TextView score = (TextView) findViewById(R.id.scoreNumber);
        TextView instruction = (TextView) findViewById(R.id.instruction);
        TextView wordInput = (TextView) findViewById(R.id.inputWord);
        wordInput.setText("");
        letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        alphabet = new ArrayList<String>();
        for(char ch : letters){
            alphabet.add(String.valueOf(ch));
        }
        //Defining all the buttons
        Random random = new Random();
        letterButtons = new ArrayList<Button>();
        for (int id : BUTTON_IDS) {
            Button button = (Button) findViewById(id);
            letterButtons.add(button);
            int letterValue = random.nextInt(alphabet.size());
            button.setText(alphabet.get(letterValue));
            alphabet.remove(letterValue);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wordInput.append(button.getText());
                }
            });
        }
        usedWords = new ArrayList<String>();
        Button enterButton = (Button) findViewById(R.id.enterButton);
        Context contextVar = this;
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = wordInput.getText().toString();
                if(usedWords.contains(word)){
                    instruction.setText(REP_MSG);
                }
                else{
                    Boolean isWord = Boolean.FALSE;
                    DictionaryRequest req = new DictionaryRequest(instruction, score, contextVar, usedWords, scoreVal, word);
                    String url = dictionaryEntries();
                    System.out.println(url);
                    req.execute(url);

                }
                wordInput.setText("");
            }
        });
        Button restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });

    }

    public void restart(){
        //reset the list of letters
        alphabet.clear();
        for(char ch : letters){
            alphabet.add(String.valueOf(ch));
        }
        //reset button letters
        Random random = new Random();
        for(Button button : letterButtons){
            int letterValue = random.nextInt(alphabet.size());
            button.setText(alphabet.get(letterValue));
            alphabet.remove(letterValue);
        }
        //reset score
        scoreVal = 0;
        TextView score = (TextView) findViewById(R.id.scoreNumber);
        score.setText(Integer.toString(scoreVal));
        TextView instruction = (TextView) findViewById(R.id.instruction);
        instruction.setText(NEXT_MSG);
        TextView wordInput = (TextView) findViewById(R.id.inputWord);
        wordInput.setText("");
        //
    }

    private String dictionaryEntries(){
        TextView inputWord = (TextView) findViewById(R.id.inputWord);
        final String language = "en_US";
        final String word = inputWord.getText().toString();
        final String word_id = word.toLowerCase();
        return "https://api.dictionaryapi.dev/api/v2/entries/" + language + "/" + word_id;
    }





}