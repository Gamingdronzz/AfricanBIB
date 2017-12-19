package biz.africanbib.Tabs;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
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
public class Tab3 extends Fragment {

    RecyclerView recyclerView;
    ComplexRecyclerViewAdapter adapter;
    Helper helper;
    boolean isTab;
    DatabaseHelper databaseHelper;

    private int productDetailsRows = 0;
    private int mediaRows = 0;
    private int newsRows = 0;
    private int serviceRows = 0;
    ArrayList<Object> items;

    public ComplexRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_3, container, false);
        Log.d("Company", "Trying to initialize");
        helper = new Helper(getContext());
        isTab = helper.isTab();
        databaseHelper = new DatabaseHelper(view.getContext(), DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        init(view);

        return view;
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_3);
        adapter = new ComplexRecyclerViewAdapter(getSampleArrayList(), getFragmentManager(), this);
        if (isTab) {
            setupGridLayout(true);
        } else {
            setupGridLayout(false);
        }
        //SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        //snapHelper.attachToRecyclerView(recyclerView);
        adapter.updateRow(DatabaseHelper.TABLE_OTHER_MEDIA, mediaRows);
        adapter.updateRow(DatabaseHelper.TABLE_LATEST_NEWS, newsRows);
        adapter.updateRow(DatabaseHelper.TABLE_PRODUCTS_AND_PRODUCT_DETAILS, productDetailsRows);
        adapter.updateRow(DatabaseHelper.TABLE_SERVICES, serviceRows);
        recyclerView.setAdapter(adapter);
        Log.d("Company", "Adapter set");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
       /* // Check for the rotation
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
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

    public ArrayList<Object> getList() {
        return items;
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

    private ArrayList<Object> getSampleArrayList() {
        items = new ArrayList<>();
        items.add(new Heading("BUSINESS SPECIFIC INFORMATION", null));


        /*
        String tableName = DatabaseHelper.TABLE_PRODUCTS_AND_PRODUCT_DETAILS;
        String columnName = DatabaseHelper.COLUMN_PRODUCTS;
        items.add(new SimpleText("Products & Product Details"));
        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    items.add(new Divider());
                    items.add(
                            helper.buildEditText(
                                    "Product",
                                    databaseHelper.getStringFromRow(tableName, columnName, ids[i]),
                                    tableName,
                                    columnName,
                                    ids[i]));
                }
                productRows = ids.length;
            }
        }
        items.add(helper.buildAdd(1, new String[]{
                "Product"}, tableName, new String[]{DatabaseHelper.COLUMN_PRODUCTS}));

*/

        items.add(new SimpleText("Products & Product Details", "productDetails"));
        String tableName = DatabaseHelper.TABLE_PRODUCTS_AND_PRODUCT_DETAILS;
        Bitmap image = null;
        String[] xmlTags = new String[]{
                "productMedia",
                "description",
                "title"};
        String[] titles = new String[]{
                "Product Media (Photo / Documents)",
                "Description",
                "Title"};
        String[] columnNames = new String[]{
                DatabaseHelper.COLUMN_MEDIA,
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_TITLE
        };
        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);
            if (ids != null) {

                for (int i = 0; i < ids.length; i++) {
                    for (int j = titles.length - 1; j >= 0; j--) {
                        if (columnNames[j].equals(DatabaseHelper.COLUMN_MEDIA)) {
                            items.add(helper.buildImage(
                                    titles[j], ids[i],
                                    helper.createBitmapFromByteArray(databaseHelper.getBlobFromRow(columnNames[j], tableName, ids[i])),
                                    tableName,
                                    columnNames[j],
                                    xmlTags[j]));
                        } else {
                            items.add(helper.buildEditText(
                                    titles[j],
                                    databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                    tableName,
                                    columnNames[j],
                                    ids[i], xmlTags[j]));
                        }
                    }
                    items.add(new Divider());
                }
                productDetailsRows = ids.length;
            }
        }
        items.add(helper.buildAdd(3, titles, tableName, columnNames, xmlTags));

        tableName = DatabaseHelper.TABLE_OTHER_PRODUCT_DATA;
        String columnName = DatabaseHelper.COLUMN_PRODUCTS;
        String value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Product Description", value, tableName, columnName, -1, "products"));

        columnName = DatabaseHelper.COLUMN_SERVICES;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Service Description", value, tableName, columnName, -1, "services"));

        items.add(new SimpleText("Media", "media"));
        tableName = DatabaseHelper.TABLE_OTHER_MEDIA;
        columnNames = new String[]{DatabaseHelper.COLUMN_SELECTED_FILE,
                //DatabaseHelper.COLUMN_FORMAT,
                DatabaseHelper.COLUMN_FILE_TYPE};
        titles = new String[]{getResources().getString(R.string.choose_file),
                //"Select Format",
                "File Type"};
        xmlTags = new String[]{"",
                "type"};
        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);
            if (ids != null) {

                for (int i = 0; i < ids.length; i++) {
                    for (int j = titles.length - 1; j >= 0; j--) {
                        if (columnNames[j].equals(DatabaseHelper.COLUMN_FILE_TYPE))
                            items.add(helper.buildDropDown(titles[j],
                                    new String[]{"Photo",
                                            "Video",
                                            "Business Report",
                                            "Flyer",
                                            "Directions",
                                            "File",
                                            "productphoto",
                                            "productvideo",
                                            "productfile"
                                    },
                                    new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9},
                                    databaseHelper.getIntFromRow(tableName, columnNames[j], ids[i]),
                                    tableName, columnNames[j], ids[i], xmlTags[j]));
                        /*else if (columnNames[j].equals(DatabaseHelper.COLUMN_FORMAT))
                            items.add(helper.buildDropDown(titles[j],
                                    new String[]{"Photo", "PDF"},
                                    new int[]{1, 2},
                                    databaseHelper.getIntFromRow(tableName, columnNames[j], ids[i]),
                                    tableName, columnNames[j], ids[i], xmlTags[j]));
                    */    else
                            items.add(helper.buildChooseFile(databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                    ids[i], tableName, columnNames[j], xmlTags[j]));
                    }

                    items.add(new Divider());
                }
                mediaRows = ids.length;
            }
        }
        items.add(helper.buildAdd(2, titles, tableName, columnNames, xmlTags));

       /* items.add(new SimpleText("Services & Service Details"));
        tableName = DatabaseHelper.TABLE_SERVICES;
        xmlTags = new String[]{
                "media",
                "description",
                "title",
                "service"};
        titles = new String[]{
                "Media (Photo / Documents)",
                "Description",
                "Title",
                "Service"};
        columnNames = new String[]{
                DatabaseHelper.COLUMN_MEDIA,
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_TITLE,
                DatabaseHelper.COLUMN_SERVICE};
        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);
            if (ids != null) {

                for (int i = 0; i < ids.length; i++) {
                    items.add(new Divider());
                    for (int j = titles.length - 1; j >= 0; j--) {
                        items.add(
                                helper.buildEditText(
                                        titles[j],
                                        databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                        tableName,
                                        columnNames[j],
                                        ids[i],xmlTags[j]));
                    }
                }
                productDetailsRows = ids.length;
            }


        }
        items.add(helper.buildAdd(4, titles, tableName, columnNames,xmlTags));

*/
        items.add(new Heading("COMPANY INDICATORS", "indicators"));
        tableName = DatabaseHelper.TABLE_COMPANY_INDICATORS;
        columnName = DatabaseHelper.COLUMN_COMPANY_SIZE;
        int selectedPosition = databaseHelper.getIntValue(columnName, tableName);
        items.add(helper.buildDropDown("Company / Institution Size", new String[]{
                "Self Employed",
                "1 - 10 Employees",
                "11 - 50 Employees",
                "51 - 200 Employees",
                "201 - 500 Employees",
                "501 - 1,000 Employees",
                "1,001 - 10,000 Employees",
                "10,001 or More"}, new int[]{0, 1, 2, 3, 4, 5, 6, 7}, selectedPosition, tableName, columnName, -1, "companySize"));

        columnName = DatabaseHelper.COLUMN_FOUNDING_YEAR_OF_COMPANY;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Founding year of Company / Institution", value, tableName, columnName, -1, "foundingYear"));
        columnName = DatabaseHelper.COLUMN_AGE_OF_ACTIVE_BUSINESS;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Age of Active Business", value, tableName, columnName, -1, "ageOfActBusiness"));
        columnName = DatabaseHelper.COLUMN_ANNUAL_SALES;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Annual Sales (Range)", value, tableName, columnName, -1, "annualSales"));
        columnName = DatabaseHelper.COLUMN_ANNUAL_REVENUE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Annual Revenue (Range)", value, tableName, columnName, -1, "annualrevenue"));


        columnName = DatabaseHelper.COLUMN_NO_OF_BRANCHES;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("No. of Branches", value, tableName, columnName, -1, "branches"));


        columnName = DatabaseHelper.COLUMN_TOTAL_NO_OF_EMPLOYEES;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Total no. of Employees", value, tableName, columnName, -1, "numberEmployees"));


        columnName = DatabaseHelper.COLUMN_NO_OF_EMPLOYEES_IN_PRODUCTION;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("No. of Employees in Production", value, tableName, columnName, -1, "numEmployeesProduction"));


        columnName = DatabaseHelper.COLUMN_NO_OF_ADMINISTRATIVE_STAFF;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("No. of Administrative Staff", value, tableName, columnName, -1, "numAdministrativeStaff"));


        columnName = DatabaseHelper.COLUMN_FREELANCE_ASSOCIATES;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("No. of Freelance Associates", value, tableName, columnName, -1, "numFreelancers"));

        columnName = DatabaseHelper.COLUMN_INVESTMENT_VOLUME;
        selectedPosition = databaseHelper.getIntValue(columnName, tableName);
        items.add(helper.buildDropDown("Investment Volume", new String[]{
                "Less than 5,000",
                "5,000-10,000",
                "10,000-15,000",
                "15,000-50,000",
                "50,000-150,000",
                "150,000-500,000",
                "more than 500,000"}, new int[]{0, 1, 2, 3, 4, 5, 6}, selectedPosition, tableName, columnName, -1, "investmentVolume"));


        columnName = DatabaseHelper.COLUMN_EMPLOYEE_ADDITIONAL_TRAINING;
        selectedPosition = databaseHelper.getIntValue(columnName, tableName);
        items.add(helper.buildDropDown("Employee’s additional training? How often? ", new String[]{
                "4 Times / Year ",
                "Twice / year",
                "Once / Year",
                "Once Every 2 Years"}, new int[]{0, 1, 2, 3}, selectedPosition, tableName, columnName, -1, "employeeAdditionalTraining"));


        columnName = DatabaseHelper.COLUMN_LAST_EMPLOYEE_TRAINING;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Last Employee Training", value, tableName, columnName, -1, "lastTraining"));


      /*
        items.add(new SimpleText("AWARDS", "awards"));
        tableName = DatabaseHelper.TABLE_AWARDS;
        xmlTags = new String[]{
                "awardfile",
                "telephone",
                "description",
                "date",
                "institution",
                "name"
        };
        titles = new String[]{
                "Award (FileChosed)",
                "Telephone / Fax",
                "Description",
                "Date",
                "Institution",
                "Name"};
        columnNames = new String[]{
                DatabaseHelper.COLUMN_AWARD_FILE,
                DatabaseHelper.COLUMN_AWARD_TEL_FAX,
                DatabaseHelper.COLUMN_AWARD_DESCRIPTION,
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_AWARD_INSTITUTION,
                DatabaseHelper.COLUMN_AWARD_NAME};
        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);
            if (ids != null) {

                for (int i = 0; i < ids.length; i++) {
                    items.add(new Divider());
                    for (int j = titles.length - 1; j >= 0; j--) {
                        if (columnNames[j].equals(DatabaseHelper.COLUMN_DATE)) {
                            items.add(helper.buildDate(
                                    titles[j],
                                    databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                    tableName,
                                    columnNames[j],
                                    ids[i], xmlTags[j]));
                        } else {
                            items.add(
                                    helper.buildEditText(
                                            titles[j],
                                            databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                            tableName,
                                            columnNames[j],
                                            ids[i], xmlTags[j]));
                        }
                    }
                }
                awardRows = ids.length;
            }


        }
        items.add(helper.buildAdd(6, titles, tableName, columnNames, xmlTags));
        items.add(new Divider());
*/
        items.add(new Heading("NEWS", null));
        items.add(new SimpleText("LATEST NEWS", "news"));
        tableName = DatabaseHelper.TABLE_LATEST_NEWS;
        titles = new String[]{
                "Description",
                "Date"};
        xmlTags = new String[]{
                "description",
                "date"};
        columnNames = new String[]{
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_DATE};
        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    for (int j = titles.length - 1; j >= 0; j--) {
                        if (columnNames[j].equals(DatabaseHelper.COLUMN_DATE)) {
                            items.add(helper.buildDate(
                                    titles[j],
                                    databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                    tableName,
                                    columnNames[j],
                                    ids[i], xmlTags[j]));
                        } else {
                            items.add(
                                    helper.buildEditText(
                                            titles[j],
                                            databaseHelper.getStringFromRow(tableName, columnNames[j], ids[i]),
                                            tableName,
                                            columnNames[j],
                                            ids[i], xmlTags[j]));
                        }
                    }
                    items.add(new Divider());
                }
                newsRows = ids.length;
            }
        }
        items.add(helper.buildAdd(2, titles, tableName, columnNames, xmlTags));
        return items;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d("Tab3", "Request Code  " + requestCode);
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
}