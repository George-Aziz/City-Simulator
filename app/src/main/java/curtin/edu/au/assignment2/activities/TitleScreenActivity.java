/*************************************************************************************************
 * Author: George Aziz
 * Date Created: 02/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: 1st Screen of the game that allows user to enter into Settings or start/continue game
 ************************************************************************************************/

package curtin.edu.au.assignment2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import curtin.edu.au.assignment2.models.GameData;
import curtin.edu.au.assignment2.R;

public class TitleScreenActivity extends AppCompatActivity
{
    private Button startBtn, settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        GameData gameData = GameData.get();
        gameData.gameStartUp(this); // starts the game

        startBtn = findViewById(R.id.startButton);
        settingsBtn = findViewById(R.id.settingsButton);

        startBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(TitleScreenActivity.this, MapScreenActivity.class));
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(TitleScreenActivity.this, SettingScreenActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        //Do nothing as this is first screen and we do not want to go back to Settings/Map Activities
    }
}