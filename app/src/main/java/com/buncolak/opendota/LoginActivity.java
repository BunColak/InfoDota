package com.buncolak.opendota;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_steamid)
    EditText et_steamid;
    @BindView(R.id.button_login)
    Button button_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_login)
    public void login() {
        String userId = et_steamid.getText().toString();
        setResult(RESULT_OK, new Intent().putExtra(Intent.EXTRA_TEXT, userId));
        finish();

    }
}