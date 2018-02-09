package catera.itp.sti.com.catera;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout btnAnnouncement, btnSchoolEvents, btnReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ShowMessage("");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ((TextView)findViewById(R.id.txtUser)).setText("Welcome " + LoginActivity.currentUser.firstName + "!");
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.txtUsername)).setText(LoginActivity.currentUser.firstName + " " + LoginActivity.currentUser.middleName + " " + LoginActivity.currentUser.lastName);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.txtAccountType)).setText(LoginActivity.currentUser.userType);

        btnAnnouncement = (LinearLayout)findViewById(R.id.home_announcement);
        btnReminders = (LinearLayout)findViewById(R.id.home_reminders);
        btnSchoolEvents = (LinearLayout)findViewById(R.id.home_school_events);

        btnAnnouncement.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowMessage("Announcement is a statement made to the public or to the media which gives information about something that has happened or that will happen.");
                return true;
            }
        });

        btnReminders.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowMessage("Something that serves as a reminder of another thing makes you think about the other thing.");
                return true;
            }
        });

        btnSchoolEvents.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowMessage(" anything that happens, especially something important or unusual");
                return true;
            }
        });

        btnReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMessage("");
                MainActivity.reminderMode = true;
                Intent i = new Intent(NavigationMenuActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMessage("");
                MainActivity.reminderMode = false;
                Intent i = new Intent(NavigationMenuActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnSchoolEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMessage("");
                MainActivity.reminderMode = true;
                Intent i = new Intent(NavigationMenuActivity.this, SchoolEventsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        }else if (id == R.id.nav_feedback)
        {
            Intent i = new Intent(NavigationMenuActivity.this, MessageListActivity.class);
            startActivity(i);
        }else if (id == R.id.nav_logout)
        {
            Intent i = new Intent(NavigationMenuActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }else if (id == R.id.nav_account)
        {
            Intent i = new Intent(NavigationMenuActivity.this, MyAccountActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void ShowMessage(String message)
    {
        ((TextView)findViewById(R.id.txtButtonDescription)).setText(message);
    }
}
