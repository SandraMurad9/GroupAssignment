package course.edu.apps.groupassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText nameTxt;
    private EditText emailTxt;
    private EditText phoneTxt;
    Spinner genderSpn ;
    Spinner courseSpn;
    private EditText UniversityTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        genderSpn = findViewById(R.id.genderSpn);
        courseSpn = findViewById(R.id.courseSpn);
        setUpViews();
        populateGenderSpinner();
        populateCourseSpinner();
    }

    private void populateCourseSpinner() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Java");
        data.add("Data Base");
        data.add("Data Structure");
        ArrayAdapter<String > adpter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,data);
        courseSpn.setAdapter(adpter);
    }

    private void populateGenderSpinner() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Male");
        data.add("Female");
        ArrayAdapter<String > adpter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,data);
        genderSpn.setAdapter(adpter);
    }

    private void setUpViews() {
        nameTxt =  findViewById(R.id.nameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        UniversityTxt =findViewById(R.id.UniversityTxt);
    }

    private String processRequest(String restUrl) throws UnsupportedEncodingException {
        String name = nameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String phone = phoneTxt.getText().toString();
        String gender = genderSpn.getSelectedItem().toString();
        String university = UniversityTxt.getText().toString();
        String course = courseSpn.getSelectedItem().toString();

        String data = URLEncoder.encode("Name", "UTF-8")
                + "=" + URLEncoder.encode(name, "UTF-8");

        data += "&" + URLEncoder.encode("Email", "UTF-8") + "="
                + URLEncoder.encode(email, "UTF-8");

        data += "&" + URLEncoder.encode("Phone", "UTF-8")
                + "=" + URLEncoder.encode(phone, "UTF-8");

        data += "&" + URLEncoder.encode("Gender", "UTF-8") + "="
                + URLEncoder.encode(gender, "UTF-8");

        data += "&" + URLEncoder.encode("University", "UTF-8")
                + "=" + URLEncoder.encode(university, "UTF-8");

        String text = "";
        BufferedReader reader=null;

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL(restUrl);

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        // Show response on activity
        return text;



    }

    private class SendPostRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return processRequest(urls[0]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    public void btnAddOnClick(View view) {
        String restUrl = "http://127.0.0.1/groupAss1/addStudent.php";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    123);

        } else{
            SendPostRequest runner = new SendPostRequest();
            runner.execute(restUrl);
        }
    }
}