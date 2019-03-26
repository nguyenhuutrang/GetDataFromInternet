package homework.android.getdatafrominternet;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> array_lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        lv = (ListView) findViewById( R.id.lv_result );
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute( "https://jsonplaceholder.typicode.com/users" );

            }
        } );

    }


    class ReadJSON extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            return getDataFromWebsite( strings[0] );
        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText( getApplicationContext(),s,Toast.LENGTH_LONG ).show();
            try {
                array_lv = new ArrayList<String>(  );
                JSONArray root = new JSONArray( s );
                for(int i = 0; i < root.length(); ++i){
                    JSONObject item = root.getJSONObject( i );
                    String name = item.getString( "name" );
                    String email = item.getString( "email" );
                    JSONObject jsonAddress = item.getJSONObject( "address" );
                    String address = jsonAddress.getString( "street" ) + " - " + jsonAddress.getString( "city" );
                    array_lv.add( name + "\n" + email + "\n" + address );
                }

                ArrayAdapter adapter = new ArrayAdapter( getApplicationContext(),android.R.layout.simple_list_item_1,array_lv );

                lv.setAdapter( adapter );
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private String getDataFromWebsite(String theUrl){
        StringBuilder content = new StringBuilder();
        try    {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return content.toString();
    }
}
