package catera.itp.sti.com.catera;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MyAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String fullName = LoginActivity.currentUser.firstName;
        fullName += " " + LoginActivity.currentUser.middleName;
        fullName += " " + LoginActivity.currentUser.lastName;

        ((TextView)findViewById(R.id.txtFullName)).setText(fullName);
        ((TextView)findViewById(R.id.txtUserID)).setText(LoginActivity.currentUser.username);
        ((TextView)findViewById(R.id.txtAccountType)).setText(LoginActivity.currentUser.userType);
    }

}
