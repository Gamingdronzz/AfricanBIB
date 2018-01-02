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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.africanbib.App.AppController;
import biz.africanbib.Models.Add;
import biz.africanbib.Models.ChooseFile;
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
    public int subsidiaryCount = 0;
    //This is our tablayout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    //SegmentedProgressBar segmentedPrgressBar;
    FloatingActionButton goLeft, goRight;
    DatabaseHelper databaseHelper;
    String companyName = null;
    ViewPagerAdapter adapter;
    Helper helper;
    public static boolean first = true;
    private Button buttonValidate;
    List<ImageData> imageData;
    List<FileData> fileData;
    int currentImage = 0;
    int currentFile = 0;
    VolleyHelper volleyHelper;
    boolean logUpload = false;


    AwesomeProgressDialog awesomeDialog;
    AwesomeSuccessDialog awesomeSuccessDialog;

    boolean xmlUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(getApplicationContext(), DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        bindViews();
        setUpTabLayout();
        init();
        handleIntent();
        volleyHelper = new VolleyHelper(this, this);
        Log.v(TAG, "Current company id = " + DatabaseHelper.getCurrentCompanyId());


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu); //your file name
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.uploadLog:
                UploadLog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UploadLog() {
        try {

            String file, fileName;
            Map<String, String> logParams = new HashMap<>();
            String logFile = getFilesDir() + "/" + helper.checkForInput(companyName) + ".txt";
            file = Base64.encodeToString(helper.getByteArrayFromFile(logFile), Base64.DEFAULT);
            fileName = companyName;
            logParams.put("filename", fileName);
            logParams.put("file", file);
            logParams.put("application", "AfricanBIB");
            volleyHelper.makeStringRequest(helper.getUploadURL() + "uploadLog.php", fileName, logParams);
            logUpload = true;
            Log.v(TAG, "Sending file ");
            awesomeDialog.setTitle("Uploading Log File")
                    .setColoredCircle(R.color.dialogProgressBackgroundColor)
                    .setMessage("Please Wait..")
                    .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                    .show();
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
            new AwesomeErrorDialog(this)
                    .setTitle("Error")
                    .setMessage("Unable to upload log file!")
                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(false).setButtonText(getString(R.string.dialog_ok_button))
                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                    .setButtonText(getString(R.string.dialog_ok_button))
                    .setErrorButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            finish();
                        }
                    })
                    .show();
        }
    }


    private void handleIntent() {
        Intent intent = getIntent();
        typeOfBusiness = intent.getIntExtra("type", -1);
    }

    private void init() {
        helper = new Helper(this);

        imageData = new ArrayList<>();
        fileData = new ArrayList<>();
        goLeft.setVisibility(View.INVISIBLE);
        logUpload = false;
        awesomeDialog = new AwesomeProgressDialog(this)
                .setTitle("Uploading Business")
                .setMessage("Please wait..")
                .setColoredCircle(R.color.dialogProgressBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
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
        AppController.getInstance().writeLogToFile(helper.checkForInput(companyName));
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
        //segmentedProgressBar = (SegmentedProgressBar) findViewById(R.id.segmented_progressbar);
        goLeft = (FloatingActionButton) findViewById(R.id.fab_go_left);
        goRight = (FloatingActionButton) findViewById(R.id.fab_go_right);
        buttonValidate = (Button) findViewById(R.id.validate);
        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImage = 0;
                currentFile = 0;
                xmlUploaded = false;
                if (imageData != null) {
                    imageData.clear();
                }
                if (fileData != null) {
                    fileData.clear();
                }
                checkValidation();
            }
        });

    }

    private void checkValidation() {
        try {
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
                    }
                }
                i = -1;
                ArrayList<Object> items4 = tab4.getList();
                tab1.getAdapter().notifyDataSetChanged();
                tab2.getAdapter().notifyDataSetChanged();
                tab3.getAdapter().notifyDataSetChanged();
                tab4.getAdapter().notifyDataSetChanged();

                awesomeDialog.setMessage("Validating...");
                awesomeDialog.show();
                Test(items, items2, items3, items4);

            } else {
                Toast.makeText(this, "Please accept the agreeement first", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
            new AwesomeErrorDialog(this)
                    .setTitle("Error")
                    .setMessage("Error in Validating")
                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(false).setButtonText(getString(R.string.dialog_ok_button))
                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                    .setButtonText(getString(R.string.dialog_ok_button))
                    .setErrorButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            finish();
                        }
                    })
                    .show();
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
        //segmentedProgressBar.setCompletedSegments(position);

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
        productMediaCount = 0;
        referencesCount = 0;
        ownersCount = 0;
        managersCount = 0;
        subsidiaryCount = 0;
        try {
            Log.v(TAG, "Trying to create xml File at : " + getApplicationContext().getFilesDir());
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
            showXML();
        } catch (IOException e) {
            Log.d(TAG, Log.getStackTraceString(e));
            new AwesomeErrorDialog(this)
                    .setTitle("Error")
                    .setMessage("Error in generating xml!")
                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(false).setButtonText(getString(R.string.dialog_ok_button))
                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                    .setButtonText(getString(R.string.dialog_ok_button))
                    .setErrorButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            finish();
                        }
                    })
                    .show();
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
                if (heading.getHeading().equals("CONTACT PERSON") || heading.getHeading().equals("COMPANY CONTACT")) {
                    xmlSerializer.text(System.getProperty("line.separator"));
                    xmlSerializer.startTag(null, headTag);
                    xmlSerializer.text(System.getProperty("line.separator"));
                    xmlSerializer.startTag(null, "type");
                    xmlSerializer.text("0");
                    xmlSerializer.endTag(null, "type");
                } else if (heading.getHeading().equals("COMPANY POSTAL ADDRESS")) {
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
                                xmlSerializer.text(helper.forReplacementString(simpleEditText.getValue()));
                                xmlSerializer.endTag(null, simpleEditText.getXmlTag());
                            }
                        }
                    } else if (item instanceof SimpleDate) {
                        SimpleDate date = (SimpleDate) item;
                        if (date.getValue() != null) {
                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, date.getXmlTag());
                            xmlSerializer.text(helper.reverseFormat(date.getValue()));
                            xmlSerializer.endTag(null, date.getXmlTag());
                        }
                    } else if (item instanceof SimpleImage) {
                        SimpleImage simpleImage = (SimpleImage) item;
                        if (simpleImage.getImage() != null) {

                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, simpleImage.getXmlTag());
                            String tagName = helper.checkForInput(simpleImage.getTitle().toLowerCase());
                            tagName = tagName.replace("*", "");
                            tagName = tagName.replace("photo", "");
                            tagName = tagName.replace(" ", "");
                            tagName = tagName.replace("(", "");
                            tagName = tagName.replace(")", "");
                            xmlSerializer.text(tagName + ".jpg");
                            xmlSerializer.endTag(null, simpleImage.getXmlTag());
                            imageData.add(new ImageData(simpleImage.getColumnName(), simpleImage.getTableName(), simpleImage.getRowno(), tagName));
                        }
                    } else if (item instanceof DropDown) {
                        DropDown dropDown = (DropDown) item;
                        if (dropDown.getColumnName().equals(DatabaseHelper.COLUMN_PREFIX)) {
                            personTag = "person";
                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, personTag);
                        }

                        if (dropDown.getColumnName().equals(DatabaseHelper.COLUMN_PLACE_OF_COLECTION)) {
                            String collectedBy = " ";
                            String atLoc = " ";
                            String onDate = " ";
                            SimpleEditText edt;
                            SimpleDate date;
                            SimpleEditText edtLoc;
                            if (helper.getSelectedValue(dropDown, dropDown.getSelectedPosition()).equals("Others")) {
                                edt = (SimpleEditText) items.get(++i);
                                edtLoc = (SimpleEditText) items.get(++i);
                                date = (SimpleDate) items.get(++i);
                                collectedBy = edt.getValue();
                            } else {
                                if (items.get(i + 2) instanceof SimpleEditText) {
                                    i = i + 2;
                                } else i++;
                                edtLoc = (SimpleEditText) items.get(i);
                                date = (SimpleDate) items.get(++i);
                                collectedBy = helper.getSelectedValue(dropDown, dropDown.getSelectedPosition());
                            }
                            atLoc = edtLoc.getValue();
                            onDate = helper.reverseFormat(date.getValue());
                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, ((DropDown) item).getXmlTag());
                            if (!collectedBy.isEmpty()) {
                                if (!atLoc.isEmpty()) {
                                    if (onDate != null) {
                                        xmlSerializer.text(helper.forReplacementString(collectedBy + ", in " + atLoc + " on " + onDate));
                                    } else
                                        xmlSerializer.text(helper.forReplacementString(collectedBy + ", in " + atLoc));

                                } else {
                                    if (onDate != null) {
                                        xmlSerializer.text(helper.forReplacementString(collectedBy + ", on " + onDate));
                                    } else
                                        xmlSerializer.text(helper.forReplacementString(collectedBy));
                                }
                            } else
                                xmlSerializer.text("");
                            xmlSerializer.endTag(null, ((DropDown) item).getXmlTag());
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
                            xmlSerializer.text(helper.forReplacementString(simpleEditText.getValue()));
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
                    if (dropDown.getXmlTag() != null) {
                        xmlSerializer.text(System.getProperty("line.separator"));
                        xmlSerializer.startTag(null, ((DropDown) obj).getXmlTag());
                        xmlSerializer.text(helper.getSelectedValue(dropDown, dropDown.getSelectedPosition()));
                        xmlSerializer.endTag(null, ((DropDown) obj).getXmlTag());
                    }
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
                } else if (obj instanceof ChooseFile) {
                    ChooseFile chooseFile = (ChooseFile) obj;
                    int type = databaseHelper.getIntFromRow(chooseFile.getTableName(), DatabaseHelper.COLUMN_FORMAT, chooseFile.getRowno());
                    String file = databaseHelper.getStringFromRow(chooseFile.getTableName(), chooseFile.getColumnName(), chooseFile.getRowno());
                    if (file != null) {
                        if (type == 0) {
                            String extension = file.substring(file.lastIndexOf("."));
                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, "photo");
                            xmlSerializer.text("Media" + chooseFile.getRowno() + extension);
                            xmlSerializer.endTag(null, "photo");
                            imageData.add(new ImageData(DatabaseHelper.COLUMN_SELECTED_IMAGE, chooseFile.getTableName(), chooseFile.getRowno(), "Media" + chooseFile.getRowno()));
                        } else if (type == 1) {
                            xmlSerializer.text(System.getProperty("line.separator"));
                            xmlSerializer.startTag(null, "file");
                            xmlSerializer.text("File" + chooseFile.getRowno() + ".pdf");
                            xmlSerializer.endTag(null, "file");
                            fileData.add(new FileData(chooseFile.getColumnName(), chooseFile.getTableName(), chooseFile.getRowno(), "File" + chooseFile.getRowno()));
                        }
                    }
                } else if (obj instanceof SimpleDate) {
                    SimpleDate date = (SimpleDate) obj;
                    Log.d(TAG, "simpleTextXml: " + i + ":" + date.getTitle());
                    if (date.getValue() != null) {
                        xmlSerializer.text(System.getProperty("line.separator"));
                        xmlSerializer.startTag(null, date.getXmlTag());
                        xmlSerializer.text(helper.reverseFormat(date.getValue()));
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
                        if (simpleImage.getTitle().equals("Manager Image")) {
                            managersCount++;
                            tagName = tagName + managersCount;
                        }
                        if (simpleImage.getTitle().equals("Owners Image")) {
                            ownersCount++;
                            tagName = tagName + ownersCount;
                        }
                        if (simpleImage.getTitle().equals("Subsidiary Logo")) {
                            subsidiaryCount++;
                            tagName = tagName + subsidiaryCount;
                        }
                        if (simpleImage.getTitle().equals("Institution Logo")) {
                            referencesCount++;
                            tagName = tagName + referencesCount;
                        }

                        //Add Product Media Group Tag
                        if (simpleImage.getXmlTag().equals("productMedia")) {
                            xmlSerializer.startTag(null, "type");
                            xmlSerializer.text("7");
                            xmlSerializer.endTag(null, "type");
                            xmlSerializer.startTag(null, "photo");
                            xmlSerializer.text(tagName + ".jpg");
                            xmlSerializer.endTag(null, "photo");
                        } else {
                            xmlSerializer.text(tagName + ".jpg");
                        }
                        //Log.v(TAG,"Image name = " + simpleImage.getXmlTag() + "\n Tag value = " + tagName);

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
        File file = new File(getApplicationContext().getFilesDir(), helper.checkForInput(companyName) + ".xml");
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
            Log.d(TAG, Log.getStackTraceString(e));
            new AwesomeErrorDialog(this)
                    .setTitle("Error")
                    .setMessage("Error in generating xml!")
                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(false).setButtonText(getString(R.string.dialog_ok_button))
                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                    .setButtonText(getString(R.string.dialog_ok_button))
                    .setErrorButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            finish();
                        }
                    })
                    .show();
        } catch (IOException e) {
            Log.d(TAG, Log.getStackTraceString(e));
            new AwesomeErrorDialog(this)
                    .setTitle("Error")
                    .setMessage("Error in generating xml!")
                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(false).setButtonText(getString(R.string.dialog_ok_button))
                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                    .setButtonText(getString(R.string.dialog_ok_button))
                    .setErrorButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            finish();
                        }
                    })
                    .show();
        }

        sendXMLForUpload(aBuffer);
    }

    @Override
    public void onError(VolleyError error) {
        if (logUpload) {
            awesomeDialog.hide();
            //awesomeDialog.setCancelable(true);
            final AwesomeSuccessDialog awesomeSuccessDialog = new AwesomeSuccessDialog(this);
            awesomeSuccessDialog.setPositiveButtonText("Upload Log Again")
                    .setCancelable(false)
                    .setNegativeButtonText("Some Other Time")
                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                    .setNegativeButtonbackgroundColor(R.color.dialogErrorBackgroundColor)
                    .setPositiveButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            UploadLog();
                        }
                    })
                    .setNegativeButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            awesomeSuccessDialog.hide();
                        }
                    })

                    .show();
        } else {
            awesomeDialog.hide();
            //awesomeDialog.setCancelable(true);
            final AwesomeSuccessDialog awesomeSuccessDialog = new AwesomeSuccessDialog(this);
            awesomeSuccessDialog.setPositiveButtonText("Try Again")
                    .setCancelable(false)
                    .setNegativeButtonText("Some Other Time")
                    .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                    .setNegativeButtonbackgroundColor(R.color.dialogErrorBackgroundColor)
                    .setPositiveButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            buttonValidate.performClick();
                        }
                    })
                    .setNegativeButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            awesomeSuccessDialog.hide();
                        }
                    });
            databaseHelper.updateIntValue(DatabaseHelper.TABLE_COMPANY_PROFILE, DatabaseHelper.COLUMN_STATUS, 0);
            awesomeSuccessDialog.setColoredCircle(R.color.dialogErrorBackgroundColor);
            awesomeSuccessDialog.setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white);
            String msg = "";
            if (error instanceof TimeoutError) {
                msg = "Server Timeout\nSlow Internet Connection !!";
                Log.e("Volley", "TimeoutError\n");
            } else if (error instanceof NoConnectionError) {
                msg = "No Internet Connectivity\nTurn on your internet connection";
                Log.e("Volley", "NoConnectionError");
            } else if (error instanceof AuthFailureError) {
                msg = "Authentication Error\nContact server administrator";
            } else if (error instanceof ServerError) {
                msg = "Server Error\nContact server administrator";
            } else if (error instanceof NetworkError) {
                msg = "Network Error";
            } else {
                msg = "Technical Error";
            }
            awesomeSuccessDialog.setMessage(msg);
            awesomeSuccessDialog.show();
            //awesomeDialog.setMessage("Uploading Interrupted!\nDo you want to try again?\n\nWarning: Clicking no will erase your previous progress.");
            //awesomeDialog.setCancelable(true);
        }
    }

    @Override
    public void onResponse(String str) {
        JSONObject jsonObject = helper.getJson(str);
        Log.d(TAG, jsonObject.toString());
        try {
            if (jsonObject.get("action").equals("Creating Log File")) {
                if (jsonObject.get("result").equals(helper.SUCCESS)) {
                    awesomeDialog.hide();
                    awesomeSuccessDialog = new AwesomeSuccessDialog(this);
                    awesomeSuccessDialog.setTitle("Log Uploaded Successfully")
                            .setMessage("")
                            .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_done_black_24dp, R.color.white)
                            .setCancelable(false)
                            .setPositiveButtonText("OK")
                            .setPositiveButtonbackgroundColor(R.color.colorPrimary)
                            .setPositiveButtonTextColor(R.color.white)
                            .setPositiveButtonClick(new Closure() {
                                @Override
                                public void exec() {
                                    awesomeSuccessDialog.hide();
                                }
                            })
                            .show();
                }
            } else if (jsonObject.get("action").equals("Creating XML")) {
                if (jsonObject.get("result").equals(helper.SUCCESS)) {
                    awesomeDialog.setMessage("XML Generated and Uploaded");
                    if (imageData.size() > 0)
                        sendImageForUpload(imageData.get(currentImage));
                    else if (fileData.size() > 0)
                        sendFileForUpload(fileData.get(currentFile));
                    else {
                        showSuccessDialog();
                    }
                } else {
                    onError(new VolleyError());
                }
            } else if (jsonObject.get("action").equals("Creating Image")) {
                if (jsonObject.get("result").equals(helper.SUCCESS)) {
                    currentImage++;
                    //awesomeDialog.setMessage("Uploaded Image " + currentImage + " of " + imageData.size());
                    if (currentImage == imageData.size()) {
                        if (fileData.size() > 0)
                            sendFileForUpload(fileData.get(currentFile));
                        else {
                            showSuccessDialog();
                        }
                    } else {
                        sendImageForUpload(imageData.get(currentImage));
                    }
                } else {

                }
            } else if (jsonObject.get("action").equals("Creating File")) {
                if (jsonObject.get("result").equals(helper.SUCCESS)) {
                    currentFile++;
                    awesomeDialog.setMessage("Uploaded File " + currentFile + " of " + fileData.size());
                    if (currentFile != fileData.size()) {
                        sendFileForUpload(fileData.get(currentFile));
                    } else {
                        showSuccessDialog();

                    }
                } else {

                }
            }
        } catch (JSONException jse) {

        }
    }

    private void showSuccessDialog() {
        awesomeDialog.hide();
        awesomeSuccessDialog = new AwesomeSuccessDialog(this);
        awesomeSuccessDialog.setTitle("Business Uploaded Successfully")
                .setMessage("")
                .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_done_black_24dp, R.color.white)
                .setCancelable(false)
                .setPositiveButtonText("OK")
                .setPositiveButtonbackgroundColor(R.color.colorPrimary)
                .setPositiveButtonTextColor(R.color.white)
                .setPositiveButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        awesomeSuccessDialog.hide();
                    }
                })
                .show();
        updateBusiness();
        deleteBusinessFiles();
    }

    private void updateBusiness() {
        Calendar c = Calendar.getInstance();
        databaseHelper.updateIntValue(DatabaseHelper.TABLE_COMPANY_PROFILE, DatabaseHelper.COLUMN_STATUS, 1);
        databaseHelper.updateStringValue(DatabaseHelper.TABLE_COMPANY_PROFILE, DatabaseHelper.COLUMN_DATE_OF_UPLOADING, new SimpleDateFormat("dd-MMM-yyyy").format(c.getTime()));
        databaseHelper.updateStringValue(DatabaseHelper.TABLE_COMPANY_PROFILE, DatabaseHelper.COLUMN_TIME_OF_UPLOADING, new SimpleDateFormat("hh:mm:ss a").format(c.getTime()));
    }

    private void deleteBusinessFiles() {
        File logFile = new File(getApplicationContext().getFilesDir(), helper.checkForInput(companyName) + ".txt");
        File xmlFile = new File(getApplicationContext().getFilesDir(), helper.checkForInput(companyName) + ".xml");
        try {
            logFile.delete();
            xmlFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private class FileData {
        String ColumnName;
        String TableName;
        int Row;
        String Name;

        public FileData(String columnName, String tableName, int row, String name) {
            ColumnName = columnName;
            TableName = tableName;
            Row = row;
            Name = name;
        }

    }

    private void sendXMLForUpload(String aBuffer) {
        logUpload = false;
        try {
            awesomeDialog.setCancelable(false);
            Map<String, String> params = new HashMap<>();
            params.put("xml", aBuffer);
            params.put("businessName", helper.checkForInput(companyName));
            volleyHelper.makeStringRequest(helper.getBaseURL() + "addxml.php", "tag", params);
            awesomeDialog.setMessage("\nTotal Images : " + this.imageData.size() + "\nTotal Files : " + this.fileData.size() + "\n\nUploading XML File");
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
            new AwesomeErrorDialog(this)
                    .setTitle("Error")
                    .setMessage("Error in uploading xml!")
                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(false).setButtonText(getString(R.string.dialog_ok_button))
                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                    .setButtonText(getString(R.string.dialog_ok_button))
                    .setErrorButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            finish();
                        }
                    })
                    .show();
        }
    }

    private void sendImageForUpload(ImageData imageData) {
        try {
            String image, imageName;
            Map<String, String> imageParams = new HashMap<>();
            if (imageData.Row == -1)
                image = Base64.encodeToString(databaseHelper.getBlobValue(imageData.ColumnName, imageData.TableName), Base64.DEFAULT);
            else
                image = Base64.encodeToString(databaseHelper.getBlobFromRow(imageData.ColumnName, imageData.TableName, imageData.Row), Base64.DEFAULT);
            imageName = imageData.Name;
            imageParams.put("imagename", imageName);
            imageParams.put("image", image);
            imageParams.put("businessName", companyName);
            imageParams.put("number", currentImage + "");

            volleyHelper.makeStringRequest(helper.getBaseURL() + "createimage.php", imageName, imageParams);
            Log.v(TAG, "Sending Image " + currentImage);
            awesomeDialog.setMessage("\nTotal Images : " + this.imageData.size() + "\nTotal Files : " + this.fileData.size() + "\n\nUploading Image " + (currentImage + 1) + " of " + this.imageData.size());
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
            new AwesomeErrorDialog(this)
                    .setTitle("Error")
                    .setMessage("Error in uploading images!")
                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(false).setButtonText(getString(R.string.dialog_ok_button))
                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                    .setButtonText(getString(R.string.dialog_ok_button))
                    .setErrorButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            finish();
                        }
                    })
                    .show();
        }
    }

    private void sendFileForUpload(FileData fileData) {
        try {
            String file, fileName;
            Map<String, String> fileParams = new HashMap<>();
            if (fileData.Row == -1) {
                file = Base64.encodeToString(
                        helper.getByteArrayFromFile(databaseHelper.getStringValue(fileData.ColumnName, fileData.TableName)),
                        Base64.DEFAULT);
            } else
                file = Base64.encodeToString(
                        helper.getByteArrayFromFile(databaseHelper.getStringFromRow(fileData.TableName, fileData.ColumnName, fileData.Row)),
                        Base64.DEFAULT);

            Log.v(TAG, "PDF Byte Array\n" + file);
            fileName = fileData.Name;
            fileParams.put("filename", fileName);
            fileParams.put("file", file);
            fileParams.put("businessName", companyName);
            fileParams.put("number", currentFile + "");
            volleyHelper.makeStringRequest(helper.getBaseURL() + "createfile.php", fileName, fileParams);
            Log.v(TAG, "Sending file " + currentFile);
            awesomeDialog.setMessage("\nTotal Images : " + this.imageData.size() + "\nTotal Files : " + this.fileData.size() + "\n\nUploading File " + (currentFile + 1) + " of " + this.fileData.size());
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
            new AwesomeErrorDialog(this)
                    .setTitle("Error")
                    .setMessage("Error in uploading file!")
                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(false).setButtonText(getString(R.string.dialog_ok_button))
                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                    .setButtonText(getString(R.string.dialog_ok_button))
                    .setErrorButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            finish();
                        }
                    })
                    .show();
        }
    }

}
