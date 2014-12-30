package shoppinglist.kizema.anton.shoppinglist;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import shoppinglist.kizema.anton.shoppinglist.color.RedColorBackgroundPolicy;
import shoppinglist.kizema.anton.shoppinglist.view.LocableScrollView;
import shoppinglist.kizema.anton.shoppinglist.view.MovableView;


public class EditActivity extends ActionBarActivity implements MovableView.OnScreenStateChangeListener {

    private LinearLayout ll;
    private LocableScrollView scroll;
    private Map<EditText, String> entries;
    private boolean isScrollable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ll = (LinearLayout) findViewById(R.id.llParent);
        scroll = (LocableScrollView) findViewById(R.id.scroll);
        enableScroll(false);
        entries = new HashMap<EditText, String>();

        EditText firstEditText = (EditText) findViewById(R.id.editText);
        addListener(firstEditText);
    }

    private void enableScroll(boolean isEnabled){
        scroll.setEnableScrolling(isEnabled);
    }

    private void addExtraView(){
        View child = getLayoutInflater().inflate(R.layout.edit_item, ll, false);
        ll.addView(child);
        EditText text = (EditText) child.findViewById(R.id.editText);
        addListener(text);
    }

    private void addListener(final EditText text){
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("TAG", "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TAG", "onTextChanged");

                if (text.getText().length() > 0) {
                    entries.put(text, text.getText().toString());
                } else {
                    entries.remove(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("TAG", "afterTextChanged");
            }
        });

        MovableView movableView = new MovableView(text);
        movableView.setFillBackgroundPolocy(new RedColorBackgroundPolicy());
        movableView.setOnScreenStateChangeListener(this);
    }

    private void log(){
        for (String v : entries.values()){
            Log.d("TAG", v);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_ok) {
            addExtraView();
            log();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRemove(View v) {
        ll.removeView((View) v.getParent());
        entries.remove(v);
    }
}
