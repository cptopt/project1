package cogentdatasolutions.project1.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cogentdatasolutions.project1.R;

/**
 * Created by madhu on 12-Sep-16.
 */
public class EducationalFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.educationdetails,container,false);
        return view;
    }

}
