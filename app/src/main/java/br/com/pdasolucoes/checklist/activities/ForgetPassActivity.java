package br.com.pdasolucoes.checklist.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import br.com.pdasolucoes.checklist.adapter.AppointmentAdapter;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 08/11/2016.
 */

public class ForgetPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        setContentView(R.layout.forget_password_activity);
    }
}
