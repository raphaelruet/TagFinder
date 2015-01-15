package com.eseoteam.android.tagfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


public class AddTagActivity extends Activity {

    EditText editTextTagName;
    EditText editTextTagId;
    EditText editTextTagData;
    private ImageButton btnValid;

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

        btnValid = (ImageButton) findViewById(R.id.validButton);
        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagName = editTextTagName.getText().toString();
                String tagId = editTextTagId.getText().toString();
                String tagData = editTextTagData.getText().toString();

                if (tagName.length() != 0 && tagId.length() != 0 && tagData.length() != 0) {

                    Intent newIntent = getIntent();
                    newIntent.putExtra("tag_name", tagName);
                    newIntent.putExtra("tag_id", tagId);
                    newIntent.putExtra("tag_data", tagData);

                    setResult(RESULT_OK, newIntent);
                    finish();
                }
            }
        });
    }
}


    /*public void onClickAdd (View btnAdd) {

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
    }*/
    /*public void onClickEnterData() {

        ImageButton btnAdd = (ImageButton) findViewById(R.id.addButton);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), AddTagActivity.class)
                        ,ENTER_DATA_REQUEST_CODE);
            }
        });*/


