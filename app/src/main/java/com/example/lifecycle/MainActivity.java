package com.example.lifecycle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


public class MainActivity extends AppCompatActivity {
    private ConstraintLayout mainScreen;
    private TextView txtSpy;

    private final String chosenMemberKey = "chosen-member";

    private void setBackgroundColorBasedOnMember(String member, ConstraintLayout screen) {
        if (member.contains("hieu")) {
            screen.setBackgroundColor(getResources().getColor(R.color.purple));
        } else if (member.contains("thu")) {
            screen.setBackgroundColor(getResources().getColor(R.color.blue));
        } else if (member.contains("trung")) {
            screen.setBackgroundColor(getResources().getColor(R.color.green));
        } else if (member.contains("nhi")) {
            screen.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            screen.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataStoreManager.instance.init(this);

        mainScreen = findViewById(R.id.mainScreen);
        Button btnExit = findViewById(R.id.btnExit);
        TextInputEditText txtMsg = findViewById(R.id.txtMsg);
        txtSpy = findViewById(R.id.txtSpy);

        btnExit.setOnClickListener(view -> finish());

        txtMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String chosenMember = editable.toString().toLowerCase();
                setBackgroundColorBasedOnMember(chosenMember, mainScreen);
                txtSpy.setText(chosenMember);
            }
        });

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        DataStoreManager.instance.getStringValue(chosenMemberKey, s -> {
            setBackgroundColorBasedOnMember(s, mainScreen);
        });
    }

    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    protected void onPause() {
        super.onPause();
        DataStoreManager.instance.saveValue(chosenMemberKey, txtSpy.getText().toString());
        Log.d("-->", "onPause");
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
    }

    protected void onStop() {
        super.onStop();
        Log.d("-->", "onStop");
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("-->", "onDestroy");
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }
}