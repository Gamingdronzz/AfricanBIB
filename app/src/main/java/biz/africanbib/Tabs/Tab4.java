package biz.africanbib.Tabs;


import android.content.res.Configuration;
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
import java.util.Date;
import java.util.List;

import biz.africanbib.Adapters.ComplexRecyclerViewAdapter;
import biz.africanbib.MainActivity;
import biz.africanbib.Models.Add;
import biz.africanbib.Models.Divider;
import biz.africanbib.Models.DropDown;
import biz.africanbib.Models.Heading;
import biz.africanbib.Models.SimpleEditText;
import biz.africanbib.Models.SimpleText;
import biz.africanbib.R;
import biz.africanbib.Tools.DatabaseHelper;
import biz.africanbib.Tools.Utils;

//Our class extending fragment
public class Tab4 extends Fragment {

    RecyclerView recyclerView;
    ComplexRecyclerViewAdapter adapter;
    Utils utils;
    boolean isTab;
    DatabaseHelper databaseHelper;
    private int industryRows = 0;

    public ComplexRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_4, container, false);
        Log.d("Company", "Trying to initialize");
        utils = new Utils(getContext());
        isTab = utils.isTab();
        databaseHelper = new DatabaseHelper(view.getContext(), DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        init(view);

        return view;
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_4);
        adapter = new ComplexRecyclerViewAdapter(getSampleArrayList(), getFragmentManager());

        if (isTab) {
            setupGridLayout(true);
        } else {
            setupGridLayout(false);
        }
        //SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        //snapHelper.attachToRecyclerView(recyclerView);
        adapter.updateRow(DatabaseHelper.TABLE_SECTORS, industryRows);
        recyclerView.setAdapter(adapter);
        Log.d("Company", "Adapter set");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        // Check for the rotation
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


        }
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
        ArrayList<Object> items = new ArrayList<>();


        databaseHelper.getTableData(DatabaseHelper.TABLE_SECTORS);



        items.add(new Heading("BUSINESS INFORMATION"));

        String tableName = DatabaseHelper.TABLE_BUSINESS_CORRESPONDING_LANGUAGES;
        String columnName = DatabaseHelper.COLUMN_LANGUAGE;
        String si = databaseHelper.getStringValue(columnName, tableName);
        List<Integer> selectionLanguages = utils.getSelectedIndices(si);
        if (selectionLanguages != null) {
            for (int k :
                    selectionLanguages) {
                Log.v("Tab4", "Setting language = " + k);
            }
        }
        items.add(utils.buildMultiSelectDropdown("Business Corresponding Languages",
                tableName,
                columnName,
                new String[]{
                        "English",
                        "Somali",
                        "Arabic	",
                        "Ndebele",
                        "Seychellois",
                        "Shona",
                        "French",
                        "Amharic",
                        "Portuguese",
                        "Swazi",
                        "Kinyarwanda",
                        "Zulu",
                        "German",
                        "Malagasy",
                        "Swahili",
                        "Sango",
                        "Creole",
                        "Afrikaans",
                        "Spanish",
                        "Chichewa",
                        "Tigrinya",
                        "Xhosa"
                }, selectionLanguages, -1));


        items.add(new SimpleText("Industries"));
        tableName = DatabaseHelper.TABLE_SECTORS;
        String[] titles = new String[]{
                "Sector",
                "Industry"};
        String[] columnNames = new String[]{
                DatabaseHelper.COLUMN_SECTOR,
                DatabaseHelper.COLUMN_INDUSTRY

        };

        if (MainActivity.typeOfBusiness == MainActivity.EDITBUSINESS) {
            int[] ids = databaseHelper.getrowids(tableName);

            if (ids != null) {
                List<Integer> selectedIndices = new ArrayList<>();
                for (int i = 0; i < ids.length; i++) {
                    items.add(new Divider());
                    String s = databaseHelper.getStringFromRow(tableName, columnNames[0], ids[i]);

                    selectedIndices = utils.getSelectedIndices(s);
                    if (selectedIndices != null) {
                        for (int k :
                                selectedIndices) {
                            Log.v("Tab4", "Setting sector = " + k);
                        }
                    }
                    int selectedIndex = databaseHelper.getIntFromRow(tableName, columnNames[1], ids[i]);
                    items.add(utils.buildDropDown(
                            titles[1], utils.getIndustryList(),
                            selectedIndex,
                            tableName,
                            columnNames[1],
                            ids[i]));

                    items.add(utils.buildMultiSelectDropdown(titles[0],
                            tableName,
                            columnNames[0],
                            utils.manageMultiSelectList(selectedIndex),
                            selectedIndices,
                            ids[i]
                    ));
                }
                industryRows = ids.length;
            }


        }
        items.add(utils.buildAdd(2, titles,
                tableName,
                columnNames));


        items.add(new Heading("SOURCE OF DATA"));

        tableName = DatabaseHelper.TABLE_SOURCE_OF_DATA;
        columnName = DatabaseHelper.COLUMN_NAME_OF_COLLECTOR;
        String value;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(utils.buildEditText("Name of Collector", value, tableName, columnName, -1));
        columnName = DatabaseHelper.COLUMN_AUTHORIZED_BY;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(utils.buildEditText("Authorized By", value, tableName, columnName, -1));
        columnName = DatabaseHelper.COLUMN_PLACE_OF_COLECTION;
        int selectedPosition = databaseHelper.getIntValue(columnName, tableName);
        items.add(utils.buildDropDown("Place of Collection",
                new String[]{"Event", "Company", "Others"}, selectedPosition, tableName, columnName, -1));
        if (selectedPosition == 2) {
            columnName = DatabaseHelper.COLUMN_OTHERS_SPECIFY;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(utils.buildEditText("Place of Collection (Specify)", value, tableName, columnName, -1));
        }
        columnName = DatabaseHelper.COLUMN_DATE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(utils.buildDate("Date/Time", value,new Date(), tableName, columnName, -1));
        columnName = DatabaseHelper.COLUMN_LOCATION;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(utils.buildEditText("Name of Location / Event", value, tableName, columnName, -1));
        items.add(utils.buildDropDown("Country of Location / Event",
                utils.getCountryNames(), selectedPosition, tableName, columnName, -1));
        items.add(new SimpleText("DISCLAIMER\n\n" +
                "I certify that the information provided in this form is true, complete and correct to the best of my knowledge and belief. I understand that the information provided in this form is checked and updated by AfricanBIB GmbH on the AfricanBIB website with due diligence on a regular basis. This notwithstanding, data may become subject to changes during the intervening period. Therefore AfricanBIB GmbH does not assume any liability or guarantee for the timeliness, accuracy and completeness of the information provided. This applies also to other websites that may be accessed through hyperlinks. AfricanBIB GmbH assumes no responsibility for the contents of websites that can be accessed through such links.\n" +
                "Further, AfricanBIB GmbH reserves the right to change or amend the information provided at any time and without prior notice.\n" +
                "Contents and structure of this form sites are copyright protected. Reproduction of information or data content, in particular the use of text (whether in full or in part), pictures or graphics, requires the prior approval of AfricanBIB GmbH.\n"));


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
}