package ie.swayne.ilicsoundboard;


import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.support.design.widget.TabLayout;


import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/*
    TODO

    - Replace category textviews with fragments
    - Update UI
    - Add a settings menu

 */


public class MainActivity extends AppCompatActivity {

    public static final String MSG = "Swayne.ILIC_Soundboard:";

    private AssetManager assetManager;
    private MediaPlayer mp;
    private AssetFileDescriptor afd;
    private ArrayList<SoundFolder> soundFolders;
    private ArrayList<ImageFolder> imageFolders;
    private LinearLayout linLay;
    private MyFragment[] fragments;
    private ViewPager viewPager;
    private String[] audioFolderNames;
    private String[] imageFolderNames;

    private PagerAdapter adapter;

    //Set this to whatever folder your sub-folders are in inside the asset folders
    //For example, an example path of an mp3 file is Assets/audio/Swayne/noise1.mp3
    //Swayne would be equivalent to the SoundFolder class
    //noise1.mp3 would be equivalent to the SoundFile class

    public static  String AUDIO_FOLDER = "audio";
    public static String IMAGE_FOLDER = "images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        soundFolders = new ArrayList<SoundFolder>();
        imageFolders = new ArrayList<ImageFolder>();
       // linLay = findViewById(R.id.container_layout);

        linLay = new LinearLayout(this);
        linLay.setVerticalScrollBarEnabled(true);

        TabLayout tabLay = findViewById(R.id.tab_layout);

        tabLay.setTabMode(TabLayout.MODE_SCROLLABLE);



        try {

            //Get assets from folder
            assetManager = getAssets();

            //Return list of folders inside the audio folder
            audioFolderNames = assetManager.list(AUDIO_FOLDER);
            imageFolderNames = assetManager.list(IMAGE_FOLDER);




            for(int i = 0;i < imageFolderNames.length;i++) {
                String[] fileNames = assetManager.list(IMAGE_FOLDER + "/" + imageFolderNames[i]);
                ImageFolder imf = new ImageFolder(imageFolderNames[i]);

                for(int j = 0;j < fileNames.length;j++) {
                    imf.add(new ImageFile(fileNames[j],IMAGE_FOLDER + "/" + imageFolderNames[i] + "/" + fileNames[j]));
                }
                imageFolders.add(imf);
            }


            //Turn the physical audio folder into an object-oriented setup
            for(int i = 0; i < audioFolderNames.length; i++) {
                tabLay.addTab(tabLay.newTab().setText(audioFolderNames[i]));

                //Get the file names of each mp3 inside each folder
                String[] fileNames = assetManager.list(AUDIO_FOLDER + "/" + audioFolderNames[i]);

                //Create a virtual sound folder that represents the physical folder
                SoundFolder sf = new SoundFolder(audioFolderNames[i]);

                //for each folder, create a sound file that is then inserted into that folder
                for(int j = 0;j < fileNames.length;j++) {
                    sf.add(new SoundFile(fileNames[j], AUDIO_FOLDER + "/" + audioFolderNames[i] + "/" + fileNames[j]));
                }

                //Add all the folders to an arraylist of folders
                soundFolders.add(sf);
            }
        } catch (IOException e) {
            //This should only happen if I delete the audio files lol
            Log.e(MSG, e.getMessage());
        }



        viewPager = findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLay.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLay));
        tabLay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i(MSG, "" + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragments = new MyFragment[soundFolders.size()];

        //Create corresponding buttons for each folder
        for(int x = 0; x < soundFolders.size();x++) {
            SoundButton[] buttons = new SoundButton[soundFolders.get(x).size()];


            MyFragment frag = MyFragment.newInstance(x, soundFolders.get(x).getFolderName());
            fragments[x] = frag;

            frag.setFolder(soundFolders.get(x));
            adapter.addFragment(frag, soundFolders.get(x).getFolderName());

            TextView tv = new TextView(this);
            tv.setText(audioFolderNames[x]);
            tv.setTextSize(28);
           // linLay.addView(tv);

            //Programatically adds the buttons to the LinearLayout
            for (int i = 0; i < buttons.length; i++) {
                buttons[i] = new SoundButton(this, soundFolders.get(x).get(i).getPath());

                buttons[i].setText(soundFolders.get(x).get(i).getName());
                buttons[i].setOnClickListener(ButtonClickListener);


                try {
                    Drawable d = Drawable.createFromStream(getAssets().open(imageFolders.get(x).get(0).getPath()), null);
                    buttons[i].setImage(d);
                } catch(Exception e) {
                    Log.i("MSG", "IO EXCEPTION");
                }
                buttons[i].setWidth(90);

                fragments[x].addButton(buttons[i]);
                Space space = new Space(this);
                space.setMinimumHeight(5);
               // linLay.addView(space);
                //linLay.addView(buttons[i]);
            }
        }

    }

    //Called when a button is clicked, plays the corresponding path sent to it, if state is < 0 then it is played in slow motion
    private void playSound(String path, int state) {

        PlaybackParams pbp = new PlaybackParams();

     //   if(state < 0)
       //  pbp.setSpeed(0.5f);

        //If another button is clicked, then get rid of the current MediaPlayer.
        if(mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
        }

        //Create a MediaPlayer
        try {
            afd = assetManager.openFd(path);
            mp = new MediaPlayer();
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.setPlaybackParams(pbp);
            mp.start();
        } catch(IOException e) {
            Log.e(MSG, e.getMessage());
        }
    }

    //When clicked call the playSound() method
    private View.OnClickListener ButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SoundButton button = (SoundButton) v;
            int state = button.returnState();
            playSound(((SoundButton) v).getPath(), state);
        }
    };
}


