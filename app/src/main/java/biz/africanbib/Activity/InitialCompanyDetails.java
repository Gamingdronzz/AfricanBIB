package biz.africanbib.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import biz.africanbib.R;
import biz.africanbib.Tools.DatabaseHelper;

public class InitialCompanyDetails extends AppCompatActivity{

    Button button;
    EditText editTextCompanyName;
    DatabaseHelper databaseHelper;

    Response.Listener<String> listener;
    Response.ErrorListener errorListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_company_details);
        button = (Button) findViewById(R.id.button_submit);
        listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        databaseHelper = new DatabaseHelper(this,DatabaseHelper.DATABASE_NAME,null,DatabaseHelper.DATABASE_VERSION);


        StringRequest request = new StringRequest(StringRequest.Method.POST,"www.google.com/abc.php",listener,errorListener);





        editTextCompanyName = (EditText) findViewById(R.id.edit_text_company_name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForValues()) {
                    addBusiness();
                    LoadMainActivity();
                    Toast.makeText(InitialCompanyDetails.this, "Current Company = " + DatabaseHelper.getCurrentCompanyId(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(InitialCompanyDetails.this, "Please Enter a Company Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addBusiness()
    {
        databaseHelper.addBusiness(editTextCompanyName.getText().toString());
    }

    void LoadMainActivity() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),MainActivity.class);
        intent.putExtra("type",MainActivity.NEWBUSINESS);
        startActivity(intent);
        finish();
    }

    private boolean checkForValues() {
        if (editTextCompanyName.getText().toString().equals(""))
            return false;

        return true;
    }
}
