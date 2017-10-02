package biz.africanbib;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Balpreet on 27-Sep-17.
 */

public class GenerateXMLDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // set dialog icon
                .setIcon(android.R.drawable.alert_dark_frame)
                // set Dialog Title
                .setTitle("Generating XML")
                // Set Dialog Message
                .setMessage("Please Wait..")

                // positive button
               .create();

    }
}
