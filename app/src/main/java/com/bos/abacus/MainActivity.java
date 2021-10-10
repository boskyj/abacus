package com.bos.abacus;

import static com.bos.abacus.Constant.ANSWER_DELAY;
import static com.bos.abacus.Constant.END;
import static com.bos.abacus.Constant.QUESTION_COUNT;
import static com.bos.abacus.Constant.QUESTION_DELAY;
import static com.bos.abacus.Constant.ROW;
import static com.bos.abacus.Constant.START;
import static com.bos.abacus.Constant.PROBLEM_TYPE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText start;
    private EditText end;
    private EditText row;
    private EditText questionCount;
    private EditText questionDelay;
    private EditText answerDelay;
    private RadioGroup radioOpGroup;
    private RadioButton radioOpr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {
        Intent intent = new Intent(this, DisplayActivity.class);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        row = findViewById(R.id.row);
        questionCount = findViewById(R.id.questionCount);
        questionDelay = findViewById(R.id.questionDelay);
        answerDelay = findViewById(R.id.answerDelay);
        radioOpGroup = findViewById(R.id.operationGroup);
        if (!validate()) {
            int selectedId = radioOpGroup.getCheckedRadioButtonId();
            ProblemType problemType;
            switch (selectedId) {
                case R.id.lessId:
                    problemType = ProblemType.SUMWITHSUBSTRACT;
                    break;
                case R.id.mulId:
                    problemType = ProblemType.MULTIPLY;
                    break;
                default:
                    problemType = ProblemType.SUM;
            }


            intent.putExtra(START, start.getText().toString());
            intent.putExtra(END, end.getText().toString());
            intent.putExtra(ROW, row.getText().toString());
            intent.putExtra(QUESTION_COUNT, questionCount.getText().toString());
            intent.putExtra(QUESTION_DELAY, questionDelay.getText().toString());
            intent.putExtra(ANSWER_DELAY, answerDelay.getText().toString());
            intent.putExtra(PROBLEM_TYPE, problemType.name());
            //EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
            //String message = editText.getText().toString();
            //intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }

    private boolean validate() {
        boolean error = false;
        if (validate(start) | validate(end) | validate(row) | validate(questionCount) | validate(questionDelay) | validate(answerDelay) || validateEnd()) {
            return true;
        }
        return false;
    }

    private boolean validate(EditText val) {
        if (val.getText().toString().isEmpty()) {
            val.setError("Value is required!");
            return true;
        } else {
            int editval = Integer.parseInt(val.getText().toString());
            if (editval == 0) {
                val.setError("Value Should be grater than zero!");
                return true;
            }
            return false;
        }

    }

    private boolean validateEnd() {
        int startVal = Integer.parseInt(start.getText().toString());
        int endVal = Integer.parseInt(end.getText().toString());
        if (endVal <= startVal) {
            end.setError("End should be greater than Start");
            return true;
        }
        return false;
    }
}