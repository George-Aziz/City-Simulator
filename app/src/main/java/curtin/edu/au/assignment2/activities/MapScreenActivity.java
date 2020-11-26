/************************************************************************************
 * Author: George Aziz
 * Date Created: 02/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Activity that displays the Game Map with Structure Selector & Game Stats
 ************************************************************************************/

package curtin.edu.au.assignment2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.zip.Inflater;

import curtin.edu.au.assignment2.R;
import curtin.edu.au.assignment2.fragments.MapDetailsFragment;
import curtin.edu.au.assignment2.fragments.MapFragment;
import curtin.edu.au.assignment2.fragments.SelectorFragment;

public class MapScreenActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFrag = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFrag == null)
        {
            mapFrag = new MapFragment();
            fm.beginTransaction().add(R.id.map, mapFrag).commit();
        }

        MapDetailsFragment statsFrag = (MapDetailsFragment) fm.findFragmentById(R.id.stats);
        if (statsFrag == null)
        {
            statsFrag = new MapDetailsFragment();
            fm.beginTransaction().add(R.id.stats, statsFrag).commit();
        }

        SelectorFragment selectFrag = (SelectorFragment) fm.findFragmentById(R.id.selector);
        if (selectFrag == null)
        {
            selectFrag = new SelectorFragment();
            fm.beginTransaction().add(R.id.selector, selectFrag).commit();
        }
    }

    //Goes back to Title Screen but starts new activity and not default back button to prevent issues
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(MapScreenActivity.this, TitleScreenActivity.class));
    }
}