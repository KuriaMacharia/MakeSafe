package com.center.makesafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.center.makesafe.Helper.CheckNetWorkStatus;
import com.center.makesafe.Helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_COMPANY = "company";
    private static final String KEY_REPRESENTATIVE = "represenative";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_LICENSE = "license_number";
    private static final String KEY_ABN = "abn";
    private static final String KEY_REGION = "region";
    private static final String KEY_POSTCODE = "postcode";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_TYPE = "type";
    private static final String KEY_PASSWORD = "password";

    private static final String BASE_URL = "http://www.agizakenya.com/makesafe/";

    String[] type = {"---Select---", "Plumbing", "Electricals","Construction"};

    EditText companyEdt, represenativeEdt, phoneEdt, emailEdt, websiteEdt, licenseEdt, abnEdt, regionEdt,postcodeEdt,
            addressEdt, passwordEdt;
    Spinner typeSpin;
    CheckBox agreeBox;
    Button registerBtn;
    String selectedType, typeSelected;
    ProgressDialog UstartDialog;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        companyEdt=(EditText) findViewById(R.id.edt_company);
        represenativeEdt=(EditText) findViewById(R.id.edt_representative);
        phoneEdt=(EditText) findViewById(R.id.edt_phone);
        emailEdt=(EditText) findViewById(R.id.edt_email);
        websiteEdt=(EditText) findViewById(R.id.edt_website);
        licenseEdt=(EditText) findViewById(R.id.edt_license_number);
        abnEdt=(EditText) findViewById(R.id.edt_abn);
        regionEdt=(EditText) findViewById(R.id.edt_region);
        postcodeEdt=(EditText) findViewById(R.id.edt_post_code);
        addressEdt=(EditText) findViewById(R.id.edt_address);
        passwordEdt=(EditText) findViewById(R.id.edt_password_register);

        typeSpin=(Spinner) findViewById(R.id.spin_type);
        agreeBox=(CheckBox) findViewById(R.id.check_terms);
        registerBtn=(Button) findViewById(R.id.btn_register);

        UstartDialog = new ProgressDialog(RegisterActivity.this, R.style.mydialog);

        ArrayAdapter dir = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item, type);
        dir.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpin.setAdapter(dir);
        typeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedType=String.valueOf(typeSpin.getSelectedItem());
                if (!selectedType.contentEquals("---Select---")) {
                    typeSelected=selectedType;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetWorkStatus.isNetworkAvailable(RegisterActivity.this)) {
                        new RegisterTrade().execute();
                }else{
                    Toast.makeText(RegisterActivity.this, "Unable to connect to internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class RegisterTrade extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UstartDialog.setMessage("Registering. Please wait...");
            UstartDialog.setIndeterminate(false);
            UstartDialog.setCancelable(false);
            UstartDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();

            httpParams.put(KEY_COMPANY, companyEdt.getText().toString());
            httpParams.put(KEY_REPRESENTATIVE, represenativeEdt.getText().toString());

            httpParams.put(KEY_PHONE, phoneEdt.getText().toString());
            httpParams.put(KEY_EMAIL, emailEdt.getText().toString());
            httpParams.put(KEY_WEBSITE, websiteEdt.getText().toString());

            httpParams.put(KEY_LICENSE, licenseEdt.getText().toString());
            httpParams.put(KEY_ABN, abnEdt.getText().toString());

            httpParams.put(KEY_REGION, regionEdt.getText().toString());
            httpParams.put(KEY_POSTCODE, postcodeEdt.getText().toString());
            httpParams.put(KEY_ADDRESS, addressEdt.getText().toString());

            httpParams.put(KEY_TYPE, typeSelected);
            httpParams.put(KEY_PASSWORD, passwordEdt.getText().toString());

            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "register_trade.php", "POST", httpParams);
            if(success==1)
                try {
                    success = jsonObject.getInt(KEY_SUCCESS);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            return null;
        }

        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        Toast.makeText(RegisterActivity.this,"Road Added",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        UstartDialog.dismiss();
                    } else {
                        Toast.makeText(RegisterActivity.this,"Registraion Failed",Toast.LENGTH_LONG).show();
                        UstartDialog.dismiss();
                    }
                }
            });
        }
    }
}
