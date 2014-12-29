package shoppinglist.kizema.anton.shoppinglist.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import shoppinglist.kizema.anton.shoppinglist.R;


public class PlaceholderFragmentShoppingList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView listView;
    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;

    // These are the Contacts rows that we will retrieve
    private static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};

    // This is the select criteria
    private static final String SELECTION = "((" +
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";


    public PlaceholderFragmentShoppingList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Create a progress bar to display while the list loads
        listView = (ListView) getActivity().findViewById(R.id.listView);

        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        listView.setEmptyView(progressBar);


        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) getActivity().findViewById(android.R.id.content);
        root.addView(progressBar);

        // For the cursor adapter, specify which columns go into which views
        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {R.id.name};

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new CustomSimpleCursorAdapter(getActivity(),
                R.layout.list_item, null,
                fromColumns, toViews, 0);
        listView.setAdapter(mAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);

    }

    private class CustomSimpleCursorAdapter extends SimpleCursorAdapter{

        public CustomSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView != null) {
                return super.getView(position, convertView, parent);
            }

            View view = super.getView(position, convertView, parent);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( getMaxWidth(), RelativeLayout.LayoutParams.WRAP_CONTENT);
            View textView = view.findViewById(R.id.name);
            textView.setLayoutParams(params);
            return view;
        }
    }

        private int getMaxWidth(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        return (int) ( width*0.75);
    }

//    private static class MyAdapter extends CursorAdapter
//    {
//        private LayoutInflater mInflater;
//        private Cursor cur;
//
//        public MyAdapter(Context context, Cursor c) {
//            super(context,c);
//            this.mInflater = LayoutInflater.from(context);
//            this.cur = c;
//        }
//        public MyAdapter(Context context, Cursor c, boolean autoRequery)
//        {
//            super(context, c, autoRequery);
//            this.mInflater = LayoutInflater.from(context);
//            this.cur = c;
//        }
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent)
//        {
//            ViewHolder viewHolder;
//            if(convertView == null)
//            {
//                convertView = this.mInflater.inflate(R.layout.list_item, null);
//                viewHolder = new ViewHolder();
//                viewHolder.name = (TextView)convertView.findViewById(R.id.name);
//                viewHolder.inShoppingList = (ImageView)convertView.findViewById(R.id.isInApp);
//                convertView.setTag(viewHolder);
//            }else
//            {
//                viewHolder = (ViewHolder)convertView.getTag();
//            }
//            this.cur.moveToPosition(position);
//
//            viewHolder.name.setText(this.cur.getString(this.cur.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)));
//
//            return convertView;
//        }
//
//        @Override
//        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//            return null;
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//
//        }
//        /* (non-Javadoc)
//         * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
//         */
////        @Override
////        public void bindView(View view, Context context, Cursor cursor) {
////            ViewHolder viewHolder = (ViewHolder)view.getTag();
////
////            viewHolder.name.setText(cursor.getColumnIndex(INDICATOR_NAME));
////
////        }
////
////        @Override
////        public View newView(Context context, Cursor cursor, ViewGroup parent) {
////            View view = this.mInflater.inflate(R.layout.list_item, null);
////
////            ViewHolder viewHolder = new ViewHolder();
////            viewHolder.name = (TextView) view.findViewById(R.id.name);
////            viewHolder.inShoppingList = (ImageView) view.findViewById(R.id.isInApp);
////            view.setTag(viewHolder);
////
////            return view;
////        }
//
//        private static class ViewHolder
//        {
//            TextView name;
//            ImageView inShoppingList;
//        }
//    }

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(getActivity(), ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }
}