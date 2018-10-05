package ie.swayne.ilicsoundboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;
import java.io.Serializable;

interface OnButtonActionListener {
    public void onButtonDown(View view);
    public void onButtonUp(View view);
}

public class SoundButton extends AppCompatButton implements Serializable {

    private String soundPath;
    private int slow = -1;
    private String imgPath;

    private String text;

    public SoundButton(Context context, String soundPath) {
        super(context);
        this.soundPath = soundPath;

        this.setHeight(400);
        this.setWidth(400);
        this.setTextColor(Color.parseColor("#ffffff"));
    }

    public void setImage(Drawable d) {
        this.imgPath = imgPath;
        this.setBackground(d);
    }

    public String getPath() {
        return soundPath;
    }

    public SoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //Flips everytime it is called, returns 1 when button is clicked for the first time and -1 when clicked for the second time (lets the music player know to play it in slow motion)
    public int returnState() {
        slow = -slow;
        return slow;
    }


    public void setOnButtonActionListener(OnButtonActionListener listener) {
        buttonListener = listener;
    }

    protected OnButtonActionListener buttonListener;
}
