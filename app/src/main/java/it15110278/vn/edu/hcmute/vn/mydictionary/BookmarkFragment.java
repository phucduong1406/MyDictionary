package it15110278.vn.edu.hcmute.vn.mydictionary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BookmarkFragment extends Fragment {

    private FragmentListener listener;  // Declare a variable for this listener in fragment

    private DBHelper mDBHelper;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    public static BookmarkFragment getNewIntance(DBHelper dbHelper) {
        BookmarkFragment fragment = new BookmarkFragment();
        fragment.mDBHelper = dbHelper;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    //
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //
        setHasOptionsMenu(true);

        // Lấy danh sách bookmark
        ListView bookmarkList = view.findViewById(R.id.bookmarkList);
        final BookmarkAdapter adapter = new BookmarkAdapter(getActivity(), mDBHelper.getAllWordFromBookmark());
        bookmarkList.setAdapter(adapter);

        adapter.setOnItemClick(new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                if (listener != null)
                    listener.onItemClick(String.valueOf(adapter.getItem(position)));
            }
        });


        // Xóa từ trong danh sách bookmark
        adapter.setOnItemDeleteClick(new ListItemListener() {

            //String value = String.valueOf(adapter.getItem(position));
            //Toast.makeText(getContext(), value + " item is deleted", Toast.LENGTH_SHORT).show();

            @Override
            public void onItemClick(int position) {
                adapter.removeItem(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // Set the listener inside the fragment
    public void setOnFragmentListener(FragmentListener listener) {
        this.listener = listener;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_clear, menu);
    }
}
