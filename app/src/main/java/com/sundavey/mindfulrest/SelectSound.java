package com.sundavey.mindfulrest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectSound extends Activity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sound);

        mListView = (ListView) findViewById(R.id.list);
        String [] titles = new String[]{"Singing Bowl", "Peaceful Woods", "Cave", "Waves", "Wind"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, titles);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i;
                String itemValue = (String) mListView.getItemAtPosition(i);
                Intent intent = new Intent();
                intent.putExtra("Position", position);
                intent.putExtra("Title", itemValue);
                setResult(1, intent);
                finish();
            }
        });
    }
}
