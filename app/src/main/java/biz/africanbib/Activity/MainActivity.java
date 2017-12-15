package biz.africanbib.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public int productMediaCount = 0;
    public int referencesCount = 0;
    public int ownersCount = 0;
    public int managersCount = 0;
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
    List<ImageData> imageData;


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
        imageData = new ArrayList<>();
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
        int i = -1;
        if (tab4.getAccepted()) {

            ArrayList<Object> items = tab1.getList();
            for (Object o :
                    items) {
                i++;
                if (o instanceof SimpleEditText) {
                    SimpleEditText simpleEditText = (SimpleEditText) o;


                    //Business Name
                    if (simpleEditText.getTitle().equals(tab1.businessName)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Business Name", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(0);
                            simpleEditText.setFocused(true);
                            tab4.getAdapter().notifyItemChanged(i);

                            return;
                        }
                    }

                    if (simpleEditText.getTitle().equals(tab1.registerationNumber)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Registeration Number", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(0);
                            simpleEditText.setFocused(true);
                            tab1.getAdapter().notifyItemChanged(i);
                            return;
                        }
                    }

                    //Telephone
                    if (simpleEditText.getTitle().equals(tab1.telephone)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Telephone", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(0);
                            simpleEditText.setFocused(true);
                            tab1.getAdapter().notifyItemChanged(i);

                            return;
                        }
                    }


                    //City
                    if (simpleEditText.getTitle().equals(tab1.city_town)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid City / Town", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(0);
                            simpleEditText.setFocused(true);
                            tab1.getAdapter().notifyItemChanged(i);

                            return;
                        }
                    }


                    //State
                    if (simpleEditText.getTitle().equals(tab1.state)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid State", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(0);
                            simpleEditText.setFocused(true);
                            tab1.getAdapter().notifyItemChanged(i);

                            return;
                        }
                    }


                    //Country
                    if (simpleEditText.getTitle().equals(tab1.country)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Country", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(0);
                            simpleEditText.setFocused(true);
                            tab1.getAdapter().notifyItemChanged(i);

                            return;
                        }
                    }
                } else if (o instanceof SimpleImage) {
                    SimpleImage simpleImage = (SimpleImage) o;
                    if (simpleImage.getTitle().equals(tab1.corporateLogo)) {
                        if (simpleImage.getImage() == null) {
                            Toast.makeText(this, "Please select Corporate Logo", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(0);
                            return;
                        }
                    }

                    if (simpleImage.getTitle().equals(tab1.keyVisual)) {
                        if (simpleImage.getImage() == null) {
                            Toast.makeText(this, "Please select KeyVisual Photo", Toast.LENGTH_SHORT).show();
                            tab1.getAdapter().notifyItemChanged(i);
                            viewPager.setCurrentItem(0);
                            return;
                        }
                    }
                }
            }

            //items.clear();
            i = -1;
            ArrayList<Object> items4 = tab4.getList();
            for (Object o :
                    items4) {
                i++;
                if (o instanceof SimpleEditText) {
                    SimpleEditText simpleEditText = (SimpleEditText) o;


                    //Business Name
                    if (simpleEditText.getTitle().equals(tab4.nameOfCollector)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Collector Name", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(3);
                            simpleEditText.setFocused(true);
                            tab4.getAdapter().notifyItemChanged(i);

                            Log.v("Validate", "position = " + i);
                            return;
                        }
                    }

                    //Telephone
                    if (simpleEditText.getTitle().equals(tab4.authorizedBy)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Authorizing Name", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(3);
                            simpleEditText.setFocused(true);
                            tab4.getAdapter().notifyItemChanged(i);

                            return;
                        }
                    }


                    //City
                    if (simpleEditText.getTitle().equals(tab4.placeOfCollection)) {
                        if (helper.checkForInput(simpleEditText.getValue()) == null) {
                            Toast.makeText(this, "Please enter a valid Place of Collection", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(3);
                            simpleEditText.setFocused(true);
                            tab4.getAdapter().notifyItemChanged(i);

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
            xmlSerializer.text(System.getProperty("line.separator"));
            xmlSerializer.startTag(null, "Organization");

            addXmlContent(items1, xmlSerializer);
            addXmlContent(items2, xmlSerializer);
            addXmlContent(items3, xmlSerializer);
            addXmlContent(items4, xmlSerializer);

            xmlSerializer.text(System.getProperty("line.separator"));
            xmlSerializer.endTag(null, "Organization");
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
        String personTag;
        for (int i = 0; i < items.size() - 1; i++) {
            personTag = null;
            Object item = items.get(i);
            Log.v(TAG, "i =" + i);
            if (items.get(i) instanceof Heading) {
                Heading heading = (Heading) items.get(i);
                headTag = heading.getXmlTag();
                if (heading.getHeading().equals("CONTACT PERSON")) {
                    xmlSerializer.text(System.getProperty("line.separator"));
                    xmlSerializer.startTag(null, headTag);
                    xmlSerializer.text(System.getProperty("line.separator"));
                    xmlSerializer.startTag(null, "type");
                    xmlSerializer.text("0");
                    xmlSerializer.endTag(null, "type");
                } else if (heading.getHeading().equals("COMPANY POSTAL ADDRESS") || heading.getHeading().equals("COMPANY CONTACT")) {
                    xmlSerializer.text(System.getProperty("line.separator"));
                    xmlSerializer.startTag(null, headTag);
                    xmlSerializer.text(System.getProperty("line.separator"));
                    xmlSerializer.startTag(null, "type");
                    xmlSerializer.text("0");
                    xmlSerializer.endTag(null, "type");
                    xmlSerializer.text(System.getProperty("line.separator"));
                    xmlSerializer.startTag(null, "isPrimary");
                    xmlSerializer.text("1");
                    xmlSerializer.endTag(null, "isPrimary");
                } else {
                    if (headTag != null) {
                        Log.v(TAG, "START TAG " + headTag);
                        xmlSerializer.text(System.getProperty("line.separator"));
                        xmlSerializer.startTag(null, headTag);
                    }
                }
            } else {
                while (!(item instanceof Heading)) {

                    if (item instanceof SimpleEditText) {
                        SimpleEditText simpleEditText = (SimpleEditText) item;
                        // Log.v(TAG, "Found " + simpleEditText.getTitle() + " with value = " + simpleEditText.getValue() + " where i = " + i);
                        if (simpleEditText.getValue() != null) {
                            if (simpleEditText.getValue().trim().length() != 0) {
                                xmlSerializer.text(System.getProperty("line.separator"));
                                xmlSerializer.startTag(null, simpleEditText.getXmlTag());
                                xmlSerializer.text(Helper.forReplacementString(simpleEditText.getValue()));
                                xmlSerializer.endTag(null, simpleEditText.getXmlTag());
                            }
                        }
                    } else if (item instanceof SimpleDate) {
                        SimpleDate date = (SimpleDate) item;
                        //Log.v(TAG, "Found " + date.getTitle() + " with value = " + date.getValue() + " where i = " + i);
                        if (date.getValue() != null) {
                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, date.getXmlTag());
                            xmlSerializer.text(helper.toDays(date.getValue()));
                            xmlSerializer.endTag(null, date.getXmlTag());
                        }
                    } else if (item instanceof SimpleImage) {
                        SimpleImage simpleImage = (SimpleImage) item;
                        //Log.v(TAG, "Found " + date.getTitle() + " with value = " + date.getValue() + " where i = " + i);
                        if (simpleImage.getImage() != null) {

                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, simpleImage.getXmlTag());
                            String tagName = helper.checkForInput(simpleImage.getTitle().toLowerCase());
                            tagName = tagName.replace("*", "");
                            tagName = tagName.replace("photo", "");
                            tagName = tagName.replace(" ", "");
                            tagName = tagName.replace("(", "");
                            tagName = tagName.replace(") ", "");
                            xmlSerializer.text(tagName + ".jpg");
                            xmlSerializer.endTag(null, simpleImage.getXmlTag());
                            imageData.add(new ImageData(simpleImage.getColumnName(), simpleImage.getTableName(), simpleImage.getRowno(), tagName));
                        }
                    } else if (item instanceof DropDown) {
                        DropDown dropDown = (DropDown) item;
                        String collectedBy = " ";
                        if (dropDown.getColumnName().equals(DatabaseHelper.COLUMN_PREFIX)) {
                            personTag = "person";
                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, personTag);
                        }
                        if (dropDown.getColumnName().equals(DatabaseHelper.COLUMN_PLACE_OF_COLECTION)) {
                            if (helper.getSelectedValue(dropDown, dropDown.getSelectedPosition()).equals("Others")) {
                                SimpleEditText edt = (SimpleEditText) items.get(++i);
                                SimpleDate date = (SimpleDate) items.get(++i);
                                if (edt.getValue().trim().length() == 0) {
                                    if (date.getValue() != null && date.getValue().trim().length() != 0) {
                                        xmlSerializer.text(System.getProperty("line.separator"));
                                        xmlSerializer.startTag(null, ((DropDown) item).getXmlTag());
                                        collectedBy = helper.toDays(date.getValue());
                                        xmlSerializer.text(collectedBy);
                                        xmlSerializer.endTag(null, ((DropDown) item).getXmlTag());
                                    }
                                } else {
                                    xmlSerializer.text(System.getProperty("line.separator"));
                                    xmlSerializer.startTag(null, ((DropDown) item).getXmlTag());
                                    collectedBy = Helper.forReplacementString(edt.getValue()) + " , " + helper.toDays(date.getValue());
                                    xmlSerializer.text(collectedBy);
                                    xmlSerializer.endTag(null, ((DropDown) item).getXmlTag());
                                }
                            } else {
                                if (items.get(i + 1) instanceof SimpleEditText) {
                                    i = i + 2;
                                } else i++;
                                SimpleDate date = (SimpleDate) items.get(i);
                                xmlSerializer.text(System.getProperty("line.separator"));
                                xmlSerializer.startTag(null, ((DropDown) item).getXmlTag());
                                collectedBy = helper.getSelectedValue(dropDown, dropDown.getSelectedPosition()) + ", " + helper.toDays(date.getValue());
                                xmlSerializer.text(collectedBy);
                                xmlSerializer.endTag(null, ((DropDown) item).getXmlTag());
                            }
                        } else {
                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, ((DropDown) item).getXmlTag());
                            xmlSerializer.text(helper.getSelectedValue(dropDown, dropDown.getSelectedPosition()));
                            xmlSerializer.endTag(null, ((DropDown) item).getXmlTag());
                        }
                    } else if (item instanceof MultiSelectDropdown) {
                        MultiSelectDropdown multiSelectDropdown = (MultiSelectDropdown) item;
                        //Log.v(TAG, "Found " + multiSelectDropdown.getTitle() + " with value = " + multiSelectDropdown.getSelectedIndices() + " where i = " + i);
                        List<Integer> indices = multiSelectDropdown.getSelectedIndices();
                        if (indices.size() > 0) {
                            for (int index : indices) {
                                xmlSerializer.text(System.getProperty("line.separator"));
                                xmlSerializer.startTag(null, multiSelectDropdown.getXmlTag());
                                helper.childTags(multiSelectDropdown, index, xmlSerializer);
                                xmlSerializer.endTag(null, multiSelectDropdown.getXmlTag());
                            }
                        }
                    } else if (item instanceof SimpleText) {
                        SimpleText simpleText = (SimpleText) item;
                        i = simpleTextXml(simpleText, i, items, xmlSerializer);
                    }
                    if (i < items.size() - 1) {
                        i++;
                        item = items.get(i);
                    } else {
                        break;
                    }

                }
                i--;
                if (personTag != null) {
                    xmlSerializer.text(System.getProperty("line.separator"));
                    xmlSerializer.endTag(null, personTag);
                }
                if (headTag != null) {
                    Log.v(TAG, "ENDTAG " + headTag);
                    xmlSerializer.text(System.getProperty("line.separator"));
                    xmlSerializer.endTag(null, headTag);
                }
            }
        }
    }

    private int simpleTextXml(SimpleText simpleText, int i, ArrayList<Object> items, XmlSerializer xmlSerializer) throws IOException {
        Object obj;
        String personTag;
        Log.d(TAG, "size:" + items.size());
        Log.d(TAG, "simpleTextXml: " + i + ":" + simpleText.getTitle());
        String tag = simpleText.getXmlTag();
        if (simpleText.getTitle().equals("DISCLAIMER\n\n" +
                "I certify that the information provided in this form is true, complete and correct to the best of my knowledge and belief. I understand that the information provided in this form is checked and updated by AfricanBIB GmbH on the AfricanBIB website with due diligence on a regular basis. This notwithstanding, data may become subject to changes during the intervening period. Therefore AfricanBIB GmbH does not assume any liability or guarantee for the timeliness, accuracy and completeness of the information provided. This applies also to other websites that may be accessed through hyperlinks. AfricanBIB GmbH assumes no responsibility for the contents of websites that can be accessed through such links.\n" +
                "Further, AfricanBIB GmbH reserves the right to change or amend the information provided at any time and without prior notice.\n" +
                "Contents and structure of this form sites are copyright protected. Reproduction of information or data content, in particular the use of text (whether in full or in part), pictures or graphics, requires the prior approval of AfricanBIB GmbH.\n")) {
            return i;
        }
        i++;
        while (!(items.get(i) instanceof Add)) {
            if (tag != null) {
                xmlSerializer.text(System.getProperty("line.separator"));
                xmlSerializer.startTag(null, tag);
            }
            if (simpleText.getTitle().equals("OWNERS")) {
                xmlSerializer.text(System.getProperty("line.separator"));
                xmlSerializer.startTag(null, "type");
                xmlSerializer.text("1");
                xmlSerializer.endTag(null, "type");
            } else if (simpleText.getTitle().equals("MANAGERS")) {
                xmlSerializer.text(System.getProperty("line.separator"));
                xmlSerializer.startTag(null, "type");
                xmlSerializer.text("2");
                xmlSerializer.endTag(null, "type");
            } else if (simpleText.getTitle().equals("REFERENCES")) {
                xmlSerializer.text(System.getProperty("line.separator"));
                xmlSerializer.startTag(null, "type");
                xmlSerializer.text("3");
                xmlSerializer.endTag(null, "type");
            } else if (simpleText.getTitle().equals("SUBSIDIARIES")) {
                xmlSerializer.text(System.getProperty("line.separator"));
                xmlSerializer.startTag(null, "type");
                xmlSerializer.text("4");
                xmlSerializer.endTag(null, "type");
            }
            obj = items.get(i);
            personTag = null;
            while (!(obj instanceof Divider)) {

                if (obj instanceof SimpleEditText) {
                    SimpleEditText simpleEditText = (SimpleEditText) obj;
                    Log.d(TAG, "simpleTextXml: " + i + ":" + simpleEditText.getTitle());
                    if (simpleEditText.getValue() != null) {
                        if (simpleEditText.getValue().trim().length() != 0) {
                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, simpleEditText.getXmlTag());
                            xmlSerializer.text(Helper.forReplacementString(simpleEditText.getValue()));
                            xmlSerializer.endTag(null, simpleEditText.getXmlTag());
                        }
                    }
                } else if (obj instanceof DropDown) {
                    DropDown dropDown = (DropDown) obj;
                    Log.d(TAG, "simpleTextXml: " + i + ":" + dropDown.getHeading());
                    if (dropDown.getColumnName().equals(DatabaseHelper.COLUMN_PREFIX)) {
                        personTag = "person";
                        xmlSerializer.text(System.getProperty("line.separator"));
                        xmlSerializer.startTag(null, personTag);
                    }
                    xmlSerializer.text(System.getProperty("line.separator"));
                    xmlSerializer.startTag(null, ((DropDown) obj).getXmlTag());
                    xmlSerializer.text(helper.getSelectedValue(dropDown, dropDown.getSelectedPosition()));
                    xmlSerializer.endTag(null, ((DropDown) obj).getXmlTag());
                } else if (obj instanceof MultiSelectDropdown) {
                    MultiSelectDropdown multiSelectDropdown = (MultiSelectDropdown) obj;
                    Log.d(TAG, "simpleTextXml: " + i + ":" + multiSelectDropdown.getTitle());
                    List<Integer> indices = multiSelectDropdown.getSelectedIndices();
                    if (indices.size() > 0) {
                        for (int index :
                                indices) {
                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, multiSelectDropdown.getXmlTag());
                            helper.childTags(multiSelectDropdown, index, xmlSerializer);
                            xmlSerializer.endTag(null, multiSelectDropdown.getXmlTag());
                        }
                    }
                } else if (obj instanceof SimpleDate) {
                    SimpleDate date = (SimpleDate) obj;
                    Log.d(TAG, "simpleTextXml: " + i + ":" + date.getTitle());
                    if (date.getValue() != null) {
                        xmlSerializer.text(System.getProperty("line.separator"));
                        xmlSerializer.startTag(null, date.getXmlTag());
                        xmlSerializer.text(helper.toDays(date.getValue()));
                        xmlSerializer.endTag(null, date.getXmlTag());
                    }
                } else if (obj instanceof SimpleImage) {
                    SimpleImage simpleImage = (SimpleImage) obj;
                    Log.d(TAG, "simpleTextXml: " + i + ":" + simpleImage.getTitle());
                    if (simpleImage.getImage() != null) {
                        xmlSerializer.text(System.getProperty("line.separator"));
                        xmlSerializer.startTag(null, simpleImage.getXmlTag());
                        String tagName = helper.checkForInput(simpleImage.getTitle());
                        tagName = tagName.replace("*", "");
                        tagName = tagName.replace("photo", "");
                        tagName = tagName.replace("(", "");
                        tagName = tagName.replace(") ", "");
                        if (simpleImage.getTitle().equals("Product Media (Photo / Documents)")) {
                            tagName = tagName.replace("Photo/Documents)", "");
                            productMediaCount++;
                            tagName = tagName + productMediaCount;
                        }
                        if (simpleImage.getTitle().equals("Manager Logo")) {
                            managersCount++;
                            tagName = tagName + managersCount;
                        }
                        if (simpleImage.getTitle().equals("Owners Logo")) {
                            ownersCount++;
                            tagName = tagName + ownersCount;
                        }
                        if (simpleImage.getTitle().equals("Institution Logo")) {
                            referencesCount++;
                            tagName = tagName + referencesCount;
                        }
                        xmlSerializer.text(tagName + ".jpg");
                        xmlSerializer.endTag(null, simpleImage.getXmlTag());
                        imageData.add(new ImageData(simpleImage.getColumnName(), simpleImage.getTableName(), simpleImage.getRowno(), tagName));
                    }
                }
                if (i < items.size() - 1) {
                    i++;
                    Log.d(TAG, "simpleTextXml: " + i);
                    obj = items.get(i);
                } else break;
            }
            if (personTag != null) {
                xmlSerializer.text(System.getProperty("line.separator"));
                xmlSerializer.endTag(null, personTag);
            }
            if (tag != null) {
                xmlSerializer.text(System.getProperty("line.separator"));
                xmlSerializer.endTag(null, tag);
            }
            if (i >= items.size() - 1)
                return i;
            else {
                i++;
                Log.d(TAG, "last : " + i);
            }
        }
        return i - 1;
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
            aBuffer = aBuffer.replace("<.?></.?>", "");
            Log.v(TAG, aBuffer);
            myReader.close();
            fIn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        VolleyHelper volleyHelper = new VolleyHelper(this, this);

        Map<String, String> params = new HashMap<>();

        params.put("xml", aBuffer);
        params.put("businessName", companyName);

       /* String logo = Base64.encodeToString(databaseHelper.getBlobValue(DatabaseHelper.COLUMN_LOGO, DatabaseHelper.TABLE_COMPANY_PROFILE), Base64.DEFAULT);
        String keyvisuallogo = Base64.encodeToString(databaseHelper.getBlobValue(DatabaseHelper.COLUMN_KEYVISUAL_PHOTO, DatabaseHelper.TABLE_COMPANY_PROFILE), Base64.DEFAULT);
        params.put("companylogo", logo);
        params.put("keyvisual", keyvisuallogo);
       */
        volleyHelper.makeStringRequest(helper.getBaseURL() + "addxml.php", "tag", params);

        int size = imageData.size();
        String[] images = new String[size];
        String[] imagenames = new String[size];
        int i = 0;
        for (ImageData id : imageData) {
            Map<String, String> params1 = new HashMap<>();
            if (id.Row == -1)
                images[i] = Base64.encodeToString(databaseHelper.getBlobValue(id.ColumnName, id.TableName), Base64.DEFAULT);
            else
                images[i] = Base64.encodeToString(databaseHelper.getBlobFromRow(id.ColumnName, id.TableName, id.Row), Base64.DEFAULT);
            imagenames[i] = id.Name;
            params1.put("imagename", imagenames[i]);
            params1.put("image", images[i]);
            params1.put("businessName", companyName);
            i++;
            volleyHelper.makeStringRequest(helper.getBaseURL() + "createimage.php", "tag" + i, params1);
        }
        awesomeInfoDialog.setMessage("Submitting files to server");

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
        JSONObject jsonObject = helper.getJson(str);
        Log.d(TAG, jsonObject.toString());
        try {
            if (jsonObject.get("result").equals(helper.SUCCESS)) {
                awesomeInfoDialog.setMessage("Succesfully uploaded Business");
                databaseHelper.updateIntValue(DatabaseHelper.TABLE_COMPANY_PROFILE, DatabaseHelper.COLUMN_STATUS, 1);
                databaseHelper.updateDateTime(DatabaseHelper.TABLE_COMPANY_PROFILE);
            } else {
                awesomeInfoDialog.setMessage("Business Already Uploaded");
                databaseHelper.updateIntValue(DatabaseHelper.TABLE_COMPANY_PROFILE, DatabaseHelper.COLUMN_STATUS, 1);
            }
        } catch (JSONException jse) {

        }
        //awesomeInfoDialog.setMessage(str);
        awesomeInfoDialog.setCancelable(true);

    }

    private class ImageData {
        String ColumnName;
        String TableName;
        int Row;
        String Name;

        public ImageData(String columnName, String tableName, int row, String name) {
            ColumnName = columnName;
            TableName = tableName;
            Row = row;
            Name = name;
        }
    }
}
