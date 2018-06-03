package com.example.mg.todo.utils;

import android.content.Context;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mg.todo.R;

public class ActionBarCallBack implements Callback {

    private Context c;

    public ActionBarCallBack(Context context) {
        c = context;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        switch (item.getItemId()) {

            case R.id.delete:
                Toast.makeText(c, " Button Click !", Toast.LENGTH_LONG).show();
                mode.finish();
                return true;
            default:

                return false;
        }

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode arg0) {
        // OK action bar
        Toast.makeText(c, " ActionMode Status Finish !", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        mode.setTitle("items selected");
        return false;
    }

}