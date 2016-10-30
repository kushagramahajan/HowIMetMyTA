package com.example.kushagra.meetupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {


    private static final String FORM_WINDOW_TYPE_EXTRA = "com.example.kushagra.meetupapp.Form Window Type";

    public enum FormWindowType {BASE , CHOOSE  , DATETIME }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        loadAppropriateFragment();

    }

    private void loadAppropriateFragment()
    {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Intent intent = getIntent();
        FormWindowType formWindowType = (FormWindowType)
                intent.getSerializableExtra( FORM_WINDOW_TYPE_EXTRA  );

        if(formWindowType == FormWindowType.BASE || formWindowType == null)
        {

            Fragment fragment = new FormBaseFragment();

            fragmentTransaction.add(R.id.activity_form , fragment ,null );
            fragmentTransaction.commit();


        }
        else if(formWindowType == FormWindowType.DATETIME )
        {
            

        }



    }
}
