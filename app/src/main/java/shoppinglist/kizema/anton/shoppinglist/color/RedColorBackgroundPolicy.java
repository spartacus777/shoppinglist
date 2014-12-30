package shoppinglist.kizema.anton.shoppinglist.color;

import android.graphics.Color;
import android.view.View;

/**
 * Created by Anton on 30.12.2014.
 */
public class RedColorBackgroundPolicy implements FillBackgroundPolocy {
    @Override
    public void setHooveredBackground(View v) {
        v.setBackgroundColor(Color.RED);
    }

    @Override
    public void setNormalBackground(View v) {
        v.setBackgroundColor(Color.TRANSPARENT);
    }
}
