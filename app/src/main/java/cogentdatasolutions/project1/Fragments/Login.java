package cogentdatasolutions.project1.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cogentdatasolutions.project1.Activities.Forgototp;
import cogentdatasolutions.project1.Activities.MainActivity;
import cogentdatasolutions.project1.R;

/**
 * Created by Divya on 6/14/2016.
 */
public class Login extends Fragment {

    private static final String TAG = Login.class.getSimpleName();
    EditText emailId, password,sample;
    Button submit;
    TextView textView;
    String empid,errmsg;
    String loginid,loginpassword,mailtxt;
    String finalJson;
    JSONObject jsonObject1,jsonObject2 = null;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.login,container,false);

        emailId = (EditText) view.findViewById(R.id.emailId);
        password = (EditText) view.findViewById(R.id.password);
        submit = (Button) view.findViewById(R.id.submit);

        textView = (TextView) view.findViewById(R.id.textView);
        textView.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle("Enter Your Mail Id");
                final EditText input=new EditText(getContext());
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mailtxt=input.getText().toString();
                       // sample.setText(mailtxt);
                        new ForgotPassTask().execute("http://10.80.15.119:8080/OptnCpt/rest/service/recoverPassword");
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
//                LayoutInflater li=LayoutInflater.from(getContext());
//                View mview=li.inflate(R.layout.forgotpassword,(ViewGroup)getView(),false);
//                AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
//                adb.setView(mview);
//               final EditText forgotmail= (EditText) view.findViewById(R.id.email);
//                //forgPassEmail=forgotmail.getText().toString();
//                adb.setCancelable(false)
//                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                sample.setText(forgotmail.getText().toString());
//                               // new ForgotPassTask().execute("http://10.80.15.119:8080/OptnCpt/rest/service/recoverpassword");
//                               // forgPassEmail=forgotmail.getText().toString();
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog dialog=adb.create();
//                dialog.show();
//
           }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginid=emailId.getText().toString();
                loginpassword=password.getText().toString();

                if(emailId.length()==0) {
                    emailId.setError("Email cannot be blank");
                } else if (password.length()==0){
                    password.setError("Password cannot be blank");
                } else if (password.length()<8||password.length()>70){
                    Toast.makeText(getActivity(), "Password should contain minimum 8 characters and max 70 characters", Toast.LENGTH_LONG).show();
                }
                else if (!isValidPassword(loginpassword)){
                    Toast.makeText(getActivity(), "Password Should contain Special Characters and numbers", Toast.LENGTH_LONG).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(emailId.getText().toString()).matches()){
                    emailId.setError("Invalid Email Address");
                }else {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    editor = preferences.edit();
                    editor.putString("EMAILID",loginid);
                    editor.putString("PWD",loginpassword);
                    editor.commit();
                    String str=preferences.getString("EMAILID","");
                    Log.e(TAG, "Preferences string: "+str );
                new LoginServerTask().execute("http://10.80.15.119:8080/OptnCpt/rest/service/login");

            }

            }
        });
        return view;
    }

    public boolean isValidPassword(final String password){
        Pattern pattern;
        Matcher matcher;
//        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public class LoginServerTask extends AsyncTask<String, String, String> {

         HttpURLConnection connection = null;
         BufferedReader bufferedReader;
         URL url;
         InputStream inputStream;

        @Override
        protected String doInBackground(String... params) {

            try {

                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                jsonObject1 = new JSONObject();
                jsonObject1.put("loginid","" + loginid);
                jsonObject1.put("loginpassword","" + loginpassword);

                String jsonObj = jsonObject1.toString();
                Log.e(TAG, "doInBackground: "+jsonObj );
                //Header
                //connection.setRequestProperty("jsonobject",""+jsonObject1.toString());
                connection.setRequestProperty("loginObject", ""+jsonObj);
                connection.connect();
                inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder buffer = new StringBuilder();

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }


                finalJson = buffer.toString();
                Log.e(TAG, "JSON Object"+finalJson );
                // JSONObject parentObject=new JSONObject(finalJson);
                // JSONArray parentArray=parentObject.getJSONArray("worldpopulation");
                // JSONObject lastObject=parentArray.getJSONObject(1);
//                JSONArray parentArr=new JSONArray(finalJson);
//                JSONObject lastObject=parentArr.getJSONObject(1);
//                String result=lastObject.getString("status");


                return finalJson;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (finalJson!=null){
                try {
                    JSONObject jobj = new JSONObject(finalJson);
                    Log.e(TAG, "Response Json: "+jobj );
                    String str = (String)jobj.get("status");
                    if (str.equals("true")) {
                        String msg =jobj.getString("msg");
                        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                         empid=(String)jobj.get("employeeId");
                        Log.e(TAG, "EmployeeId: "+empid );
                        editor.putString("EMPID",empid);
                        editor.commit();
                        Intent i=new Intent(getActivity(),MainActivity.class);
                        startActivity(i);
//                        startActivity(new Intent(getActivity(), MainActivity.class));
//                        Fragment frag = new Login();
//                        FragmentTransaction ft = getFragmentManager().beginTransaction();
//                        ft.replace(R.id.register_frag,frag);
//
                    }else if(str.equals("false"))
                    {
                        errmsg=(String)jobj.get("err_msg");
                        Log.e(TAG, "ErrorMsg: "+errmsg );
                        Toast.makeText(getContext(), errmsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }
             emailId.setText("");
            password.setText("");
            super.onPostExecute(result);
        }
    }

   public class ForgotPassTask extends AsyncTask<String,String,String>{

        HttpURLConnection connection = null;
        BufferedReader bufferedReader;
        URL url;
        InputStream inputStream;


        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                jsonObject2 = new JSONObject();
                jsonObject2.put("mail", ""+mailtxt);

                String jsonObj2 = jsonObject2.toString();

                connection.setRequestProperty("forgotPasswordDetails", ""+jsonObj2);
                connection.connect();
                inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }
                finalJson = buffer.toString();
                Log.e(TAG, "RESPONSE FROM SERVER IS: "+finalJson);
                return finalJson;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection!=null){
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (finalJson!=null){
                try {
                    JSONObject jsonobj=new JSONObject(finalJson);
                    Log.e(TAG, "JSON OBJECT FROM SERVER: "+jsonobj);
                    String str=jsonobj.getString("status");
                    if (str.equals("true"))
                    {
                        String msg =jsonobj.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getActivity(),Forgototp.class);
                        startActivity(i);

                    }else {
                        errmsg=(String)jsonobj.get("err_msg");
                        Log.e(TAG, "ErrorMsg: "+errmsg );
                        Toast.makeText(getContext(), errmsg, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }
}
