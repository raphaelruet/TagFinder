package com.eseoteam.android.tagfinder;

/**
 * Created by Charline LEROUGE on 08/01/15.
 */

import android.app.Fragment;

public class TagListFragment extends Fragment {

    /**private ArrayAdapter<String> mTagListAdapter;

    public TagListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_library,
                container, false);

        //Dummy data for the listView
       /* String[] tagArray = {
                "Tag 1 - Shoe",
                "Tag 2 - Key",
                "Tag 3 - Phone",
                "Tag 4 - Shoe",
                "Tag 5 - Key",
                "Tag 6 - Phone",
                "Tag 7 - Shoe",
                "Tag 8 - Key",
                "Tag 9 - Phone",
                "Tag 10 - Shoe",
                "Tag 11 - Key",
                "Tag 12 - Phone"
        };*/



        /*List<String> listTag = new ArrayList<String>(
                Arrays.asList(CustomCursorAdapter.KEY_NAME));*/

        //ArrayAdapter will take data from source to populate listView
        /**mTagListAdapter = new ArrayAdapter<String>(
                getActivity(),//current context
                R.layout.single_tag,//ID of list tag layout
                R.id.list_tag_textview, //ID of textview to populate
                new ArrayList<String>()); //list data

        // Get reference to the list view, and attach adapter
        ListView listView = (ListView) rootView.findViewById(R.id.listview_tag);
        listView.setAdapter(mTagListAdapter);
        mTagListAdapter.notifyDataSetChanged();

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String tagInfo = mTagListAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });*/

       // return rootView;
   // }
}



