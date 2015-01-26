
package com.zony.playeggs;

import android.app.Activity;
import android.os.Bundle;

public class FlyingCatActivity extends Activity {

    private Board mBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBoard = new Board(this, null);
        mBoard.setEndListener(new AnimatorEndListener() {
            @Override
            public void onAnimatorEnd() {
                finish();
            }
        });
        setContentView(mBoard);
    }
}
