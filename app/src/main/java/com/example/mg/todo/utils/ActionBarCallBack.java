package com.example.mg.todo.utils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mg.todo.R;

public class ActionBarCallBack implements ActionMode.Callback {

  /*  private Context c;

    ActionBarCallBack(Context context) {
        c = context;
    }*/

    @Override
    public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
        mode.setTitle("items selected");
        return true;
    }

    @Override
    public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {

            case R.id.delete:
                mode.finish();
                return true;
            default:

                return false;
        }
    }

    @Override
    public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {

    }
}