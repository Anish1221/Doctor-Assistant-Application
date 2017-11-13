package com.example.birat.Docassist;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class loginAsDoctor extends AppCompatActivity {

    TextInputEditText admNo, password;
    String admNoString = "", passwordString = "";
    boolean isDataValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_as_doctor);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        admNo = (TextInputEditText) findViewById(R.id.adm_no_login);
        password = (TextInputEditText) findViewById(R.id.password_login);
    }


    private boolean validateDetails() {

        boolean validateData = true;
        // Reset errors.
        admNo.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String admNoString = admNo.getText().toString();
        String passwordString = password.getText().toString();

        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordString)) {
            password.setError("This field is required");
            validateData = false;
            focusView = password;
        }        // Check for a valid Admission no, if the user entered one.
        if (TextUtils.isEmpty(admNoString)) {
            admNo.setError("This field is required");
            validateData = false;
            focusView = admNo;
        }
        if (validateData==false) {
            focusView.requestFocus();
        }

        return validateData;
    }

    public void loginCheckAsDoctor(View view){
        isDataValid = validateDetails();
        if (isDataValid == true) {
            admNoString = admNo.getText().toString();
            passwordString = password.getText().toString();
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(admNoString, passwordString);
        }
    }

    public class BackgroundTask extends AsyncTask<String,Void,String> {
        String loginAsDoctorURL;
        Context context;

        BackgroundTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute(){
            loginAsDoctorURL = "http://anoyin.000webhostapp.com/loginAsDoctor.php";
        }

        @Override
        protected String doInBackground(String... params) {
            String DOCTOR_ADM_NO=params[0];
            String DOCTOR_PASSWORD=params[1];

            try {
                URL url = new URL(loginAsDoctorURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("DOCTOR_ADM_NO", "UTF-8") +"="+URLEncoder.encode(DOCTOR_ADM_NO,"UTF-8")+"&"+
                        URLEncoder.encode("DOCTOR_PASSWORD", "UTF-8") +"="+URLEncoder.encode(DOCTOR_PASSWORD,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine())!= null){
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response.trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onProgressUpdate(Void... avoid){
            super.onProgressUpdate(avoid);
        }

        @Override
        protected void onPostExecute(String result){

            Boolean loginCheck = result.equals("Invalid Admission No or Password");
            if(loginCheck){
                Message.message(context,result);
                admNo.setText("");
                password.setText("");
            }else {
                Intent intent = new Intent(context,HomeScreenDoctor.class);
                intent.putExtra("admissionNo", admNoString);
                intent.putExtra("name",result);
                admNo.setText("");
                password.setText("");
                startActivity(intent);
                finishAffinity();

            }
        }
    }

}
