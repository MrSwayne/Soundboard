package ie.swayne.ilicsoundboard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MyFragment extends Fragment {

    public static final String ARG_PAGE = "arg_page";
    public static final String ARG_NAME = "arg_name";
    public static final String ARG_PATH = "arg_path";

    static DisplayMetrics displayMetrics;

    private String imgPath;
    private ArrayList<SoundButton> buttons = new ArrayList<SoundButton>();
    private RelativeLayout relativeLayout;
    Button button;
    private int numChildren = 0;

    private SoundFolder folder;

    public void setFolder(SoundFolder folder) {
        this.folder = folder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static MyFragment newInstance(int pageNumber, String name) {
        MyFragment myFragment = new MyFragment();
        Bundle args = new Bundle();
        myFragment.setRetainInstance(true);
        args.putInt(ARG_PAGE, pageNumber);
        args.putString(ARG_NAME, name);
      //  args.putString(ARG_PATH, imgPath);

        displayMetrics = new DisplayMetrics();
        myFragment.setArguments(args);

        return myFragment;
    }

    public void addButton(SoundButton button) {
       // button.setImage(new Bundle().getString(ARG_PATH));
        buttons.add(button);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Bundle args = getArguments();

        if(buttons == null)
         buttons = (ArrayList<SoundButton>) args.getSerializable("buttons");

        int pageNumber = args.getInt(ARG_PAGE);

        View view = inflater.inflate(R.layout.fragment_my, container, false);




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle args = getArguments();
        String msg = args.getString(ARG_NAME);

        if(buttons == null)
         buttons = (ArrayList<SoundButton>) args.getSerializable("buttons");

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);



        SoundButton rowAnchorButton = buttons.get(0) ;

        int NUM_OF_COLS = 4;


        ArrayList<TextView> views = new ArrayList<TextView>();

        for(int i = 0;i < buttons.size();i++) {

            TextView tv = new TextView(this.getContext());
            tv.setText(buttons.get(i).getText());
            views.add(tv);

            relativeLayout = getView().findViewById(R.id.linearLayout);
            buttons.get(i).setId(2000 + i);
            buttons.get(i).setTextSize(0);

            views.get(i).setId(1000 + i);
            views.get(i).setGravity(Gravity.CENTER);

            RelativeLayout.LayoutParams r1p2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            RelativeLayout.LayoutParams r2p2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            r1p2.height = (displayMetrics.widthPixels - (20 * NUM_OF_COLS - 1)) / NUM_OF_COLS;//(displayMetrics.heightPixels - 120) / (NUM_OF_COLS + 1);
            r1p2.width = (displayMetrics.widthPixels - (20 * NUM_OF_COLS - 1)) / NUM_OF_COLS;
            r1p2.setMargins(0,20,20,20);
            r2p2.height = 100;
            r2p2.setMargins(0,0,20,20);
            r2p2.width = r1p2.width;


            if(i == 0) {
                r1p2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                r1p2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                r2p2.addRule(RelativeLayout.BELOW, buttons.get(i).getId());

            } else {
                if(i % NUM_OF_COLS == 0)
                    r1p2.addRule(RelativeLayout.BELOW, views.get(i - NUM_OF_COLS).getId());
                else {
                    r1p2.addRule(RelativeLayout.RIGHT_OF, buttons.get(i - 1).getId());
                    r2p2.addRule(RelativeLayout.RIGHT_OF, views.get(i - 1).getId());

                    if(i > NUM_OF_COLS)
                    r1p2.addRule(RelativeLayout.BELOW, views.get(i - NUM_OF_COLS).getId());
                }
            }


            r2p2.addRule(RelativeLayout.BELOW, buttons.get(i).getId());


            tv.setLayoutParams(r2p2);
            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER);
            buttons.get(i).setLayoutParams(r1p2);
            relativeLayout.addView(buttons.get(i));
            relativeLayout.addView(tv);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(relativeLayout != null)
            relativeLayout.removeAllViews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("buttons", buttons);
    }
}
