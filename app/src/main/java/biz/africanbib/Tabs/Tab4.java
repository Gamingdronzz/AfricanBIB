package biz.africanbib.Tabs;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
public class Tab4 extends Fragment {

    RecyclerView recyclerView;
    ComplexRecyclerViewAdapter adapter;
    Helper helper;
    boolean isTab;
    DatabaseHelper databaseHelper;
    private int industryRows = 0;
    CheckBox accept;
    ArrayList<Object> items;

    public String nameOfCollector = "Name of Collector *";
    public String authorizedBy = "Authorized By *";
    public String placeOfCollection = "Place of Collection *";
    private final String TAG = "Tab4";
    private Fragment fragment;
    ProgressDialog progressDialog;
    public boolean getAccepted() {
        return accept.isChecked();
    }

    public ArrayList<Object> getList() {
        return items;
    }


    public ComplexRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_4, container, false);
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

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_4);
        accept = (CheckBox) view.findViewById(R.id.accept);
        getSampleArrayList();

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

    private class LoadTab extends AsyncTask<Void, Void, ArrayList<Object>> {

        @Override
        protected ArrayList<Object> doInBackground(Void... voids) {
            items = new ArrayList<>();


            databaseHelper.getTableData(DatabaseHelper.TABLE_SECTORS);


            items.add(new Heading("BUSINESS INFORMATION", null));

            String tableName = DatabaseHelper.TABLE_BUSINESS_CORRESPONDING_LANGUAGES;
            String columnName = DatabaseHelper.COLUMN_LANGUAGE;
            String si = databaseHelper.getStringValue(columnName, tableName);
            List<Integer> selectionLanguages = helper.getSelectedIndices(si);
            if (selectionLanguages != null) {
                for (int k :
                        selectionLanguages) {
                    Log.v("Tab4", "Setting language = " + k);
                }
            }
            items.add(helper.buildMultiSelectDropdown("Business Corresponding Languages",
                    tableName,
                    columnName,
                    new String[]{
                            "Afrikaans",
                            "Amharic",
                            "Arabic",
                            "Chichewa",
                            "English",
                            "French",
                            "German",
                            "Kinyarwanda",
                            "Malagasy",
                            "Ndebele",
                            "Portuguese",
                            "Sango",
                            "Seychellois Creole",
                            "Shona",
                            "Somali",
                            "Spanish",
                            "Swahili",
                            "Swazi",
                            "Tigrinya",
                            "Xhosa",
                            "Zulu"
                    },
                    new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 10, 12, 13, 14, 15, 16, 17, 18, 19, 21, 20},
                    selectionLanguages, -1, "businessLanguage"));


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
                        String s = databaseHelper.getStringFromRow(tableName, columnNames[0], ids[i]);

                        selectedIndices = helper.getSelectedIndices(s);
                        if (selectedIndices != null) {
                            for (int k :
                                    selectedIndices) {
                                Log.v("Tab4", "Setting sector = " + k);
                            }
                        }
                        int selectedIndex = databaseHelper.getIntFromRow(tableName, columnNames[1], ids[i]);
                        items.add(helper.buildDropDown(
                                titles[1], helper.getIndustryList(), helper.getIndustryCodes(),
                                selectedIndex,
                                tableName,
                                columnNames[1],
                                ids[i], "industry"));

                        items.add(helper.buildMultiSelectDropdown(titles[0],
                                tableName,
                                columnNames[0],
                                helper.manageMultiSelectList(selectedIndex),
                                helper.manageMultiSelectList2(selectedIndex),
                                selectedIndices,
                                ids[i], "sector"
                        ));
                        items.add(new Divider());
                    }
                    industryRows = ids.length;
                }


            }
            items.add(helper.buildAdd(2, titles,
                    tableName,
                    columnNames, new String[]{"sector", "industry"}));


            items.add(new Heading("SOURCE OF DATA", null));

            tableName = DatabaseHelper.TABLE_SOURCE_OF_DATA;
            columnName = DatabaseHelper.COLUMN_NAME_OF_COLLECTOR;
            String value;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText(nameOfCollector, value, tableName, columnName, -1, "approvedBy"));
            columnName = DatabaseHelper.COLUMN_AUTHORIZED_BY;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText(authorizedBy, value, tableName, columnName, -1, "authorizedBy"));

            columnName = DatabaseHelper.COLUMN_PLACE_OF_COLECTION;
            int selectedPosition = databaseHelper.getIntValue(columnName, tableName);
            items.add(helper.buildDropDown(placeOfCollection,
                    new String[]{"Event", "Company", "Others"}, new int[]{0}, selectedPosition, tableName, columnName, -1, "collectedBy"));
            if (selectedPosition == 2) {
                columnName = DatabaseHelper.COLUMN_OTHERS_SPECIFY;
                value = databaseHelper.getStringValue(columnName, tableName);
                items.add(helper.buildEditText("Place of Collection (Specify)", value, tableName, columnName, -1, "collectedBy"));
            }

            columnName = DatabaseHelper.COLUMN_DATE;
            items.add(helper.buildDate("Date/Time", databaseHelper.getStringValue(columnName, tableName), tableName, columnName, -1, null));

            columnName = DatabaseHelper.COLUMN_LOCATION;
            value = databaseHelper.getStringValue(columnName, tableName);
            items.add(helper.buildEditText("Name of Location / Event", value, tableName, columnName, -1, "location"));

            columnName = DatabaseHelper.COLUMN_GRANT_FREE_ACCESS;
            selectedPosition = databaseHelper.getIntValue(columnName, tableName);
            items.add(helper.buildDropDown("Grant Free Access", new String[]{"False", "True"}, new int[]{0, 1}, selectedPosition, tableName, columnName, -1, "grantFreeAccess"));

            columnName = DatabaseHelper.COLUMN_DATE_GRANT_FREE_ACCESS;
            items.add(helper.buildDate("Date Grant Free Access", databaseHelper.getStringValue(columnName, tableName), tableName, columnName, -1, "dateGrantFreeAccess"));

            columnName = DatabaseHelper.COLUMN_BUSINESS_CARD_GENERATION;
            selectedPosition = databaseHelper.getIntValue(columnName, tableName);
            items.add(helper.buildDropDown("Business Card Generation", new String[]{"False", "True"}, new int[]{0, 1}, selectedPosition, tableName, columnName, -1, "businessCardGeneration"));

            columnName = DatabaseHelper.COLUMN_COUNTRY;
            items.add(helper.buildDropDown("Country of Location / Event",
                    helper.getCountryNames(), helper.getCountryCodes(), selectedPosition, tableName, columnName, -1, "countryoflocation"));
            items.add(new SimpleText("DISCLAIMER\n\n" +
                    "I certify that the information provided in this form is true, complete and correct to the best of my knowledge and belief. I understand that the information provided in this form is checked and updated by AfricanBIB GmbH on the AfricanBIB website with due diligence on a regular basis. This notwithstanding, data may become subject to changes during the intervening period. Therefore AfricanBIB GmbH does not assume any liability or guarantee for the timeliness, accuracy and completeness of the information provided. This applies also to other websites that may be accessed through hyperlinks. AfricanBIB GmbH assumes no responsibility for the contents of websites that can be accessed through such links.\n" +
                    "Further, AfricanBIB GmbH reserves the right to change or amend the information provided at any time and without prior notice.\n" +
                    "Contents and structure of this form sites are copyright protected. Reproduction of information or data content, in particular the use of text (whether in full or in part), pictures or graphics, requires the prior approval of AfricanBIB GmbH.\n", "disclaimer"));

            Log.v(TAG,"Tab4 Initialize Complete");
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
            //SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
            //snapHelper.attachToRecyclerView(recyclerView);
            adapter.updateRow(DatabaseHelper.TABLE_SECTORS, industryRows);
            recyclerView.setAdapter(adapter);
            Log.d("Company", "Adapter set");
            progressDialog.dismiss();
        }
    }
}