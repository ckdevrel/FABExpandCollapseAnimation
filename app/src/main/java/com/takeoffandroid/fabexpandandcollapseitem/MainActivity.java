package com.takeoffandroid.fabexpandandcollapseitem;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private FrameLayout frameLayout;

    private FloatingActionButton mainImageButton;

    private RotateAnimation rotateOpenAnimation;

    private RotateAnimation rotateCloseAnimation;

    private ArrayList<FloatingActionButton> promotedActions;

    ObjectAnimator objectAnimator[];

    private int SPACING_BETWEEN_BUTTONS;

    private static final int ANIMATION_TIME = 100;

    private boolean isMenuOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.main);

         frameLayout = (FrameLayout) findViewById(R.id.container);

                promotedActions = new ArrayList<FloatingActionButton> ();
        
                SPACING_BETWEEN_BUTTONS = (int) this.getResources ().getDimension(R.dimen.dim56dp) + 20;
                openRotation();
                closeRotation();
        
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText (getApplicationContext (), "Button clicked.", Toast.LENGTH_SHORT).show();
            }
        };

        addItem(getResources().getDrawable(android.R.drawable.ic_menu_edit), onClickListener);
        addItem(getResources().getDrawable(android.R.drawable.ic_menu_send), onClickListener);
        addItem(getResources().getDrawable(android.R.drawable.ic_input_get), onClickListener);

        addMainItem(getResources().getDrawable(R.drawable.ic_add));
    }

    private void openRotation() {

        rotateOpenAnimation = new RotateAnimation(0, 45, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateOpenAnimation.setFillAfter (true);
        rotateOpenAnimation.setFillEnabled (true);
        rotateOpenAnimation.setDuration (ANIMATION_TIME);
    }

    private void closeRotation() {

        rotateCloseAnimation = new RotateAnimation(45, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateCloseAnimation.setFillAfter (true);
        rotateCloseAnimation.setFillEnabled (true);
        rotateCloseAnimation.setDuration (ANIMATION_TIME);
    }

    public void addItem(Drawable drawable, View.OnClickListener onClickListener) {

        FloatingActionButton button = (FloatingActionButton) LayoutInflater.from (MainActivity.this).inflate(R.layout.include_fab, frameLayout, false);

        button.setImageDrawable (drawable);
        button.setOnClickListener (onClickListener);

        promotedActions.add (button);

        frameLayout.addView (button);

    }

    public FloatingActionButton addMainItem(Drawable drawable) {

        View view =  LayoutInflater.from(MainActivity.this).inflate(R.layout.include_fab, frameLayout, false);

        FloatingActionButton imgFab = (FloatingActionButton)view.findViewById (R.id.fab);
        imgFab.setVisibility (View.VISIBLE);
        imgFab.setImageDrawable (drawable);

        imgFab.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {

                if(isMenuOpened) {
                    closePromotedActions ().start ();
                    isMenuOpened = false;
                } else {
                    isMenuOpened = true;
                    openPromotedActions ().start ();
                }
            }
        });

        frameLayout.addView (imgFab);

        mainImageButton = imgFab;

        return imgFab;
    }

    public AnimatorSet closePromotedActions() {

        if (objectAnimator == null){
            objectAnimatorSetup();
        }

        AnimatorSet animation = new AnimatorSet();

        for (int i = 0; i < promotedActions.size(); i++) {

            objectAnimator[i] = setCloseAnimation(promotedActions.get(i), i);
        }

        if (objectAnimator.length == 0) {
            objectAnimator = null;
        }

        animation.playTogether (objectAnimator);
        animation.addListener (new Animator.AnimatorListener () {
            @Override
            public void onAnimationStart (Animator animator) {
                mainImageButton.startAnimation (rotateCloseAnimation);
                mainImageButton.setClickable (false);
            }

            @Override
            public void onAnimationEnd (Animator animator) {
                mainImageButton.setClickable (true);
                hidePromotedActions ();
            }

            @Override
            public void onAnimationCancel (Animator animator) {
                mainImageButton.setClickable (true);
            }

            @Override
            public void onAnimationRepeat (Animator animator) {
            }
        });

        return animation;
    }

    public AnimatorSet openPromotedActions() {

        if (objectAnimator == null){
            objectAnimatorSetup();
        }



        AnimatorSet animation = new AnimatorSet();

        for (int i = 0; i < promotedActions.size(); i++) {

            objectAnimator[i] = setOpenAnimation (promotedActions.get (i), i);
        }

        if (objectAnimator.length == 0) {
            objectAnimator = null;
        }

        animation.playTogether (objectAnimator);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mainImageButton.startAnimation(rotateOpenAnimation);
                mainImageButton.setClickable (false);
                showPromotedActions ();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mainImageButton.setClickable (true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mainImageButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });


        return animation;
    }

    private void objectAnimatorSetup() {

        objectAnimator = new ObjectAnimator[promotedActions.size()];
    }

    private void hidePromotedActions() {

        for (int i = 0; i < promotedActions.size(); i++) {
            promotedActions.get(i).setVisibility(View.GONE);
        }

    }

    private void showPromotedActions() {

        for (int i = 0; i < promotedActions.size(); i++) {
            promotedActions.get(i).setVisibility(View.VISIBLE);
        }
    }

    private ObjectAnimator setOpenAnimation(FloatingActionButton promotedAction, int position) {

        ObjectAnimator objectAnimator;

        if(MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, "translationY", 0f, -SPACING_BETWEEN_BUTTONS * (promotedActions.size() - position));
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));

        } else {
            objectAnimator = ObjectAnimator.ofFloat(promotedAction, "translationX", 0f, -SPACING_BETWEEN_BUTTONS * (promotedActions.size() - position));
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        }

        return objectAnimator;
    }

    private ObjectAnimator setCloseAnimation(FloatingActionButton promotedAction, int position) {

        ObjectAnimator objectAnimator;

        if(MainActivity.this.getResources ().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, "translationY", -SPACING_BETWEEN_BUTTONS * (promotedActions.size() - position), 0f);
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));

        } else {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, "translationX", -SPACING_BETWEEN_BUTTONS * (promotedActions.size() - position), 0f);
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        }

        return objectAnimator;
    }

}
