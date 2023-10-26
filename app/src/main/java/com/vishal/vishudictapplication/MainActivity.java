package com.vishal.vishudictapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    EditText enterWord;
    Button btnFind;
    TextView meaning, result;
    RequestQueue queue;
    ListView listView;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterWord = findViewById(R.id.searchWord);
        btnFind = findViewById(R.id.btnFind);
        meaning = findViewById(R.id.meaning);
        result = findViewById(R.id.result);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = enterWord.getText().toString();
                findWord(word);

                // Add the word to the list of last five words
                list.add(0, word);
                if (list.size() > 5) {
                    list.remove(5);
                }

                // Update the ListView
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void findWord(String enterWord) {
        String url ="https://api.dictionaryapi.dev/api/v2/entries/en/"+enterWord;
        JsonArrayRequest request =new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject object =response.getJSONObject(0);


                    String words =object.getString("word");

                    meaning.setText(words);

                    JSONArray jsonArray =object.getJSONArray("meanings");

                    JSONObject jsonObject =jsonArray.getJSONObject(0);

                    JSONArray jsonArray1 =jsonObject.getJSONArray("definitions");
                    JSONObject jsonObject1 =jsonArray1.getJSONObject(0);
//                    String poSpeech=jsonObject.getString("partOfSpeech");
                    String definition =jsonObject1.getString("definition");
                    result.setText("Definiton -:\n"+definition);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something error", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(request);

    }
}