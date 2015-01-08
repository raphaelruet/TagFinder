package com.eseoteam.android.tagfinder;

/**
 * Created by Charline LEROUGE on 08/01/15.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagListFragment extends Fragment {

    private ArrayAdapter<String> mTagListAdapter;

    public TagListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_library,
                container, false);

        //Dummy data for the listView
        String[] tagArray = {
                "Tag 1 - Shoe",
                "Tag 2 - Key",
                "Tag 3 - Phone",
                "Tag 1 - Shoe",
                "Tag 2 - Key",
                "Tag 3 - Phone",
                "Tag 1 - Shoe",
                "Tag 2 - Key",
                "Tag 3 - Phone",
                "Tag 1 - Shoe",
                "Tag 2 - Key",
                "Tag 3 - Phone"
        };

        List<String> listTag = new ArrayList<String>(
                Arrays.asList(tagArray));

        //ArrayAdapter will take data from source to populate listView
        mTagListAdapter = new ArrayAdapter<String>(
                getActivity(),//current context
                R.layout.list_tag,//ID of list tag layout
                R.id.list_tag_textview, //ID of textview to populate
                listTag); //list data
        // Get reference to the list view, and attach adapter
        ListView listView = (ListView) rootView.findViewById(R.id.listview_tag);
        listView.setAdapter(mTagListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String tagInfo = mTagListAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), InfoTagActivity.class)
                        .putExtra(Intent.EXTRA_TEXT,tagInfo);
                startActivity(intent);
            }
        });

        return rootView;
    }
}



