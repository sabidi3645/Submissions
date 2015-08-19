package com.codepath.todooapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.apache.commons.io.FileUtils.readLines;
import static org.apache.commons.io.FileUtils.writeLines;


public class ToDo extends ActionBarActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    EditText eNewItem;
    int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todoo);
        lvItems = (ListView)findViewById(R.id.lvItems);      //When this layout is created we want to assign an object to the text view
        items = new ArrayList<String>();                    // Creating a new String Arraylist. By default the data type is numeric. So need to cast String
        //A list view has many items/views. We need to create an array adapter to manage it.
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        //itemsAdapter.add("First box");
        //itemsAdapter.add("Second box");
        setupListViewListener();
        editItems();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_doo, menu);
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

    public void OnAddItem(View view) {
        eNewItem = (EditText)findViewById(R.id.eNewItem);
        String fieldValue = eNewItem.getText().toString();
        itemsAdapter.add(fieldValue);
        writeItems();
        eNewItem.setText("");

    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

    }

    private void editItems(){

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

               String fieldValue = itemsAdapter.getItem(position).toString();
               //   Intent CodeLearnFirstIntent = new Intent (getApplicationContext(), EditItemActivity.class);
               //    CodeLearnFirstIntent.putExtra("username",fieldValue);
               //   startActivity(CodeLearnFirstIntent);

               Intent i = new Intent(ToDo.this, EditItemActivity.class);
               i.putExtra("username", fieldValue); // pass arbitrary data to launched activity
               i.putExtra("position", itemsAdapter.getPosition(fieldValue));
               startActivityForResult(i, REQUEST_CODE);
           }
       }


        );
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String name = data.getExtras().getString("name");
            int code = data.getExtras().getInt("code", 0);
            items.set(code, name);
            itemsAdapter.notifyDataSetChanged();
            writeItems();

            // Toast the name to display temporarily on screen
           // Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        }
    }

    //This function when called reads the contents of todo.txt file and loads them in the ArrayList
    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(readLines(todoFile));
        }
        catch(IOException e){
            items = new ArrayList<String>();
        }
    }

    //This function when called write the ToDo updated items in the todo.txt file
    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            writeLines(todoFile, items);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }



}
