package cogentdatasolutions.project1.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cogentdatasolutions.project1.Fragments.Register;
import cogentdatasolutions.project1.R;

/**
 * Created by madhu on 15-Jul-16.
 */
public class EditDetails extends AppCompatActivity
{
    private static final String TAG = Register.class.getSimpleName();
    SharedPreferences.Editor editor;
    private JSONObject jsonObject1,jsonObject2 = null;
    private HttpURLConnection connection = null;
    private BufferedReader bufferedReader = null;
    private InputStream inputStream = null;
    URL url = null;
    String finalJson;
    Button next,saveChanges;
    EditText email,dob;
    String mail,empId,addr1,addr2,street1,street2,country1,country2,state1,state2,pin1,pin2;
    Toolbar basicdetails;
    AutoCompleteTextView highestQualify,industry,specialization,university,passingYear,cLocation,jobType,pLocation,nationality;
    MultiAutoCompleteTextView skills;
    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editdetails);
        email= (EditText) findViewById(R.id.email);
        dob= (EditText) findViewById(R.id.dob);
        highestQualify = (AutoCompleteTextView)findViewById(R.id.qualifications);
        industry = (AutoCompleteTextView)findViewById(R.id.industry);
        specialization = (AutoCompleteTextView)findViewById(R.id.specialization);
        university = (AutoCompleteTextView)findViewById(R.id.university);
        passingYear = (AutoCompleteTextView)findViewById(R.id.passingYear);
        cLocation = (AutoCompleteTextView)findViewById(R.id.currentLocation);
        jobType = (AutoCompleteTextView)findViewById(R.id.jobtype);
        pLocation = (AutoCompleteTextView)findViewById(R.id.prefferedLocation);
        nationality = (AutoCompleteTextView)findViewById(R.id.nationality);
        skills = (MultiAutoCompleteTextView)findViewById(R.id.skills);
        basicdetails= (Toolbar) findViewById(R.id.detailsbar);
      setSupportActionBar(basicdetails);
   getSupportActionBar().setTitle("Basic Details");
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mail  = prefs.getString("EMAILID","");
        empId = prefs.getString("EMPID","");
        email.setText(mail); 
       email.setFocusable(false);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                UpdateLabel();
            }
        };
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditDetails.this,date,
                        myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        next= (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EditDetails.this,EditAddress.class);
                startActivity(i);
                finish();

            }
        });
    }
    private void UpdateLabel() {
        String myformat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

//    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.editoptionsmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.changepassword1:
                Intent i=new Intent(EditDetails.this,Changepassword.class);
                startActivity(i);
                break;
            case R.id.settings:
                Intent i1=new Intent(EditDetails.this,Settings.class);
                startActivity(i1);
                break;
            case R.id.Logout:
                Intent i2=new Intent(EditDetails.this,MainActivity.class);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                editor = preferences.edit();
                editor.clear();
                editor.commit();

                startActivity(i2);
                break;
        }
        return true;
    }




}
