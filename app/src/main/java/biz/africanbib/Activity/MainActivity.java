package biz.africanbib.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;

import biz.africanbib.Models.DropDown;
import biz.africanbib.Models.Heading;
import biz.africanbib.Models.SimpleEditText;
import biz.africanbib.R;
import biz.africanbib.Tabs.Tab1;
import biz.africanbib.Tabs.Tab2;
import biz.africanbib.Tabs.Tab3;
import biz.africanbib.Tabs.Tab4;
import biz.africanbib.Tabs.ViewPagerAdapter;
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
    public static boolean first = true;
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


    private void showValidate(boolean show) {
        if (show) {
            buttonValidate.setVisibility(View.VISIBLE);
        } else {
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

    private void checkValidation() {
        Tab1 tab1 = (Tab1) adapter.getItem(0);
        Tab2 tab2 = (Tab2) adapter.getItem(1);
        Tab3 tab3 = (Tab3) adapter.getItem(2);
        Tab4 tab4 = (Tab4) adapter.getItem(3);
        if (tab4.getAccepted()) {

            ArrayList<Object> items = tab1.getList();
            for (Object o :
                    items) {
                if (o instanceof SimpleEditText) {
                    SimpleEditText simpleEditText = (SimpleEditText) o;


                    //Business Name
                    if (simpleEditText.getTitle().equals(tab1.businessName)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Business Name in Tab1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    //Telephone
                    if (simpleEditText.getTitle().equals(tab1.telephone)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Telephone in Tab 1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


                    //City
                    if (simpleEditText.getTitle().equals(tab1.city_town)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid City / Town in Tab 1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


                    //State
                    if (simpleEditText.getTitle().equals(tab1.state)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid State in Tab 1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


                    //Country
                    if (simpleEditText.getTitle().equals(tab1.country)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Country in Tab 1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }

            //items.clear();
            ArrayList<Object> items1 = tab4.getList();
            for (Object o :
                    items1) {
                if (o instanceof SimpleEditText) {
                    SimpleEditText simpleEditText = (SimpleEditText) o;


                    //Business Name
                    if (simpleEditText.getTitle().equals(tab4.nameOfCollector)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Collector Name in Tab4", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    //Telephone
                    if (simpleEditText.getTitle().equals(tab4.authorizedBy)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Authorizing Name in Tab 4", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


                    //City
                    if (simpleEditText.getTitle().equals(tab4.placeOfCollection)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Place of Collection in Tab 4", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }


            tab1.getAdapter().notifyDataSetChanged();
            tab2.getAdapter().notifyDataSetChanged();
            tab3.getAdapter().notifyDataSetChanged();
            tab4.getAdapter().notifyDataSetChanged();

            Test(items, items1);
            showXML();
        } else {

            Toast.makeText(this, "Please accept the agreeement first", Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout = setUpTablayout(tabLayout);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPagerLayout);

        //Creating our pager blogAdapter
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        Tab1 tab1 = (Tab1) adapter.getItem(0);
        //Adding blogAdapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(this);
        viewPager.setOffscreenPageLimit(5);
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
        first = true;
        finish();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    void Test(ArrayList<Object> items, ArrayList<Object> items1) {

  /*      Tab1 tab1 = (Tab1) adapter.getItem(0);
        Tab2 tab2 = (Tab2) adapter.getItem(1);
        Tab3 tab3 = (Tab3) adapter.getItem(2);
        Tab4 tab4 = (Tab4) adapter.getItem(3);
        ArrayList<Object> items1 = tab1.getList();
        ArrayList<Object> items2 = tab2.getList();
        ArrayList<Object> items3 = tab3.getList();
        ArrayList<Object> items4 = tab4.getList();
*/
        try {
            Log.v(TAG,"Trying to create xml File");
            File file = new File(getApplicationContext().getDataDir(), companyName+".xml");
            file.createNewFile();
            Log.v(TAG,"Path = " + file.getAbsolutePath());
            FileOutputStream fileos = new FileOutputStream(file);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "Organisation");

            Log.d(TAG,"Item size = " + items.size()+"");
            for (int i = 0; i < items.size() - 1; i++) {
                int j=0;
                if (items.get(i) instanceof Heading) {
                    Log.v(TAG,"Start Tag = " + ((Heading) items.get(i)).getXmlTag());
                    //xmlSerializer.startTag(null, ((Heading) items.get(i)).getHeading());
                    String startTag = ((Heading) items.get(i)).getXmlTag();
                    if(startTag !=null)
                    {xmlSerializer.startTag(null,startTag);
                    }

                    j = i + 1;
                    Log.v(TAG,"J inital = " + j);
                    while (!(items.get(j) instanceof Heading)) {
                        Log.v(TAG,"J start = " + j);
                        if (items.get(j) instanceof SimpleEditText) {
                            Log.v(TAG,"Starting Tag = " + ((SimpleEditText) items.get(j)).getXmlTag());
                            xmlSerializer.startTag(null, ((SimpleEditText) items.get(j)).getXmlTag());

                            if (((SimpleEditText) items.get(j)).getValue() != null) {
                                xmlSerializer.text(((SimpleEditText) items.get(j)).getValue());
                            }
                            else {
                                xmlSerializer.text("null");
                            }
                            Log.v(TAG,"Ending Tag = " + ((SimpleEditText) items.get(j)).getXmlTag());
                            xmlSerializer.endTag(null, ((SimpleEditText) items.get(j)).getXmlTag());
                        }
                        if (items.get(j) instanceof DropDown) {
                            Log.v(TAG,"Starting Tag = " + ((DropDown) items.get(j)).getXmlTag());
                            xmlSerializer.startTag(null, ((DropDown) items.get(j)).getXmlTag());
                            Log.v(TAG,"Value of Tag = " + ((DropDown) items.get(j)).getSelectedPosition());
                            xmlSerializer.text(String.valueOf(((DropDown) items.get(j)).getSelectedPosition()));
                            Log.v(TAG,"Ending Tag = " +((DropDown) items.get(j)).getXmlTag());
                            xmlSerializer.endTag(null, ((DropDown) items.get(j)).getXmlTag());
                        }
                        Log.v(TAG,"J after = " + j);
                        if (j < items.size() - 1) {
                            Log.v(TAG, "J final = " + j);
                            j++;
                        }
                        else
                        {
                            break;
                        }
                    }
                    if(startTag!=null) {
                        xmlSerializer.endTag(null, startTag);
                    }
                }
                i = j - 1;
            }

            /*
            //For single
            xmlSerializer.startTag(null, "name");
            xmlSerializer.text(databaseHelper.getStringValue(DatabaseHelper.COLUMN_COMPANY_NAME, DatabaseHelper.TABLE_COMPANY_PROFILE));
            xmlSerializer.endTag(null, "name");

            xmlSerializer.startTag(null, "registrationNumber");
            xmlSerializer.text(databaseHelper.getStringValue(DatabaseHelper.COLUMN_REGISTERATION_NO, DatabaseHelper.TABLE_COMPANY_PROFILE));
            xmlSerializer.endTag(null, "registrationNumber");

            xmlSerializer.startTag(null, "description");
            xmlSerializer.text(databaseHelper.getStringValue(DatabaseHelper.COLUMN_DESCRIPTION, DatabaseHelper.TABLE_COMPANY_PROFILE));
            xmlSerializer.endTag(null, "description");

            xmlSerializer.startTag(null, "foundingYear");
            xmlSerializer.text(databaseHelper.getStringValue(DatabaseHelper.COLUMN_FOUNDING_YEAR_OF_COMPANY, DatabaseHelper.TABLE_COMPANY_INDICATORS));
            xmlSerializer.endTag(null, "foundingYear");

            xmlSerializer.startTag(null, "ageOfActBusiness");
            xmlSerializer.text(databaseHelper.getStringValue(DatabaseHelper.COLUMN_AGE_OF_ACTIVE_BUSINESS, DatabaseHelper.TABLE_COMPANY_INDICATORS));
            xmlSerializer.endTag(null, "ageOfActBusiness");

            xmlSerializer.startTag(null, "country");
            xmlSerializer.text(String.valueOf(databaseHelper.getIntValue(DatabaseHelper.COLUMN_COUNTRY, DatabaseHelper.TABLE_SOURCE_OF_DATA)));
            xmlSerializer.endTag(null, "country");

            xmlSerializer.startTag(null, "collectedBy");
            xmlSerializer.text(databaseHelper.getStringValue(DatabaseHelper.COLUMN_NAME_OF_COLLECTOR, DatabaseHelper.TABLE_SOURCE_OF_DATA));
            xmlSerializer.endTag(null, "collectedBy");


            //For group
            xmlSerializer.startTag(null, "contact");

            xmlSerializer.startTag(null, "cellphone");
            xmlSerializer.text(databaseHelper.getStringValue(DatabaseHelper.COLUMN_CELLPHONE, DatabaseHelper.TABLE_COMPANY_CONTACT));
            xmlSerializer.endTag(null, "cellphone");

            xmlSerializer.startTag(null, "telephone");
            xmlSerializer.text(databaseHelper.getStringValue(DatabaseHelper.COLUMN_TELEPHONE, DatabaseHelper.TABLE_COMPANY_CONTACT));
            xmlSerializer.endTag(null, "telephone");

            xmlSerializer.endTag(null, "contact");



            */
            xmlSerializer.endTag(null, "Organisation");
            xmlSerializer.endDocument();
            xmlSerializer.flush();

            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
            Toast.makeText(this,"Succesfully generated xml",Toast.LENGTH_SHORT).show();
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }

    }

    public void showXML() {
        File file = new File(getApplicationContext().getDataDir(), companyName+".xml");
        try {
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            Log.v(TAG, aBuffer);
            myReader.close();
            fIn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
