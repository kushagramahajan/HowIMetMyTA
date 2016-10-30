package com.example.kushagra.meetupapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FormBaseFragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View fragLayout = inflater.inflate(R.layout.fragment_form_base , container, false);

        return fragLayout;
    }




}
