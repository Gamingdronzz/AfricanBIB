package biz.africanbib.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.logging.Logger;

import biz.africanbib.R;
import biz.africanbib.Tools.DatabaseHelper;

public class SplashScreen extends AppCompatActivity {

    Button addBusiness, editBusiness;
    LinearLayout linearLayout;
    ImageView imageView;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        databaseHelper = new DatabaseHelper(getApplicationContext(), DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        bindVIews();
        //ShowNextActivity();
        StartAnimations();

    }

    private void bindVIews() {
        addBusiness = (Button) findViewById(R.id.button_add_business);
        addBusiness.setVisibility(View.INVISIBLE);
        editBusiness = (Button) findViewById(R.id.button_edit_business);
        editBusiness.setVisibility(View.INVISIBLE);
        imageView = (ImageView) findViewById(R.id.logo);
        linearLayout = (LinearLayout) findViewById(R.id.poweredBy);
        linearLayout.setVisibility(View.INVISIBLE);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCompanyWebsite();
            }
        });
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

    private void openCompanyWebsite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse("http://www.gamingdronzz.com"));
        startActivity(browserIntent);
    }

    private void StartAnimations() {
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        final Animation animationBounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        final Animation add = AnimationUtils.loadAnimation(this, R.anim.add);
        final Animation edit = AnimationUtils.loadAnimation(this, R.anim.edit);

        anim.reset();
        animationBounce.reset();
        add.reset();
        edit.reset();

        imageView.clearAnimation();
        imageView.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout.clearAnimation();
                linearLayout.startAnimation(animationBounce);

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationBounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                addBusiness.clearAnimation();
                editBusiness.clearAnimation();
                addBusiness.setVisibility(View.VISIBLE);
                addBusiness.startAnimation(add);
                editBusiness.setVisibility(View.VISIBLE);
                editBusiness.startAnimation(edit);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void LoadNextActivity(boolean newBusiness) {

        if (newBusiness) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), InitialCompanyDetails.class);
            intent.putExtra("type", MainActivity.NEWBUSINESS);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), PreviousBusinessLists.class);
            intent.putExtra("type", MainActivity.EDITBUSINESS);
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
                finish();
            }
        });

        alertDialog.setNegativeButton("No", null);

        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setTitle("African BIB");
        alertDialog.show();
    }
}
