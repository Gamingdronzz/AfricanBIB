package biz.africanbib.Tabs;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import biz.africanbib.Activity.MainActivity;
import biz.africanbib.Adapters.ComplexRecyclerViewAdapter;
import biz.africanbib.Models.Add;
import biz.africanbib.Models.Divider;
import biz.africanbib.Models.DropDown;
import biz.africanbib.Models.Heading;
import biz.africanbib.Models.SimpleEditText;
import biz.africanbib.Models.SimpleText;
import biz.africanbib.R;
import biz.africanbib.Tools.DatabaseHelper;
import biz.africanbib.Tools.Helper;

//Our class extending fragment
public class Tab2 extends Fragment {

    RecyclerView recyclerView;
    ComplexRecyclerViewAdapter adapter;
    Helper helper;
    boolean isTab;
    DatabaseHelper databaseHelper;
    int ownerRows;
    int refrencesRows;
    int managerRows;
    int subsidiaryRows;
    private final String TAG = "Tab2";
    private Fragment fragment;
    ProgressDialog progressDialog;

    ArrayList<Object> items = new ArrayList<>();

    public ArrayList<Object> getList() {
        return items;
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_2, container, false);
        Log.d("Company", "Trying to initialize");
        helper = new Helper(getContext());
        isTab = helper.isTab();
        fragment = this;
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        databaseHelper = new DatabaseHelper(view.getContext(), DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        init(view);

        return view;
    }

    public ComplexRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_2);
        getSampleArrayList();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        // Check for the rotation
        /*if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this.getContext(), "LANDSCAPE", Toast.LENGTH_SHORT).show();
            setupGridLayout(true);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this.getContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
            if (isTab) {
                setupGridLayout(true);
            } else {
                setupGridLayout(false);
            }


        }*/
    }

    private void setupGridLayout(boolean multiColumn) {
        if (multiColumn) {
            GridLayoutManager manager = new GridLayoutManager(this.getContext(), 2);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    Object ob = adapter.getItem(position);
                    if (ob instanceof Heading || ob instanceof Add || ob instanceof SimpleText || ob instanceof Divider)
                        return 2;
                    else
                        return 1;
                }
            });
            recyclerView.setLayoutManager(manager);
        } else {
            GridLayoutManager manager = new GridLayoutManager(this.getContext(), 1);
            recyclerView.setLayoutManager(manager);
        }

    }

    private void getSampleArrayList() {
        LoadTab loadTab = new LoadTab();
        loadTab.execute();
    }

    private void getValuesFromViews() {
        Object[] items;
        items = new Object[adapter.getItemCount()];
        for (int i = 0; i < items.length; i++) {
            //Log.d("Company","I = " + i);
            items[i] = adapter.getItem(i);
            if (items[i] instanceof SimpleEditText) {
                SimpleEditText ob = (SimpleEditText) items[i];
                Log.d("Company", ob.getTitle() + " = " + ob.getValue());
            } else if (items[i] instanceof DropDown) {
                DropDown ob = (DropDown) items[i];
                Log.d("Company", ob.getHeading() + " = " + ob.getSelectedPosition());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d("Tab2", "Request Code  " + requestCode);
            adapter.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    adapter.onRequestPermissionsResult(requestCode, permissions, grantResults);
                } else {
                    Toast.makeText(getActivity(), "Please give your permission.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    private class LoadTab extends AsyncTask<Void, Void, ArrayList<Object>> {

        @Override
        protected ArrayList<Object> doInBackground(Void... voids) {
            items = new ArrayList<>();


            //items.add(new SimpleEditText(""));


        /*
        items.add(new Heading("INVESTMENT OPPURTUNTIES", "investmentOpportunities"));
        items.add(new SimpleText("Needs"));
        Log.v("Tab2", "Business type = " + MainActivity.typeOfBusiness);

        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {

                    items.add(helper.buildEditText(
                            "Need",
                            databaseHelper.getStringFromRow(tableName, columnName, ids[i]),
                            tableName,
                            columnName,
                            ids[i], "need"));
                    items.add(new Divider());
                }
                needRows = ids.length;
            }


        }
        items.add(helper.buildAdd(1, new String[]{"Need"}, tableName, new String[]{columnName}, new String[]{"need"}));

        items.add(new SimpleText("Offers"));
        columnName = DatabaseHelper.COLUMN_OFFER;
        tableName = DatabaseHelper.TABLE_OFFERS;
        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    items.add(
                            helper.buildEditText(
                                    "Offer",
                                    databaseHelper.getStringFromRow(tableName, columnName, ids[i]),
                                    tableName,
                                    columnName,
                                    ids[i], "offer"));
                    items.add(new Divider());
                }
                offerrows = ids.length;
            }


        }
        items.add(helper.buildAdd(1, new String[]{"Offer"}, DatabaseHelper.TABLE_OFFERS, new String[]{DatabaseHelper.COLUMN_OFFER}, new String[]{"offer"}));

*/
            String columnName = DatabaseHelper.COLUMN_FIRST_NAME;
            String tableName = DatabaseHelper.TABLE_CONTACT_PERSON;
            String value;
            items.add(new Heading("CONTACT PERSON", "contact"));
            columnName = DatabaseHelper.COLUMN_TELEPHONE;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("Telephone", value, tableName, columnName, -1, "telephone"));
            columnName = DatabaseHelper.COLUMN_CELLPHONE;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("Cellphone", value, tableName, columnName, -1, "cellphone"));
            columnName = DatabaseHelper.COLUMN_FAX;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("Fax", value, tableName, columnName, -1, "fax"));
            columnName = DatabaseHelper.COLUMN_EMAIL;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("Email", value, tableName, columnName, -1, "email"));
            columnName = DatabaseHelper.COLUMN_STREET;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("Street & Number", value, tableName, columnName, -1, "street"));
            columnName = DatabaseHelper.COLUMN_CITY;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("City / Town", value, tableName, columnName, -1, "city"));
            columnName = DatabaseHelper.COLUMN_PO_BOX;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("Post Office Box", value, tableName, columnName, -1, "postbox"));
            columnName = DatabaseHelper.COLUMN_POSTAL_CODE;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("Postal Code", value, tableName, columnName, -1, "postalCode"));
            columnName = DatabaseHelper.COLUMN_DISTRICT;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("District / State", value, tableName, columnName, -1, "district"));
            columnName = DatabaseHelper.COLUMN_COUNTRY;
            items.add(helper.buildDropDown("Country", helper.getCountryNames(), helper.getCountryCodes(), 0, tableName, columnName, -1, "country"));
            columnName = DatabaseHelper.COLUMN_PREFIX;
            int selectedPosition = databaseHelper.getIntValue(columnName, tableName);
            items.add(
                    helper.buildDropDown(
                            "Prefix",
                            new String[]{"Mr.",
                                    "Mrs.",
                                    "Ms.",
                                    "Dr",
                                    "Prof."},
                            new int[]{0, 1, 2, 3, 4},
                            selectedPosition,
                            tableName,
                            columnName,
                            -1, "prefix"));
            columnName = DatabaseHelper.COLUMN_FIRST_NAME;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("First name", value, tableName, columnName, -1, "firstname"));
            columnName = DatabaseHelper.COLUMN_LAST_NAME;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("Last name", value, tableName, columnName, -1, "lastname"));
            columnName = DatabaseHelper.COLUMN_POSITION_IN_COMPANY;
            selectedPosition = databaseHelper.getIntValue(columnName, tableName);
            items.add(
                    helper.buildDropDown(
                            "Position in company/institution",
                            new String[]{"Student/Intern",
                                    "Entry Level",
                                    "Professional / Experienced",
                                    "Manager (Manager / Supervisor)",
                                    "Executive (VP, SVP etc)",
                                    "Senior Executive (CEO,CFO)"},
                            new int[]{0},
                            selectedPosition,
                            tableName,
                            columnName,
                            -1, "position"));


            items.add(new Heading("OWNERS/MANAGERS/REFERENCES/SUBSIDIARIES", null));
            items.add(new SimpleText("REFERENCES", "contact"));
            tableName = DatabaseHelper.TABLE_REFERENCES;
            String[] xmltags = new String[]{
                    //"institutionType",
                    "image",
                    "website",
                    "email",
                    "cellphone",
                    "telephone",
                    "name"
            };
            String[] titles = new String[]{
                    //"Type of Institution",
                    "Institution Logo",
                    "Website",
                    "Email",
                    "Cellphone",
                    "Telephone",
                    "Institution Name"};
            String[] columnNames = new String[]{
                    //DatabaseHelper.COLUMN_INSTITUTION_TYPE,
                    DatabaseHelper.COLUMN_LOGO,
                    DatabaseHelper.COLUMN_WEBSITE,
                    DatabaseHelper.COLUMN_EMAIL,
                    DatabaseHelper.COLUMN_CELLPHONE,
                    DatabaseHelper.COLUMN_TELEPHONE,
                    DatabaseHelper.COLUMN_INSTITUTION_NAME};
            if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
                int[] ids = databaseHelper.getrowids(tableName);
                if (ids != null) {
                    Log.v("Reference", "ID Length = " + ids.length);
                    for (int i = 0; i < ids.length; i++) {
                        for (int j = titles.length - 1; j >= 0; j--) {
                            Log.v("Reference", "I = " + i + "\nJ = " + j);
                            if (columnNames[j].equals(DatabaseHelper.COLUMN_INSTITUTION_TYPE)) {
                                selectedPosition = databaseHelper.getIntFromRow(tableName, columnNames[j], ids[i]);
                                items.add(helper.buildDropDown(titles[j], new String[]{"Local Association",
                                                "International Association",
                                                "Partner",
                                                "Clients"}, new int[]{0, 1, 2, 3}, selectedPosition, tableName, columnNames[j],
                                        ids[i], xmltags[j]));
                            } else if (columnNames[j].equals(DatabaseHelper.COLUMN_LOGO)) {
                                Bitmap logo = null;
                                try {
                                    logo = helper.createBitmapFromByteArray(databaseHelper.getBlobFromRow(columnNames[j], tableName, ids[i]));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                items.add(helper.buildImage(titles[j], ids[i], logo, tableName, columnNames[j], xmltags[j]));
                            } else {
                                items.add(
                                        helper.buildEditText(
                                                titles[j],
                                                databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                                tableName,
                                                columnNames[j],
                                                ids[i], xmltags[j]));
                            }
                        }
                        items.add(new Divider());
                    }
                    refrencesRows = ids.length;
                }
            }
            items.add(helper.buildAdd(6, titles, tableName, columnNames, xmltags));


            items.add(new SimpleText("OWNERS", "contact"));
            tableName = DatabaseHelper.TABLE_OWNERS;
            xmltags = new String[]{
                    "image",
                    "affiliation",
                    "professional",
                    "academic",
                    "nationality",
                    "birthday",
                    "firstname",
                    "lastname",
                    "prefix",
                    "email",
                    "telephone"
            };
            titles = new String[]{
                    "Owners Image",
                    "Affiliation",
                    "Professional",
                    "Academic",
                    "Nationality",
                    "Birthday",
                    "First Name",
                    "Last Name",
                    "Prefix",
                    "E-Mail",
                    "Telephone"
            };
            columnNames = new String[]{
                    DatabaseHelper.COLUMN_LOGO,
                    DatabaseHelper.COLUMN_AFFILIATION,
                    DatabaseHelper.COLUMN_PROFESSIONAL,
                    DatabaseHelper.COLUMN_ACADEMIC,
                    DatabaseHelper.COLUMN_NATIONALITY,
                    DatabaseHelper.COLUMN_BIRTHDAY,
                    DatabaseHelper.COLUMN_FIRST_NAME,
                    DatabaseHelper.COLUMN_LAST_NAME,
                    DatabaseHelper.COLUMN_PREFIX,
                    DatabaseHelper.COLUMN_EMAIL,
                    DatabaseHelper.COLUMN_TELEPHONE
            };
            if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
                int[] ids = databaseHelper.getrowids(tableName);
                if (ids != null) {

                    for (int i = 0; i < ids.length; i++) {

                        for (int j = titles.length - 1; j >= 0; j--) {
                            if (columnNames[j].equals(DatabaseHelper.COLUMN_LOGO)) {
                                Bitmap ownerLogo = null;
                                try {
                                    ownerLogo = helper.createBitmapFromByteArray(databaseHelper.getBlobFromRow(columnNames[j], tableName, ids[i]));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                items.add(helper.buildImage(titles[j], ids[i], ownerLogo, tableName, columnNames[j], xmltags[j]));
                            } else if (columnNames[j].equals(DatabaseHelper.COLUMN_PREFIX)) {
                                selectedPosition = databaseHelper.getIntFromRow(tableName, columnNames[j], ids[i]);
                                items.add(helper.buildDropDown(titles[j],
                                        helper.getPrefixList(),
                                        new int[]{0, 1, 2, 3, 4}, selectedPosition, tableName, columnNames[j], ids[i], xmltags[j]));
                            } else if (columnNames[j].equals(DatabaseHelper.COLUMN_BIRTHDAY)) {
                                items.add(helper.buildDate(titles[j], databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                        tableName, columnNames[j], ids[i], xmltags[j]));
                            } else {
                                items.add(
                                        helper.buildEditText(
                                                titles[j],
                                                databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                                tableName,
                                                columnNames[j],
                                                ids[i], xmltags[j]));
                            }
                        }
                        items.add(new Divider());
                    }
                    ownerRows = ids.length;
                }


            }
            items.add(helper.buildAdd(11, titles,
                    tableName,
                    columnNames, xmltags));


            items.add(new SimpleText("MANAGERS", "contact"));
            tableName = DatabaseHelper.TABLE_MANAGERS;
            xmltags = new String[]{
                    "image",
                    "affiliation",
                    "professional",
                    "academic",
                    "nationality",
                    "birthday",
                    "firstname",
                    "lastname",
                    "prefix",
                    "email",
                    "telephone",
            };
            titles = new String[]{
                    "Manager Image",
                    "Affiliation",
                    "Professional",
                    "Academic",
                    "Nationality",
                    "Birthday",
                    "First Name",
                    "Last Name",
                    "Prefix",
                    "E-Mail",
                    "Telephone"
            };
            columnNames = new String[]{
                    DatabaseHelper.COLUMN_LOGO,
                    DatabaseHelper.COLUMN_AFFILIATION,
                    DatabaseHelper.COLUMN_PROFESSIONAL,
                    DatabaseHelper.COLUMN_ACADEMIC,
                    DatabaseHelper.COLUMN_NATIONALITY,
                    DatabaseHelper.COLUMN_BIRTHDAY,
                    DatabaseHelper.COLUMN_FIRST_NAME,
                    DatabaseHelper.COLUMN_LAST_NAME,
                    DatabaseHelper.COLUMN_PREFIX,
                    DatabaseHelper.COLUMN_EMAIL,
                    DatabaseHelper.COLUMN_TELEPHONE
            };
            if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
                int[] ids = databaseHelper.getrowids(tableName);
                if (ids != null) {

                    for (int i = 0; i < ids.length; i++) {

                        for (int j = titles.length - 1; j >= 0; j--) {
                            if (columnNames[j].equals(DatabaseHelper.COLUMN_LOGO)) {
                                Bitmap managerLogo = null;
                                try {
                                    managerLogo = helper.createBitmapFromByteArray(databaseHelper.getBlobFromRow(columnNames[j], tableName, ids[i]));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                items.add(helper.buildImage(titles[j], ids[i], managerLogo, tableName, columnNames[j], xmltags[j]));
                            } else if (columnNames[j].equals(DatabaseHelper.COLUMN_PREFIX)) {
                                selectedPosition = databaseHelper.getIntFromRow(tableName, columnNames[j], ids[i]);
                                items.add(helper.buildDropDown(titles[j],
                                        helper.getPrefixList(),
                                        helper.getPrefixCodes(), selectedPosition, tableName, columnNames[j], ids[i], xmltags[j]));
                            } else if (columnNames[j].equals(DatabaseHelper.COLUMN_BIRTHDAY)) {
                                items.add(helper.buildDate(titles[j], databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                        tableName, columnNames[j], ids[i], xmltags[j]));
                            } else {
                                items.add(
                                        helper.buildEditText(
                                                titles[j],
                                                databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                                tableName,
                                                columnNames[j],
                                                ids[i], xmltags[j]));
                            }
                        }
                        items.add(new Divider());
                    }
                    managerRows = ids.length;
                }


            }
            items.add(helper.buildAdd(11, titles,
                    tableName,
                    columnNames, xmltags));

            items.add(new SimpleText("SUBSIDIARIES", "contact"));
            tableName = DatabaseHelper.TABLE_SUBSIDIARIES;
            xmltags = new String[]{
                    "website",
                    "email",
                    "telephone",
                    "country",
                    "district",
                    "postalCode",
                    "city",
                    "street",
                    "name"
            };
            titles = new String[]{
                    "Website",
                    "Email",
                    "Telephone/Cellphone",
                    "Country",
                    "District/State",
                    "Zip",
                    "City/Town",
                    "Street & Number",
                    "Subsidiary Name"};
            columnNames = new String[]{
                    DatabaseHelper.COLUMN_WEBSITE,
                    DatabaseHelper.COLUMN_EMAIL,
                    DatabaseHelper.COLUMN_TELEPHONE,
                    DatabaseHelper.COLUMN_COUNTRY,
                    DatabaseHelper.COLUMN_DISTRICT,
                    DatabaseHelper.COLUMN_POSTAL_CODE,
                    DatabaseHelper.COLUMN_CITY,
                    DatabaseHelper.COLUMN_STREET,
                    DatabaseHelper.COLUMN_SUBSIDIARY_NAME};
            if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
                int[] ids = databaseHelper.getrowids(tableName);
                if (ids != null) {
                    Log.v("Subsidiary", "ID Length = " + ids.length);
                    for (int i = 0; i < ids.length; i++) {
                        for (int j = titles.length - 1; j >= 0; j--) {
                            Log.v("subsidiary", "I = " + i + "\nJ = " + j);
                            if (columnNames[j].equals(DatabaseHelper.COLUMN_COUNTRY)) {
                                selectedPosition = databaseHelper.getIntFromRow(tableName, columnNames[j], ids[i]);
                                items.add(helper.buildDropDown(titles[j], helper.getCountryNames(), helper.getCountryCodes(), selectedPosition, tableName, columnNames[j],
                                        ids[i], xmltags[j]));
                            } else {
                                items.add(
                                        helper.buildEditText(
                                                titles[j],
                                                databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                                tableName,
                                                columnNames[j],
                                                ids[i], xmltags[j]));
                            }
                        }
                        items.add(new Divider());
                    }
                    subsidiaryRows = ids.length;
                }
            }
            items.add(helper.buildAdd(9, titles, tableName, columnNames, xmltags));


       /* items.add(new Heading("SUBSIDIARIES", "contact"));
        tableName = DatabaseHelper.TABLE_SUBSIDIARIES;
        columnName = DatabaseHelper.COLUMN_SUBSIDIARY_NAME;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Subsidiary Name", value, tableName, columnName, -1, "subsidiaryname"));
        columnName = DatabaseHelper.COLUMN_STREET;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Street & Number", value, tableName, columnName, -1, "streetnumber"));
        columnName = DatabaseHelper.COLUMN_CITY;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("City / Town", value, tableName, columnName, -1, "city"));
        columnName = DatabaseHelper.COLUMN_POSTAL_CODE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Zip", value, tableName, columnName, -1, "postalcode"));
        columnName = DatabaseHelper.COLUMN_DISTRICT;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("District / State", value, tableName, columnName, -1, "district"));
        columnName = DatabaseHelper.COLUMN_COUNTRY;
        items.add(helper.buildDropDown("Country", helper.getCountryNames(), helper.getCountryCodes(), 0, tableName, columnName, -1, "country"));
        columnName = DatabaseHelper.COLUMN_TELEPHONE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Telephone/Cellphone", value, tableName, columnName, -1, "telephone"));
        columnName = DatabaseHelper.COLUMN_EMAIL;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("E-Mail", value, tableName, columnName, -1, "email"));
        columnName = DatabaseHelper.COLUMN_WEBSITE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Website", value, tableName, columnName, -1, "website"));
*/
            Log.v(TAG, "Tab2 Initialize Complete");
            return items;

        }

        @Override
        protected void onPostExecute(ArrayList<Object> objects) {
            super.onPostExecute(objects);
            adapter = new ComplexRecyclerViewAdapter(objects, getFragmentManager(), fragment);

            if (isTab) {
                setupGridLayout(true);
            } else {
                setupGridLayout(false);
            }
            //adapter.updateRow(DatabaseHelper.TABLE_NEEDS, needRows);
            //adapter.updateRow(DatabaseHelper.TABLE_OFFERS, offerrows);
            adapter.updateRow(DatabaseHelper.TABLE_OWNERS, ownerRows);
            adapter.updateRow(DatabaseHelper.TABLE_REFERENCES, refrencesRows);
            adapter.updateRow(DatabaseHelper.TABLE_MANAGERS, managerRows);
            adapter.updateRow(DatabaseHelper.TABLE_SUBSIDIARIES, subsidiaryRows);
            //SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
            //snapHelper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(adapter);
            Log.d("Company", "Adapter set");
            progressDialog.dismiss();
        }
    }
}
/*
        columnName = DatabaseHelper.COLUMN_NATIONALITY;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Nationality", value, tableName, columnName, -1, "nationality"));
        columnName = DatabaseHelper.COLUMN_BIRTHDAY;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildDate("Birthday", helper.toDays(value), tableName, columnName, -1, "birthday"));
        columnName = DatabaseHelper.COLUMN_PHOTO;
        selectedPosition = databaseHelper.getIntValue(columnName, tableName);
        items.add(
                helper.buildDropDown(
                        "Photo",
                        new String[]{"Collected",
                                "Not Collected"},
                        selectedPosition,
                        tableName,
                        columnName,
                        -1, "image"));

        columnName = DatabaseHelper.COLUMN_VIDEO;
        selectedPosition = databaseHelper.getIntValue(columnName, tableName);
        items.add(
                helper.buildDropDown(
                        "Video",
                        new String[]{"Collected",
                                "Not Collected"},
                        selectedPosition,
                        tableName,
                        columnName,
                        -1, "video"));
        columnName = DatabaseHelper.COLUMN_PHOTO_NOTE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Photo Note", value, tableName, columnName, -1, "photonote"));

        columnName = DatabaseHelper.COLUMN_VIDEO_NOTE;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Video Note", value, tableName, columnName, -1, "videonote"));

        items.add(new SimpleText("Academic Background", "academic"));

        tableName = DatabaseHelper.TABLE_ACADEMIC_BACKGROUND;
        String[] xmltags = new String[]{
                "country",
                "date",
                "subjectfocus",
                "nameofinstitution"
        };
        String[] titles = new String[]{
                "Country",
                "Date",
                "Subject Focus",
                "Name of Institution"};
        String[] columnNames = new String[]{
                DatabaseHelper.COLUMN_COUNTRY,
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_SUBJECT_FOCUS,
                DatabaseHelper.COLUMN_NAME_OF_INSTITUTION};
        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {

                    for (int j = titles.length - 1; j > 0; j--) {

                        if (columnNames[j].equals(DatabaseHelper.COLUMN_DATE)) {
                            items.add(helper.buildDate(
                                    titles[j],
                                    helper.toDays(databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i])),
                                    tableName,
                                    columnNames[j],
                                    ids[i], xmltags[j]));
                        } else {
                            items.add(
                                    helper.buildEditText(
                                            titles[j],
                                            databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                            tableName,
                                            columnNames[j],
                                            ids[i], xmltags[j]));
                        }

                    }
                    items.add(helper.buildDropDown(
                            titles[0],
                            helper.getCountryNames(),
                            databaseHelper.getIntFromRow(tableName, DatabaseHelper.COLUMN_COUNTRY, ids[i]),
                            tableName,
                            columnNames[0],
                            ids[i], xmltags[0]));
                    items.add(new Divider());
                }
                academicBackgroundRows = ids.length;
            }


        }
        items.add(helper.buildAdd(4, titles,
                tableName,
                columnNames, xmltags));

        items.add(new SimpleText("Professional Background", "professional"));
        tableName = DatabaseHelper.TABLE_PROFESSIONAL_BACKGROUND;
        xmltags = new String[]{
                "country",
                "date",
                "jobdescription",
                "nameofemployer",
                "jobtitle"};
        titles = new String[]{
                "Country",
                "Date",
                "Job Description",
                "Name of Employer",
                "Job Title"};
        columnNames = new String[]{
                DatabaseHelper.COLUMN_COUNTRY,
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_JOB_DESCRIPTION,
                DatabaseHelper.COLUMN_NAME_OF_EMPLOYER,
                DatabaseHelper.COLUMN_JOB_TITLE};
        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);
            if (ids != null) {

                for (int i = 0; i < ids.length; i++) {

                    for (int j = titles.length - 1; j > 0; j--) {
                        if (columnNames[j].equals(DatabaseHelper.COLUMN_DATE)) {
                            items.add(helper.buildDate(
                                    titles[j],
                                    helper.toDays(databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i])),
                                    tableName,
                                    columnNames[j],
                                    ids[i], xmltags[j]));
                        } else {
                            items.add(
                                    helper.buildEditText(
                                            titles[j],
                                            databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                            tableName,
                                            columnNames[j],
                                            ids[i], xmltags[j]));
                        }
                    }
                    items.add(helper.buildDropDown(
                            titles[0],
                            helper.getCountryNames(),
                            databaseHelper.getIntFromRow(tableName, DatabaseHelper.COLUMN_COUNTRY, ids[i]),
                            tableName,
                            columnNames[0],
                            ids[i], xmltags[0]));
                    items.add(new Divider());

                }
                professionalBackgroundRows = ids.length;
            }


        }
        items.add(helper.buildAdd(5, titles,
                tableName,
                columnNames, xmltags));
        items.add(new SimpleText("Affiliation"));
        */
/*tableName = DatabaseHelper.TABLE_AFFILIATION;
        xmltags = new String[]{
                "country",
                "sector",
                "nameofassociation"
        };
        titles = new String[]{
                "Country",
                "Sector",
                "Name of Association"};
        columnNames = new String[]{
                DatabaseHelper.COLUMN_COUNTRY,
                DatabaseHelper.COLUMN_SECTOR,
                DatabaseHelper.COLUMN_NAME_OF_ASSOCIATION};
        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);
            if (ids != null) {

                for (int i = 0; i < ids.length; i++) {

                    for (int j = titles.length - 1; j > 0; j--) {
                        items.add(
                                helper.buildEditText(
                                        titles[j],
                                        databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                        tableName,
                                        columnNames[j],
                                        ids[i], xmltags[j]));
                    }
                    items.add(helper.buildDropDown(
                            titles[0],
                            helper.getCountryNames(),
                            databaseHelper.getIntFromRow(tableName, DatabaseHelper.COLUMN_COUNTRY, ids[i]),
                            tableName,
                            columnNames[0],
                            ids[i], xmltags[0]));
                    items.add(new Divider());
                }
                affiliationRows = ids.length;
            }


        }
        items.add(helper.buildAdd(3, titles,
                tableName,
                columnNames, xmltags));

*//*

        items.add(new Heading("REFERENCE SPECIFIC INFORMATION", null));

        tableName = DatabaseHelper.TABLE_REFERENCE_SPECIFIC_INFORMATION;
        columnName = DatabaseHelper.COLUMN_INSTITUTION_NAME;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Institution name", value, tableName, columnName, -1, "institutionname"));

        columnName = DatabaseHelper.COLUMN_ORGANISATION_TYPE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Organisation type", value, tableName, columnName, -1, "organisationtype"));
        columnName = DatabaseHelper.COLUMN_LOGO;
        selectedPosition = databaseHelper.getIntValue(columnName, tableName);
        items.add(
                helper.buildDropDown(
                        "Organisation Logo",
                        new String[]{"Collected",
                                "Not Collected"},
                        selectedPosition,
                        tableName,
                        columnName,
                        -1, "organisationlogo"));
        columnName = DatabaseHelper.COLUMN_LOGO_NOTE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Logo Note", value, tableName, columnName, -1, "logonote"));
*/


