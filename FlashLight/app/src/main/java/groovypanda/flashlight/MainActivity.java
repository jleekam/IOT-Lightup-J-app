package groovypanda.flashlight;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

//    private static final String urlTarget = "http://192.168.0.42/io.cgi?DOA8=50";

    private static final String urlTargetOff = "http://192.168.0.42/io.cgi?DOA8=1"; // OFF
    private static final String urlTargetOn = "http://192.168.0.42/io.cgi?DOA8=0"; // ON
    Button button_activate;
    BufferedReader reader;
    private boolean statusOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button_activate = (Button) findViewById(R.id.activate_button);
        button_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new MyHttpTask().execute(urlTarget);
                if (statusOn) {
                    new MyHttpTask().execute(urlTargetOff);
                    statusOn = false;
                } else {
                    new MyHttpTask().execute(urlTargetOn);
                    statusOn = true;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyHttpTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line ="";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) connection.disconnect();
                try {
                    if(reader != null) reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // runs in Main thread
            super.onPostExecute(s);
        }

    }

}


