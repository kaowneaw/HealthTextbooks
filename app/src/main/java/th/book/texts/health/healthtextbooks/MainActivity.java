package th.book.texts.health.healthtextbooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import th.book.texts.health.healthtextbooks.Fragment.HomeFragment;
import th.book.texts.health.healthtextbooks.Fragment.MyProfileFragment;
import th.book.texts.health.healthtextbooks.Fragment.MyReciveFragment;
import th.book.texts.health.healthtextbooks.Fragment.OrderFragment;
import th.book.texts.health.healthtextbooks.Fragment.OrderReciveFragment;
import th.book.texts.health.healthtextbooks.Fragment.CalorieFragment;
import th.book.texts.health.healthtextbooks.Fragment.RefrigeratorFragment;
import th.book.texts.health.healthtextbooks.Utill.UserPreference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UserPreference pref;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            }
//        });

        pref = new UserPreference(this);
        Toast.makeText(getApplicationContext(), pref.getName(), Toast.LENGTH_SHORT).show();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initial();
    }

    private void initial() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, RefrigeratorFragment.newInstance("", "")).commit();
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        View headView = nav_view.getHeaderView(0);
        ImageView profile = (ImageView) headView.findViewById(R.id.profile);
        TextView nameProfile = (TextView) headView.findViewById(R.id.nameProfile);
        TextView emailProfile = (TextView) headView.findViewById(R.id.emailProfile);
        nameProfile.setText(pref.getName());
        emailProfile.setText(pref.getEmail());
        AQuery aq = new AQuery(getApplicationContext());
        aq.id(profile).image("https://graph.facebook.com/" + pref.getUserID() + "/picture?width=150&height=150");
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_camera) {
            fragmentTransaction.replace(R.id.container, HomeFragment.newInstance("", ""));
            fragmentTransaction.addToBackStack(null).commit();
        } else if (id == R.id.cal) {
            fragmentTransaction.replace(R.id.container, CalorieFragment.newInstance("", ""));
            fragmentTransaction.addToBackStack(null).commit();
        } else if (id == R.id.recivebook) {
            fragmentTransaction.replace(R.id.container, MyReciveFragment.newInstance("", ""));
            fragmentTransaction.addToBackStack(null).commit();
        } else if (id == R.id.refrigeratorMenu) {
            fragmentTransaction.replace(R.id.container, RefrigeratorFragment.newInstance("", ""));
            fragmentTransaction.addToBackStack(null).commit();
        } else if (id == R.id.nav_order) {
            fragmentTransaction.replace(R.id.container, OrderFragment.newInstance("", ""));
            fragmentTransaction.addToBackStack(null).commit();
        } else if (id == R.id.my_order) {
            fragmentTransaction.replace(R.id.container, OrderReciveFragment.newInstance("", ""));
            fragmentTransaction.addToBackStack(null).commit();
        } else if (id == R.id.myprofile) {
            fragmentTransaction.replace(R.id.container, MyProfileFragment.newInstance("", ""));
            fragmentTransaction.addToBackStack(null).commit();
        } else if (id == R.id.logout) {
            FacebookSdk.sdkInitialize(this);
            LoginManager.getInstance().logOut();
            UserPreference pref = new UserPreference(this);
            pref.clearPreference();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
