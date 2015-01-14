package com.eseoteam.android.tagfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class AddTagActivity extends Activity {

    EditText editTextTagName;
    EditText editTextTagId;
    EditText editTextTagData;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        editTextTagName = (EditText) findViewById(R.id.tagNameField);
        editTextTagId = (EditText) findViewById(R.id.tagIdField);
        editTextTagData = (EditText) findViewById(R.id.tagDataField);
    }


    public void onClickAdd (View btnAdd) {

        String tagName = editTextTagName.getText().toString();
        String tagId = editTextTagId.getText().toString();
        String tagData = editTextTagData.getText().toString();

        if ( tagName.length() != 0 && tagId.length() != 0 && tagData.length() != 0 ) {

            Intent newIntent = getIntent();
            newIntent.putExtra("tag_name", tagName);
            newIntent.putExtra("tag_id", tagId);
            newIntent.putExtra("tag_data", tagData);

            this.setResult(RESULT_OK, newIntent);

            finish();
        }
    }


    /**
    // Declare Variables
    private long rowID;
    private EditText mTagName;
    private EditText mTagId;
    private EditText mTagInfo;
    private static final String TAG_NAME = "tag_name";
    private static final String TAG_ID = "tag_id";
    private static final String TAG_INFO = "tag_info";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTagName = (EditText) findViewById(R.id.tagNameField);
        mTagId = (EditText) findViewById(R.id.tagIdField);
        mTagInfo = (EditText) findViewById(R.id.tagDataField);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rowID = extras.getLong("row_id");
            mTagName.setText(extras.getString(TAG_NAME));
            mTagId.setText(extras.getString(TAG_ID));
            mTagInfo.setText(extras.getString(TAG_INFO));
        }

    }

        // Create an ActionBar menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu){
            menu.add("Save Note")
                    .setOnMenuItemClickListener(this.SaveButtonClickListener)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            return super.onCreateOptionsMenu(menu);
        }

    // Capture save menu item click
    MenuItem.OnMenuItemClickListener SaveButtonClickListener = new MenuItem.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {

            // Passes the data into saveNote() function
            if (mTagName.getText().length() != 0) {
                AsyncTask<Object, Object, Object> saveNoteAsyncTask = new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        saveNote();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result) {
                        // Close this activity
                        finish();
                    }
                };
                // Execute the saveNoteAsyncTask AsyncTask above
                saveNoteAsyncTask.execute((Object[]) null);
            }

            else {
                // Display a simple alert dialog that forces user to put in a title
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        AddTagActivity.this);
                alert.setTitle("Title is required");
                alert.setMessage("Put in a title for this note");
                alert.setPositiveButton("Okay", null);
                alert.show();
            }

            return false;

        }
    };
    // saveNote() function
    private void saveNote() {
        DatabaseConnector dbConnector = new DatabaseConnector(this);

        if (getIntent().getExtras() == null) {
            // Passes the data to InsertNote in DatabaseConnector.java
            dbConnector.insertData(mTagName.getText().toString(), mTagId
                    .getText().toString(),mTagInfo.getText().toString()) ;
        } else {
            // Passes the Row ID and data to UpdateNote in DatabaseConnector.java
            try {
                dbConnector.updateTag(rowID, mTagName.getText().toString(), mTagId
                        .getText().toString(), mTagInfo.getText().toString()) ;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }**/


        /*findViewById(R.id.validButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagName = mTagName.getText().toString();
                String tagId = mTagId.getText().toString();
                String tagInfo = mTagInfo.getText().toString();

                if(tagName.length()!=0&&tagId.length()!=0&&tagInfo.length()!=0){
                    Intent newIntent = getIntent();
                    newIntent.putExtra("tag_tag_name", tagName);
                    newIntent.putExtra("tag_tag_id", tagId);
                    newIntent.putExtra("tag_tag_info", tagInfo);

                    setResult(RESULT_OK, newIntent);

                    finish();
                }
            }
        });*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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
    }*/
}
