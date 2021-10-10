package com.bos.abacus;

import static com.bos.abacus.Constant.ANSWER_DELAY;
import static com.bos.abacus.Constant.END;
import static com.bos.abacus.Constant.PROBLEM_TYPE;
import static com.bos.abacus.Constant.QUESTION_COUNT;
import static com.bos.abacus.Constant.QUESTION_DELAY;
import static com.bos.abacus.Constant.ROW;
import static com.bos.abacus.Constant.START;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity extends AppCompatActivity implements AbacusHandler {

    private Abacus abacus;
    private TextView abView;
    private Thread calculationThread;
    private DisplayTask displayTask;
    private boolean ansDisplay;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        abView = findViewById(R.id.abView);
        abacus = Abacus.getInstance();
        abacus.setHandler(this);
        displayTask = new DisplayTask();
        displayTask.setTextView(abView);
        abView.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onResume() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onResume();
        startAbacus();
    }


    private void startAbacus() {
        ansDisplay = false;
        Intent intent = getIntent();
        int start = Integer.parseInt(intent.getStringExtra(START));
        int end = Integer.parseInt(intent.getStringExtra(END));
        int row = Integer.parseInt(intent.getStringExtra(ROW));
        int questionCount = Integer.parseInt(intent.getStringExtra(QUESTION_COUNT));
        int questionDelay = Integer.parseInt(intent.getStringExtra(QUESTION_DELAY));
        int answerDelay = Integer.parseInt(intent.getStringExtra(ANSWER_DELAY));
        ProblemType type=ProblemType.valueOf(intent.getStringExtra(PROBLEM_TYPE));
        abacus.setStop(false);
        Runnable task = () -> abacus.start(start, end, row, questionCount, questionDelay, answerDelay, type);
        calculationThread = new Thread(task);
        calculationThread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        abacus.setStop(true);
        calculationThread.interrupt();
    }

    @Override
    public void preStart() {
        Log.d("Display", "Calculation Started");
    }

    @Override
    public void setAnswer(long answer) {
        ansDisplay = true;
        Log.d("Display Answer", answer + "");
        displayTask.setVal(abView.getText() + System.getProperty("line.separator") + System.getProperty("line.separator") + "Answer " + answer);
        runOnUiThread(displayTask);
    }

    @Override
    public void setQuestion(long question) {
        Log.d("Display Question", question + "");
        if (ansDisplay) {
            displayTask.setVal(question + "");
            ansDisplay = false;
        } else {
            displayTask.setVal(abView.getText() + System.getProperty("line.separator") + question);
        }
        runOnUiThread(displayTask);
     }

    @Override
    public void onCalculationEnd() {
        Log.d("Display", "Calculation Ended");
        runOnUiThread(()->getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON));
    }
}