package it15110278.vn.edu.hcmute.vn.mydictionary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class DetailFragment extends Fragment {

    private String value = "";

    private TextView textWord;
    private ImageButton btnBookmark, btnVolume;
    private WebView textWordTranslate;


    private DBHelper mDBHelper;
    private int mDicType;


    ImageButton btnHear;


    TextToSpeech toSpeech;
    int flagLang;  // cờ chọn từ điển E-V, V-E

    public DetailFragment() {
        // Required empty public constructor
    }

    //
    public static DetailFragment getNewInstance(String value, DBHelper dbHelper, int dicType) {
        DetailFragment fragment = new DetailFragment();
        fragment.value = value;
        fragment.mDBHelper = dbHelper;
        fragment.mDicType = dicType;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    //
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnHear = view.findViewById(R.id.btnVolume);
        textWord = view.findViewById(R.id.textWord);
        btnBookmark = view.findViewById(R.id.btnBookmark);
        btnVolume = view.findViewById(R.id.btnVolume);
        textWordTranslate = view.findViewById(R.id.textWordTranslate);


        final Word word = mDBHelper.getWord(value, mDicType);
        textWord.setText(word.key);
        textWordTranslate.loadDataWithBaseURL(null, word.value, "text/html", "utf-8", null);

        Word bookmarkWord = mDBHelper.getWordFromBookmark(value);
        int isMark = bookmarkWord == null ? 0 : 1;
        btnBookmark.setTag(isMark);

        // Set icon
        int icon = bookmarkWord == null ? R.drawable.ic_bookmark_border : R.drawable.ic_bookmark_fill;
        btnBookmark.setImageResource(icon);

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) btnBookmark.getTag();
                if (i == 0) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_fill);
                    btnBookmark.setTag(1);
                    mDBHelper.addBookmark(word);
                } else if (i == 1) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_border);
                    btnBookmark.setTag(0);
                    mDBHelper.deleteBookmark(word);
                }
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

}
