package com.yudiz.beacondemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.yudiz.beacondemo.MainActivity.destinationNode;
import static com.yudiz.beacondemo.MainActivity.destination_num;
import static com.yudiz.beacondemo.MainActivity.destinationcursor;

public class sourceSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    private static final int REQUEST_CODE_SRC = 1;
    ArrayList<String> sourceData = new ArrayList<String>();
    private static String src_previous_choice = "選擇起始";//default

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchable);

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.searchlistview);


        destinationcursor.moveToFirst(); //移至第一筆
        destination_num=destinationcursor.getCount();
        if(destination_num!=0){
            sourceData.add("我的位置");
            for(int i=0;i<destination_num;i++){
                sourceData.add(destinationcursor.getString(1));
                destinationcursor.moveToNext();
            }
//            data.add("我的位置");
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, sourceData );

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                //destinationcursor.moveToFirst();
                //destinationcursor.moveToPosition(position+1);
                //String content = destinationcursor.getString(1);
                String content = (String)adapter.getItem(position);
                src_previous_choice = content;
                Intent intent = getIntent();
                intent.putExtra("src_ListItemContent", content);//xin1116
                setResult(REQUEST_CODE_SRC, intent);
                finish();

            }
        });

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //先檢驗使用者輸入存不存在
        boolean exist=false;
        int listSize=adapter.getCount();
        for(int i=0;i<listSize;i++){
            if(adapter.getItem(i).equals(query)){
                exist=true;
            }
        }
        Log.d("1121size", "hellooo"+String.valueOf(listSize));

        if(exist){
            Toast.makeText(this, query, Toast.LENGTH_LONG).show();
            // 換成新的寫法
            src_previous_choice = query;
            Intent intent = getIntent();
            intent.putExtra("src_ListItemContent", query);//xin1116
            setResult(REQUEST_CODE_SRC, intent);
            finish();
        } else if(listSize>=1){//如果資料庫有類似的，那就拿第一筆
            String content=adapter.getItem(0);
            Toast.makeText(this, content, Toast.LENGTH_LONG).show();
            // 換成新的寫法
            src_previous_choice = content;
            Intent intent = getIntent();
            intent.putExtra("ListItemContent", content);//xin1116
            setResult(REQUEST_CODE_SRC, intent);
            finish();
        }else{
            Toast.makeText(this, "查詢不存在", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
    @Override
    public void onBackPressed()
    {   // 按了 Android 裝置的實體返回鍵
        // 準備回傳的資料


        Intent mIntent = getIntent();
        mIntent.putExtra("src_ListItemContent", src_previous_choice);
        Log.d("leosu", "onBackPressed_previous_choice: "+src_previous_choice);
        setResult(REQUEST_CODE_SRC, mIntent);
        Log.d("leosu", "onBackPressed");

        super.onBackPressed();
    }

}