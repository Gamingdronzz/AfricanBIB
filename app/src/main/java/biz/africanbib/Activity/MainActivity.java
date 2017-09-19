package biz.africanbib.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;

import java.util.ArrayList;

import biz.africanbib.Models.SimpleEditText;
import biz.africanbib.R;
import biz.africanbib.Tabs.Tab2;
import biz.africanbib.Tabs.Tab4;
import biz.africanbib.Tabs.ViewPagerAdapter;
import biz.africanbib.Tabs.Tab1;
import biz.africanbib.Tools.DatabaseHelper;
import biz.africanbib.Tools.Helper;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String TAG = "Main";
    public static int typeOfBusiness;
    public static final int NEWBUSINESS = 1;
    public static final int EDITBUSINESS = 2;
    //This is our tablayout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    SegmentedProgressBar segmentedProgressBar;
    FloatingActionButton goLeft, goRight;
    DatabaseHelper databaseHelper;
    String companyName = null;
    ViewPagerAdapter adapter;
    Helper helper;

    private Button buttonValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(getApplicationContext(), DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        bindViews();
        setUpTabLayout();
        init();
        handleIntent();
        Log.v(TAG, "Current company id = " + DatabaseHelper.getCurrentCompanyId());
    }

    private void handleIntent() {
        Intent intent = getIntent();
        typeOfBusiness = intent.getIntExtra("type", -1);

    }

    private void init() {
        helper = new Helper(this);
        goLeft.setVisibility(View.INVISIBLE);

        goLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = tabLayout.getSelectedTabPosition();
                tabLayout.getTabAt(pos - 1).select();
            }
        });
        goRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = tabLayout.getSelectedTabPosition();
                tabLayout.getTabAt(pos + 1).select();
            }
        });

        if (DatabaseHelper.getCurrentCompanyId() != -1) {
            companyName = (String) databaseHelper.getStringValue(DatabaseHelper.COLUMN_COMPANY_NAME, DatabaseHelper.TABLE_COMPANY_PROFILE);
            getSupportActionBar().setTitle(companyName);
        }
        showValidate(false);
    }


    private void showValidate(boolean show)
    {
        if(show)
        {
            buttonValidate.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonValidate.setVisibility(View.INVISIBLE);
        }
    }


    private void bindViews() {
        segmentedProgressBar = (SegmentedProgressBar) findViewById(R.id.segmented_progressbar);
        goLeft = (FloatingActionButton) findViewById(R.id.fab_go_left);
        goRight = (FloatingActionButton) findViewById(R.id.fab_go_right);
        buttonValidate = (Button) findViewById(R.id.validate);
        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
    }

    private void checkValidation()
    {
        Tab4 tab4 = (Tab4)adapter.getItem(3);
        if(tab4.getAccepted())
        {
            Tab1 tab1 = (Tab1)adapter.getItem(0);
            ArrayList<Object> items = tab1.getList();
            for (Object o :
                    items) {
                if (o instanceof SimpleEditText)
                {
                    SimpleEditText simpleEditText = (SimpleEditText) o;


                    //Business Name
                    if(simpleEditText.getTitle().equals(tab1.businessName))
                    {
                        if(helper.checkForInput(simpleEditText.getValue()) ==null)
                        {
                            Toast.makeText(this,"Please enter a valid Business Name in Tab1",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    //Telephone
                    if(simpleEditText.getTitle().equals(tab1.telephone))
                    {
                        if(helper.checkForInput(simpleEditText.getValue()) ==null)
                        {
                            Toast.makeText(this,"Please enter a valid Telephone in Tab 1",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


                    //City
                    if(simpleEditText.getTitle().equals(tab1.city_town))
                    {
                        if(helper.checkForInput(simpleEditText.getValue()) ==null)
                        {
                            Toast.makeText(this,"Please enter a valid City / Town in Tab 1",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


                    //State
                    if(simpleEditText.getTitle().equals(tab1.state))
                    {
                        if(helper.checkForInput(simpleEditText.getValue()) ==null)
                        {
                            Toast.makeText(this,"Please enter a valid State in Tab 1",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }



                    //Country
                    if(simpleEditText.getTitle().equals(tab1.country))
                    {
                        if(helper.checkForInput(simpleEditText.getValue()) ==null)
                        {
                            Toast.makeText(this,"Please enter a valid Country in Tab 1",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }

            items.clear();
            items = tab4.getList();
            for (Object o :
                    items) {
                if (o instanceof SimpleEditText)
                {
                    SimpleEditText simpleEditText = (SimpleEditText) o;


                    //Business Name
                    if(simpleEditText.getTitle().equals(tab4.nameOfCollector))
                    {
                        if(helper.checkForInput(simpleEditText.getValue()) ==null)
                        {
                            Toast.makeText(this,"Please enter a valid Collector Name in Tab4",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    //Telephone
                    if(simpleEditText.getTitle().equals(tab4.authorizedBy))
                    {
                        if(helper.checkForInput(simpleEditText.getValue()) ==null)
                        {
                            Toast.makeText(this,"Please enter a valid Authorizing Name in Tab 4",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


                    //City
                    if(simpleEditText.getTitle().equals(tab4.placeOfCollection))
                    {
                        if(helper.checkForInput(simpleEditText.getValue()) ==null)
                        {
                            Toast.makeText(this,"Please enter a valid Place of Collection in Tab 4",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }


            tab4.getAdapter().notifyDataSetChanged();

        }
        else
        {

            Toast.makeText(this,"Please accept the agreeement first",Toast.LENGTH_SHORT).show();
        }



    }

    private void setUpTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout = setUpTablayout(tabLayout);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPagerLayout);

        //Creating our pager blogAdapter
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        Tab1 tab1 = (Tab1)adapter.getItem(0);
        //Adding blogAdapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(this);
        viewPager.setOffscreenPageLimit(4);
        viewPager.getAdapter().notifyDataSetChanged();

    }

    private TabLayout setUpTablayout(TabLayout tabLayout) {
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //tabLayout.setSmoothScrollingEnabled(true);

        //tabLayout.getTabAt(0).setIcon(R.drawable.group);

        tabLayout.getTabAt(0).setText("1");
        tabLayout.getTabAt(1).setText("2");
        tabLayout.getTabAt(2).setText("3");
        tabLayout.getTabAt(3).setText("4");


        return tabLayout;
    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d(TAG, "Selected position = " + tab.getPosition());
        int position = tab.getPosition();
        if (position == 0) {
            manageFab(false, true);
        } else if (position == 3) {
            manageFab(true, false);
        } else {
            manageFab(true, true);
        }
        viewPager.setCurrentItem(position);
        segmentedProgressBar.setCompletedSegments(position);

    }

    private void manageFab(boolean showLeft, boolean showRight) {
        if (showLeft) {
            goLeft.setVisibility(View.VISIBLE);
            showValidate(false);
        } else {
            goLeft.setVisibility(View.INVISIBLE);
            showValidate(false);
        }
        if (showRight) {
            goRight.setVisibility(View.VISIBLE);
            showValidate(false);
        } else {
            goRight.setVisibility(View.INVISIBLE);
            showValidate(true);
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
        finish();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
