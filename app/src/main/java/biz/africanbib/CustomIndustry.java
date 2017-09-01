package biz.africanbib;


import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * Created by Balpreet on 05-Aug-17.
 */

public class CustomIndustry extends DialogFragment {

    private AppCompatSpinner spinner;
    String[] items;
    Bundle args;
    ArrayAdapter<String> arrayAdapter;




    public interface UserNameListener {
        void onFinishUserDialog(int position);
    }

    // Empty constructor required for DialogFragment
    public CustomIndustry() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_dialog_industry, container);
        spinner = (AppCompatSpinner) view.findViewById(R.id.custom_industry);

        //Log.v("Custom",getArguments().toString());
        args = getArguments();
        this.items = new String[args.getInt("size",0)];
        items = args.getStringArray("items");
        arrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_row);
        arrayAdapter.clear();
        arrayAdapter.addAll(items);
        // set this instance as callback for editor action
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new CustomItemSelectedListener());
        getDialog().setTitle("Please choose an Industry");
        return view;
    }

    private class CustomItemSelectedListener implements AppCompatSpinner.OnItemSelectedListener,View.OnTouchListener{
        //    private int position;
        boolean userSelect = false;

/*    public void updatePosition(int position) {
        this.position = position;
    }
    */


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (userSelect) {
                CustomIndustry.UserNameListener activity = (CustomIndustry.UserNameListener) CustomIndustry.this.getActivity();
                activity.onFinishUserDialog(i);
                CustomIndustry.this.dismiss();
                Log.v("Custom", "Finished with = " + i);
                userSelect = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            userSelect = true;
            return false;
        }
    }
}


