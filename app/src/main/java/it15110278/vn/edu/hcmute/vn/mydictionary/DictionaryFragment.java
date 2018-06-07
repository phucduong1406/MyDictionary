package it15110278.vn.edu.hcmute.vn.mydictionary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DictionaryFragment extends Fragment {

    private String value = "Hello!";  // Toast
    private FragmentListener listener;  // Declare a variable for this listener in fragment
    ArrayAdapter<String> adapter;
    ListView dictList;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dictionary, container, false);
    }

    //
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Button btnDeail = view.findViewById(R.id.btnDeital);
//        btnDeail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null)
//                    listener.onItemClick(value);
//            }
//        });


        // Lấy danh sách dictionary
        dictList = view.findViewById(R.id.dictList);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getListOfWords());
        dictList.setAdapter(adapter);
        dictList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (listener != null)
                    listener.onItemClick(getListOfWords()
                            [position]);
            }
        });
    }

    // Reset data source
    public void resetDataSource(String[] source) {
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, source);
        dictList.setAdapter(adapter);
    }

    // Filter
    public void filterValue(String value) {

        //adapter.getFilter().filter(value);

        int size = adapter.getCount();
        for (int i = 0; i < size; i++) {
            if (adapter.getItem(i).startsWith(value)) {
                dictList.setSelection(i);
                break;
            }
        }
    }


    // Danh sách dictionary
    String[] getListOfWords() {
        String[] source = new String[]{
                "abandon",
                "abandonment",
                "ability",
                "able",
                "about",
                "above",
                "abroad",
                "absence",
                "absent",
                "absolute",
                "absolutely",
                "absorb",
                "abuse",
                "academic",
                "accent",
                "acceptable",
                "access",
                "accident",
                "accidental",
                "accidentally",
                "bid",
                "bill",
                "bind",
                "calculate"
        };
        return source;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //
    public void setOnFragmentListener(FragmentListener listener) {
        this.listener = listener;
    }

}
