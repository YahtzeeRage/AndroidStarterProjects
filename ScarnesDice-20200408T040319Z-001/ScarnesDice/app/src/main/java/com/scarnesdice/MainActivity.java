package com.scarnesdice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int WINNING_SCORE = 100;
    private int overallUserScore = 0;
    private int overallCpuScore = 0;

    private int turnScore = 0;
//    private int turnCpuScore = 0;

    private Random random = new Random();

    private boolean playerTurnActive = true;
    private boolean resetActive = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setScores(0, 0, 0, true);


        Button Roll = (Button) findViewById(R.id.Roll);

        Roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playerTurnActive && (overallUserScore <= WINNING_SCORE || overallCpuScore <= WINNING_SCORE)){
                    return;
                }

                boolean turnActive = rollDice(true);
                if (!turnActive){
                    allowPlayerInput(false);
                    allowResetting(false);
                    cpuTurn();
                }
            }
        });

        Button Hold = (Button) findViewById(R.id.Hold);

        Hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (!playerTurnActive && (overallUserScore <= WINNING_SCORE || overallCpuScore <= WINNING_SCORE)){
                    return;
                }

                holdDice(true);
                //turnScore = 0;
                //setScores(overallUserScore, overallCpuScore, turnScore, false);


            }
        });

        Button Reset = (Button) findViewById(R.id.Reset);

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!resetActive){
                    return;
                }

                setScores(0, 0, 0, true);
                allowPlayerInput(true);
            }
        });
    }

    public void cpuTurn(){
        Handler cpuHandler = new Handler();
        cpuHandler.postDelayed(new Runnable() {
            public void run() {
                if((overallUserScore >= WINNING_SCORE || overallCpuScore >= WINNING_SCORE)){
                    return;
                }
                boolean turnActive = rollDice(false);

                if ( turnActive){
                    if (turnScore <= 15){
                        cpuTurn();
                    }
                    else{
                        holdDice(false);
                        allowPlayerInput(true);
                    }
                }
                else{
                    allowPlayerInput(true);
                    allowResetting(true);
                    turnScore = 0;
                }
            }
        }, 1500);
    }

    // can also use onClick attribute in the button to be neat
    public void onResetClick(View v){
        Log.d("beep", "heelp");
    }


    public void setScores(int userScore, int cpuScore, int turnScore, boolean isPlayerTurn){

        this.overallUserScore = userScore;
        this.overallCpuScore = cpuScore;
        this.turnScore = turnScore;
//        if (isPlayerTurn){
//            turnUserScore = turnScore;
//        }
//        else{
//            turnCpuScore = turnScore
//        }


        TextView scoresView = (TextView) findViewById(R.id.Scores);

        StringBuilder SB = new StringBuilder();
        SB.append("My Score: ");
        SB.append(userScore);
        SB.append(" CPU Score: ");
        SB.append(cpuScore);

        if (isPlayerTurn){
            SB.append(" MY ");
        }
        else{
            SB.append(" CPU ");
        }

        SB.append("TURN Score: ");
        SB.append(turnScore);

        scoresView.setText(SB.toString());

    }

    public void setCorrectImage(int diceNumber){
        ImageView diceImage = (ImageView) findViewById(R.id.DiceImage);


        switch(diceNumber){
            case 1:
                diceImage.setImageResource(R.drawable.dice1);
                break;
            case 2:
                diceImage.setImageResource(R.drawable.dice2);
                break;
            case 3:
                diceImage.setImageResource(R.drawable.dice3);
                break;
            case 4:
                diceImage.setImageResource(R.drawable.dice4);
                break;
            case 5:
                diceImage.setImageResource(R.drawable.dice5);
                break;
            case 6:
                diceImage.setImageResource(R.drawable.dice6);
                break;
        }
    }

    public boolean rollDice(boolean isPlayerTurn){
        int faceNumber = random.nextInt(6) + 1;
        setCorrectImage(faceNumber);
        if (faceNumber == 1){
            setScores(overallUserScore, overallCpuScore, 0, true);
            return false;
        }
        else{
            setScores(overallUserScore, overallCpuScore, turnScore + faceNumber, isPlayerTurn);
            return true;
        }
    }

    public void holdDice(boolean isPlayerTurn){

        if(isPlayerTurn){
            allowPlayerInput(false);
            allowResetting(false);
            overallUserScore += turnScore;
            if (overallUserScore >= WINNING_SCORE){
                Win(true);

            }
            else{
                setScores(overallUserScore, overallCpuScore, 0, !isPlayerTurn);
                cpuTurn();
            }
        }
        else{
            overallCpuScore += turnScore;
            if (overallCpuScore >= WINNING_SCORE){
                Win(false);
            }
            else{
                setScores(overallUserScore, overallCpuScore, 0, !isPlayerTurn);
            }
            allowPlayerInput(true);
        }


        turnScore = 0;

    }

    public void Win(boolean playerWon){
        TextView scoresView = (TextView) findViewById(R.id.Scores);
        if (playerWon){
            scoresView.setText("You Win!");
        }
        else{
            scoresView.setText("You Lose!");
        }
        allowResetting(true);

    }

    public void allowPlayerInput(boolean allow){
        playerTurnActive = allow;
    }

    public void allowResetting(boolean allow) { resetActive = allow;}




}

