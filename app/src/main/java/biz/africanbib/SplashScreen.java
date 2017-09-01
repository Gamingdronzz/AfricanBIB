package biz.africanbib;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {

    Button addBusiness,editBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        bindVIews();
        //ShowNextActivity();
    }

    private void bindVIews()
    {
        addBusiness = (Button) findViewById(R.id.button_add_business);
        editBusiness = (Button) findViewById(R.id.button_edit_business);

        addBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadNextActivity(true);
            }
        });

        editBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadNextActivity(false);
            }
        });
    }
/*
    private void ShowNextActivity() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadNextActivity();
                    }
                },
                1000
        );
    }
*/
    private void LoadNextActivity(boolean newBusiness) {

        if(newBusiness ) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), InitialCompanyDetails.class);
            intent.putExtra("type",MainActivity.NEWBUSINESS);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),PreviousBusinessLists.class);
            intent.putExtra("type",MainActivity.EDITBUSINESS);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doExit();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doExit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SplashScreen.this, R.style.MyAlertDialogStyle);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.setNegativeButton("No", null);

        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setTitle("African BIB");
        alertDialog.show();
    }
}
