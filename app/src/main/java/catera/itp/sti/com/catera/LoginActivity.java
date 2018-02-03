package catera.itp.sti.com.catera;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public static User currentUser;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.AccountType))
                {

                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextSize(16);
                        ((TextView) v).setTextColor(Color.WHITE);

                        return v;
                    }
//
//                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                        View v = super.getDropDownView(position, convertView, parent);
//                        v.setBackgroundResource(android.R.drawable.spinner_background);
//
//                        ((TextView) v).setTextColor(Color.BLACK);
//
//                        ((TextView) v).setGravity(Gravity.CENTER);
//
//                        return v;
//                    }
                };

        ((Spinner)findViewById(R.id.spinner)).setAdapter(adapter);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((TextView)findViewById(R.id.txtUsername)).getText().length() <= 0)
                {
                    Toast.makeText(LoginActivity.this, "Please enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (((TextView)findViewById(R.id.txtPassword)).getText().length() <= 0)
                {
                    Toast.makeText(LoginActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Login();
            }
        });
    }


    void Login()
    {
        new HttpAsyncTask().execute(MainActivity.domain + "android_login.php?type=" + ((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString().toLowerCase());
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            String u, p;
            boolean userExists = false;
            u = ((TextView)findViewById(R.id.txtUsername)).getText().toString();
            p = ((TextView)findViewById(R.id.txtPassword)).getText().toString();
            String[] str = result.split("#");
            for (String i : str) {
                if (i.length() <= 0)
                    continue;

                String[] str2 = i.split("/");

                String username, password;
                username = str2[1];
                password = str2[2];

                if (username.equals(u))
                {
                    userExists = true;
                    if (password.equals(p))
                    {
                        currentUser = new User();
                        currentUser.ID = Integer.parseInt(str2[0]);
                        currentUser.username = str2[1];
                        currentUser.password = str2[2];
                        currentUser.firstName = str2[3];
                        currentUser.middleName = str2[4];
                        currentUser.lastName = str2[5];
                        currentUser.userType = ((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString();

                        Toast.makeText(LoginActivity.this, "Welcome "+currentUser.firstName+"!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, NavigationMenuActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    break;
                }
            }


            if (!userExists)
            {
                Toast.makeText(LoginActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
