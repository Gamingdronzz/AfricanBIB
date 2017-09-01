package biz.africanbib.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import biz.africanbib.R;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class ViewHolderHeading extends RecyclerView.ViewHolder {
    private TextView textview;

    public ViewHolderHeading(View v) {
        super(v);
        textview = (TextView) v.findViewById(R.id.text_view_heading);
    }

    public void setHeading(String value)
    {
        this.textview.setText(value);
    }

    public String getHeading()
    {
        return this.textview.getText().toString();
    }




}