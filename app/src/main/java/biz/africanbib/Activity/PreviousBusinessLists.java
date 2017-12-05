package biz.africanbib.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import biz.africanbib.Adapters.RecyclerViewAdapterPreviousBusiness;
import biz.africanbib.Listeners.ClickListener;
import biz.africanbib.Listeners.RecyclerViewTouchListeners;
import biz.africanbib.Models.PreviousBusiness;
import biz.africanbib.R;
import biz.africanbib.Tools.DatabaseHelper;

public class PreviousBusinessLists extends AppCompatActivity implements View.OnCreateContextMenuListener {


    RecyclerView recyclerView;
    RecyclerViewAdapterPreviousBusiness adapter;
    private EditText editText;
    private List<PreviousBusiness> modelList;
    DatabaseHelper databaseHelper;
    int clickedPosition = -1;
    private final int CONTEXT_MENU_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_business_lists);
        databaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);

        bindviews();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });
        registerForContextMenu(recyclerView);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListeners(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                DatabaseHelper.setCurrentCompanyId(modelList.get(position).getBusinessID());
                intent.putExtra("type", MainActivity.EDITBUSINESS);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {
                clickedPosition = position;
                recyclerView.showContextMenu();
            }
        }));
        getPreviousBusinessList();
    }

    void filter(String text) {
        List<PreviousBusiness> temp = new ArrayList();
        for (PreviousBusiness p : modelList) {
            if (p.getBusinessName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(p);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }

    private void bindviews() {
        editText = (EditText) findViewById(R.id.edtSearch);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_previous_businesses);
        modelList = new ArrayList<PreviousBusiness>();
        adapter = new RecyclerViewAdapterPreviousBusiness(modelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void getPreviousBusinessList() {
        PreviousBusiness[] result = databaseHelper.getPreviousBusinessList();
        if (result == null) {
            Toast.makeText(this, "No Businesses Yet", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < result.length; i++) {
            modelList.add(i, result[i]);
            adapter.notifyItemInserted(i);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.recycler_view_previous_businesses) {
            menu.setHeaderTitle(modelList.get(clickedPosition).getBusinessName());
            menu.add(Menu.NONE, CONTEXT_MENU_DELETE, Menu.NONE, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int index = item.getItemId();
        if (index == CONTEXT_MENU_DELETE) {
            int businessid = modelList.get(clickedPosition).getBusinessID();
            if (databaseHelper.deleteBusiness(businessid)) {
                modelList.remove(clickedPosition);
                adapter.notifyItemRemoved(clickedPosition);
            } else {

            }
        }
        return true;
    }

}
