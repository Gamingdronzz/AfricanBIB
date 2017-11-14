package biz.africanbib.Activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;

import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.List;

import biz.africanbib.Models.Add;
import biz.africanbib.Models.Divider;
import biz.africanbib.Models.DropDown;
import biz.africanbib.Models.Heading;
import biz.africanbib.Models.MultiSelectDropdown;
import biz.africanbib.Models.SimpleDate;
import biz.africanbib.Models.SimpleEditText;
import biz.africanbib.Models.SimpleImage;
import biz.africanbib.Models.SimpleText;
import biz.africanbib.R;
import biz.africanbib.Tabs.Tab1;
import biz.africanbib.Tabs.Tab2;
import biz.africanbib.Tabs.Tab3;
import biz.africanbib.Tabs.Tab4;
import biz.africanbib.Tabs.ViewPagerAdapter;
import biz.africanbib.Tools.DatabaseHelper;
import biz.africanbib.Tools.Helper;
import biz.africanbib.Tools.VolleyHelper;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, VolleyHelper.VolleyResponse {

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


    AwesomeInfoDialog awesomeInfoDialog;

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
        awesomeInfoDialog = new AwesomeInfoDialog(this)
                .setTitle("Generating XML")
                .setMessage("Please wait..")
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.colorPrimary)
                .setCancelable(false);


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
            companyName = databaseHelper.getStringValue(DatabaseHelper.COLUMN_COMPANY_NAME, DatabaseHelper.TABLE_COMPANY_PROFILE);
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

        ArrayList<Object> items2 = tab2.getList();
        ArrayList<Object> items3 = tab3.getList();
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

                    if (simpleEditText.getTitle().equals(tab1.registerationNumber)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Registeration Number in Tab1", Toast.LENGTH_SHORT).show();
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
                } else if (o instanceof SimpleImage) {
                    SimpleImage simpleImage = (SimpleImage) o;
                    if (simpleImage.getTitle().equals(tab1.corporateLogo)) {
                        if (simpleImage.getImage() == null) {
                            Toast.makeText(this, "Please select Corporate Logo in Tab1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if (simpleImage.getTitle().equals(tab1.keyVisual)) {
                        if (simpleImage.getImage() == null) {
                            Toast.makeText(this, "Please select KeyVisual Photo in Tab1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }

            //items.clear();
            ArrayList<Object> items4 = tab4.getList();
            for (Object o :
                    items4) {
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

            awesomeInfoDialog.show();
            Test(items, items2, items3, items4);

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
        //Adding blogAdapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setCurrentItem(0);
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


    void Test(ArrayList<Object> items1, ArrayList<Object> items2, ArrayList<Object> items3, ArrayList<Object> items4) {

        try {
            Log.v(TAG, "Trying to create xml File");
            File file = new File(getApplicationContext().getFilesDir(), companyName + ".xml");
            file.createNewFile();
            Log.v(TAG, "Path = " + file.getAbsolutePath());
            FileOutputStream fileos = new FileOutputStream(file);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "Organisation");

            addXmlContent(items1, xmlSerializer);
            addXmlContent(items2, xmlSerializer);
            addXmlContent(items3, xmlSerializer);
            addXmlContent(items4, xmlSerializer);

            xmlSerializer.endTag(null, "Organisation");
            xmlSerializer.endDocument();
            xmlSerializer.flush();

            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
            Toast.makeText(this, "Succesfully generated xml", Toast.LENGTH_SHORT).show();
            awesomeInfoDialog.setMessage("Succesfully Generated XML");
            showXML();
        } catch (IOException e) {
            awesomeInfoDialog.setMessage("Error in Generating XML");
            awesomeInfoDialog.setCancelable(true);
            e.printStackTrace();
        }

    }

    private void addXmlContent(ArrayList<Object> items, XmlSerializer xmlSerializer) throws IOException {
        String headTag = null;
        for (int i = 0; i < items.size() - 1; i++) {
            Object item = items.get(i);
            Log.v(TAG, "i =" + i);
            if (items.get(i) instanceof Heading) {
                Heading heading = (Heading) items.get(i);
                if (heading.getHeading().equals("OWNERS/MANAGERS/REFERENCES")) {
                    headTag = null;
                    i = getContactXml(i, items, xmlSerializer);
                } else {
                    headTag = heading.getXmlTag();
                    if (headTag != null) {
                        Log.v(TAG, "START TAG " + headTag);
                        xmlSerializer.startTag(null, headTag);
                    }
                }
            } else {
                while (!(item instanceof Heading)) {

                    if (item instanceof SimpleEditText) {
                        SimpleEditText simpleEditText = (SimpleEditText) item;
                        // Log.v(TAG, "Found " + simpleEditText.getTitle() + " with value = " + simpleEditText.getValue() + " where i = " + i);
                        if (simpleEditText.getValue() != null) {
                            if (!simpleEditText.getValue().isEmpty()) {
                                xmlSerializer.startTag(null, simpleEditText.getXmlTag());
                                xmlSerializer.text(Helper.forReplacementString(simpleEditText.getValue()));
                                xmlSerializer.endTag(null, simpleEditText.getXmlTag());
                            }
                        }
                    }

                    else if (item instanceof SimpleDate) {
                        SimpleDate date = (SimpleDate) item;
                        //Log.v(TAG, "Found " + date.getTitle() + " with value = " + date.getValue() + " where i = " + i);
                        if (date.getValue() != null) {
                            xmlSerializer.startTag(null, date.getXmlTag());
                            xmlSerializer.text(helper.toDays(date.getValue()));
                            xmlSerializer.endTag(null, date.getXmlTag());
                        }
                    }
                    else if (item instanceof SimpleImage) {
                        SimpleImage simpleImage = (SimpleImage) item;
                        //Log.v(TAG, "Found " + date.getTitle() + " with value = " + date.getValue() + " where i = " + i);
                        if (simpleImage.getImage() != null) {
                            xmlSerializer.startTag(null, simpleImage.getXmlTag());
                            String tagName =helper.checkForInput(simpleImage.getTitle());
                            xmlSerializer.text(tagName+".jpg");
                            xmlSerializer.endTag(null, simpleImage.getXmlTag());
                        }
                    }

                    else if (item instanceof DropDown) {
                        DropDown dropDown = (DropDown) item;
                        String collectedBy;
                        xmlSerializer.startTag(null, ((DropDown) item).getXmlTag());
                        if (dropDown.getColumnName().equals(DatabaseHelper.COLUMN_PLACE_OF_COLECTION)) {
                            if (helper.getSelectedValue(dropDown, dropDown.getSelectedPosition()).equals("Others")) {
                                SimpleEditText edt = (SimpleEditText) items.get(++i);
                                SimpleDate date = (SimpleDate) items.get(++i);
                                if (edt.getValue().equals(" "))
                                    collectedBy = helper.toDays(date.getValue());
                                else
                                    collectedBy = Helper.forReplacementString(edt.getValue()) + " , " + helper.toDays(date.getValue());
                            } else {
                                if (items.get(i + 1) instanceof SimpleEditText) {
                                    i = i + 2;
                                } else i++;
                                SimpleDate date = (SimpleDate) items.get(i);
                                collectedBy = helper.getSelectedValue(dropDown, dropDown.getSelectedPosition()) + " , " + helper.toDays(date.getValue());
                            }
                            xmlSerializer.text(collectedBy);
                        } else
                            xmlSerializer.text(helper.getSelectedValue(dropDown, dropDown.getSelectedPosition()));
                        xmlSerializer.endTag(null, ((DropDown) item).getXmlTag());
                    }

                    else if (item instanceof MultiSelectDropdown) {
                        MultiSelectDropdown multiSelectDropdown = (MultiSelectDropdown) item;
                        //Log.v(TAG, "Found " + multiSelectDropdown.getTitle() + " with value = " + multiSelectDropdown.getSelectedIndices() + " where i = " + i);
                        List<Integer> indices = multiSelectDropdown.getSelectedIndices();
                        if (indices.size() > 0) {
                            for (int index : indices) {
                                xmlSerializer.startTag(null, multiSelectDropdown.getXmlTag());
                                helper.childTags(multiSelectDropdown, index, xmlSerializer);
                                xmlSerializer.endTag(null, multiSelectDropdown.getXmlTag());
                            }
                        }
                    }

                    else if (item instanceof SimpleText) {
                        SimpleText simpleText = (SimpleText) item;
                        //Log.v(TAG, "Found " + simpleText.getTitle() + " where i = " + i);
                        if (simpleText.getXmlTag() != null) {
                            if (simpleText.getXmlTag().equals("disclaimer"))
                                break;
                        }
                        String tag = ((SimpleText) item).getXmlTag();
                        if (tag != null) {
                            xmlSerializer.startTag(null, tag);
                            Log.v(TAG, "Simple Start Tag = " + tag);
                        }
                        i++;
                        item = items.get(i);
                        if (item instanceof Add) {

                        } else {       //Loop through the items until we find an object of class Add because Add denotes its the end of current group
                            while (!(item instanceof Add)) {

                                if (!(item instanceof Divider)) {
                                    if (item instanceof SimpleEditText) {
                                        SimpleEditText simpleEditText = (SimpleEditText) item;
                                        if (simpleEditText.getValue() != null) {
                                            if (!simpleEditText.getValue().isEmpty()) {
                                                xmlSerializer.startTag(null, simpleEditText.getXmlTag());
                                                xmlSerializer.text(Helper.forReplacementString(simpleEditText.getValue()));
                                                xmlSerializer.endTag(null, simpleEditText.getXmlTag());
                                            }
                                        }
                                    }

                                    if (item instanceof DropDown) {
                                        DropDown dropDown = (DropDown) item;
                                        xmlSerializer.startTag(null, ((DropDown) item).getXmlTag());
                                        xmlSerializer.text(helper.getSelectedValue(dropDown, dropDown.getSelectedPosition()));
                                        xmlSerializer.endTag(null, ((DropDown) item).getXmlTag());
                                    }

                                    if (item instanceof MultiSelectDropdown) {
                                        MultiSelectDropdown multiSelectDropdown = (MultiSelectDropdown) item;
                                        List<Integer> indices = multiSelectDropdown.getSelectedIndices();
                                        if (indices.size() > 0) {
                                            for (int index :
                                                    indices) {
                                                xmlSerializer.startTag(null, multiSelectDropdown.getXmlTag());
                                                helper.childTags(multiSelectDropdown, index, xmlSerializer);
                                                xmlSerializer.endTag(null, multiSelectDropdown.getXmlTag());
                                            }
                                        }
                                    }

                                    if (item instanceof SimpleDate) {
                                        SimpleDate date = (SimpleDate) item;
                                        if (date.getValue() != null) {
                                            xmlSerializer.startTag(null, date.getXmlTag());
                                            xmlSerializer.text(helper.toDays(date.getValue()));
                                            xmlSerializer.endTag(null, date.getXmlTag());
                                        }
                                    }
                                    if (item instanceof SimpleImage) {
                                        SimpleImage simpleImage = (SimpleImage) item;
                                        //Log.v(TAG, "Found " + date.getTitle() + " with value = " + date.getValue() + " where i = " + i);
                                        if (simpleImage.getImage() != null) {
                                            xmlSerializer.startTag(null, simpleImage.getXmlTag());
                                            String tagName =helper.checkForInput(simpleImage.getTitle());
                                            xmlSerializer.text(tagName+".jpg");
                                            xmlSerializer.endTag(null, simpleImage.getXmlTag());
                                        }
                                    }
                                }
                                i++;
                                item = items.get(i);
                            }
                        }
                        if (tag != null) {
                            Log.v(TAG, "Simple End Tag = " + tag);
                            xmlSerializer.endTag(null, tag);
                        }
                    }


                    if (i < items.size() - 1) {
                        i++;
                        item = items.get(i);
                    } else {
                        break;
                    }

                }
                i--;
                if (headTag != null) {
                    Log.v(TAG, "ENDTAG " + headTag);
                    xmlSerializer.endTag(null, headTag);
                }
            }

        }
    }

    private int getContactXml(int i, ArrayList<Object> items, XmlSerializer xmlSerializer) throws IOException {
        int j = i + 1;
        Object obj = items.get(j);
        while (!(obj instanceof Heading)) {
            if (obj instanceof SimpleText) {
                j++;
                while (!(items.get(j) instanceof Add)) {

                    xmlSerializer.startTag(null, "contact");
                    obj = items.get(j);
                    while (!(obj instanceof Divider)) {

                        if (obj instanceof SimpleEditText) {
                            SimpleEditText simpleEditText = (SimpleEditText) obj;
                            if (simpleEditText.getValue() != null) {
                                if (!simpleEditText.getValue().isEmpty()) {
                                    xmlSerializer.startTag(null, simpleEditText.getXmlTag());
                                    xmlSerializer.text(Helper.forReplacementString(simpleEditText.getValue()));
                                    xmlSerializer.endTag(null, simpleEditText.getXmlTag());
                                }
                            }
                        }
                        if (obj instanceof DropDown) {
                            DropDown dropDown = (DropDown) obj;
                            xmlSerializer.startTag(null, ((DropDown) obj).getXmlTag());
                            xmlSerializer.text(helper.getSelectedValue(dropDown, dropDown.getSelectedPosition()));
                            xmlSerializer.endTag(null, ((DropDown) obj).getXmlTag());
                        }
                        if (obj instanceof MultiSelectDropdown) {
                            MultiSelectDropdown multiSelectDropdown = (MultiSelectDropdown) obj;
                            Log.v(TAG, "Found " + multiSelectDropdown.getTitle() + " with value = " + multiSelectDropdown.getSelectedIndices() + " where i = " + i);
                            List<Integer> indices = multiSelectDropdown.getSelectedIndices();
                            if (indices.size() > 0) {
                                for (int index :
                                        indices) {
                                    xmlSerializer.startTag(null, multiSelectDropdown.getXmlTag());
                                    helper.childTags(multiSelectDropdown, index, xmlSerializer);
                                    xmlSerializer.endTag(null, multiSelectDropdown.getXmlTag());
                                }
                            }
                        }
                        if (obj instanceof SimpleDate) {
                            SimpleDate date = (SimpleDate) obj;
                            if (date.getValue() != null) {
                                xmlSerializer.startTag(null, date.getXmlTag());
                                xmlSerializer.text(helper.toDays(date.getValue()));
                                xmlSerializer.endTag(null, date.getXmlTag());
                            }
                        }
                        j++;
                        obj = items.get(j);
                    }
                    xmlSerializer.endTag(null, "contact");
                    j++;
                    obj = items.get(j);
                }
            }
            if (items.get(j) instanceof Add) {
                j++;
                obj = items.get(j);
            }
        }
        return j-1;
    }

    private void showXML() {
        File file = new File(getApplicationContext().getFilesDir(), companyName + ".xml");
        String aDataRow = "";
        String aBuffer = "";
        try {
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));


            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
                //Log.v("XML", "\n" + aDataRow);
            }
            Log.v(TAG, aBuffer);
            myReader.close();
            fIn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* VolleyHelper volleyHelper = new VolleyHelper(this, this);

        Map<String, String> params = new HashMap<>();
        params.put("xml", aBuffer);
        params.put("businessName", companyName);
        String logo = Base64.encodeToString(databaseHelper.getBlobValue(DatabaseHelper.COLUMN_LOGO, DatabaseHelper.TABLE_COMPANY_PROFILE), Base64.DEFAULT);
        String keyvisuallogo = Base64.encodeToString(databaseHelper.getBlobValue(DatabaseHelper.COLUMN_KEYVISUAL_PHOTO, DatabaseHelper.TABLE_COMPANY_PROFILE), Base64.DEFAULT);
        params.put("companylogo", logo);
        params.put("keyvisual", keyvisuallogo);
        volleyHelper.makeStringRequest(helper.getBaseURL() + "addxml.php", "tag", params);
        awesomeInfoDialog.setMessage("Submitting files to server");*/

    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof TimeoutError) {
            awesomeInfoDialog.setMessage("Server Timeout\nPlease Try Again !!");
            Log.e("Volley", "TimeoutError");
        } else if (error instanceof NoConnectionError) {
            awesomeInfoDialog.setMessage("No Internet Connectivity\nCheck your internet connection");
            Log.e("Volley", "NoConnectionError");
        } else if (error instanceof AuthFailureError) {
            awesomeInfoDialog.setMessage("Authentication Error\nContact server administrator");
        } else if (error instanceof ServerError) {
            awesomeInfoDialog.setMessage("Server Error\nContact server administrator");
        } else if (error instanceof NetworkError) {
            awesomeInfoDialog.setMessage("Netowrk Error\nTry again after some time");
        } else if (error instanceof ParseError) {
            Log.e("Volley", "ParseError");
        }
        awesomeInfoDialog.setCancelable(true);
    }

    @Override
    public void onResponse(String str) {
        Log.v("xml", "xml = " + str);
        JSONObject jsonObject = helper.getJson(str);

        try {
            if (jsonObject.get("result").equals(helper.SUCCESS)) {
                awesomeInfoDialog.setMessage("Succesfully uploaded Business");
                databaseHelper.updateIntValue(DatabaseHelper.TABLE_COMPANY_PROFILE, DatabaseHelper.COLUMN_STATUS, 1);
            } else {
                awesomeInfoDialog.setMessage("Business Already Uploaded");
                databaseHelper.updateIntValue(DatabaseHelper.TABLE_COMPANY_PROFILE, DatabaseHelper.COLUMN_STATUS, 1);
            }
        } catch (JSONException jse) {

        }
        //awesomeInfoDialog.setMessage(str);
        awesomeInfoDialog.setCancelable(true);

    }
}
