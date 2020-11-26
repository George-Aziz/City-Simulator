/************************************************************************************************
 * Author: George Aziz
 * Date Created: 02/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Activity that allows user to change settings of game and create new game if confirmed
 ************************************************************************************************/

package curtin.edu.au.assignment2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import curtin.edu.au.assignment2.R;
import curtin.edu.au.assignment2.models.GameData;
import curtin.edu.au.assignment2.models.GameSettings;

public class SettingScreenActivity extends AppCompatActivity
{
    private Button confirmBtn;
    private EditText mWidthInput, mHeightInput, initMoneyInput, serviceCostInput, houseCostInput,
            commCostInput, roadCostInput, cityNameInput;
    private String ogMWidth, ogMHeight, ogInitMoney, ogServiceCost, ogHouseCost, ogCommCost,
            ogRoadCost, ogCityName;

    private GameSettings gameSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        //Find All Views on this activity
        mWidthInput = findViewById(R.id.mapWidthVal);
        mHeightInput = findViewById(R.id.mapHeightVal);
        initMoneyInput = findViewById(R.id.initMoneyVal);
        serviceCostInput = findViewById(R.id.serviceCostVal);
        houseCostInput = findViewById(R.id.houseCostVal);
        commCostInput = findViewById(R.id.commCostVal);
        roadCostInput = findViewById(R.id.roadCostVal);
        cityNameInput = findViewById(R.id.cityNameIn);
        confirmBtn = findViewById(R.id.confirmBtn);

        // Displays in EditText to the user the current settings of the game
        gameSettings = GameSettings.get();
        ogMWidth = String.valueOf(gameSettings.getMapWidth());
        ogMHeight = String.valueOf(gameSettings.getMapHeight());
        ogInitMoney = String.valueOf(gameSettings.getInitialMoney());
        ogServiceCost = String.valueOf(gameSettings.getServiceCost());
        ogHouseCost = String.valueOf(gameSettings.getHouseBuildingCost());
        ogCommCost = String.valueOf(gameSettings.getCommBuildingCost());
        ogRoadCost = String.valueOf(gameSettings.getRoadBuildingCost());
        ogCityName = gameSettings.getCityName();

        mWidthInput.setText(ogMWidth);
        mHeightInput.setText(ogMHeight);
        initMoneyInput.setText(ogInitMoney);
        serviceCostInput.setText(ogServiceCost);
        houseCostInput.setText(ogHouseCost);
        commCostInput.setText(ogCommCost);
        roadCostInput.setText(ogRoadCost);
        cityNameInput.setText(ogCityName);


        confirmBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //If All the inputs are valid then proceed to assign new values to game settings
                if(validate("Map Width", ogMWidth, mWidthInput, 3, 100) && //Limited to 3 and 100 since 3 allows one structure and 100 has been tested and works
                validate("Map Height", ogMHeight, mHeightInput,3, 10) && // Limited to 3 - 10 as 3 allows one structure to be placed and 10 the max for landscape
                validate("Initial Money", ogInitMoney, initMoneyInput, 100, 100000) &&
                validate("Service Cost", ogServiceCost, serviceCostInput, 0,100000) &&
                validate("House Building Cost", ogHouseCost, houseCostInput, 0,100000) &&
                validate("Commercial Building Cost", ogCommCost, commCostInput, 0,100000) &&
                validate("Road Building Cost", ogRoadCost, roadCostInput, 0,100000) &&
                validateCityName(ogCityName,cityNameInput,17)) // 17 character long max or else city name will go under temperature view
                {
                    gameSettings.setMapWidth(Integer.parseInt(mWidthInput.getText().toString()));
                    gameSettings.setMapHeight(Integer.parseInt(mHeightInput.getText().toString()));
                    gameSettings.setInitialMoney(Integer.parseInt(initMoneyInput.getText().toString()));
                    gameSettings.setServiceCost(Integer.parseInt(serviceCostInput.getText().toString()));
                    gameSettings.setHouseBuildingCost(Integer.parseInt(houseCostInput.getText().toString()));
                    gameSettings.setCommBuildingCost(Integer.parseInt(commCostInput.getText().toString()));
                    gameSettings.setRoadBuildingCost(Integer.parseInt(roadCostInput.getText().toString()));
                    gameSettings.setCityName(cityNameInput.getText().toString());

                    GameData game = GameData.get();
                    game.restartGame(); //Restarts game whenever button is clicked to start new game
                    startActivity(new Intent(SettingScreenActivity.this, MapScreenActivity.class));
                }
            }
        });
    }

    //Validates Integers which are all the EditTexts in this view except for City Name
    public boolean validate(String editTextType, String originalString, EditText curEditText, int min, int max)
    {
        boolean valid = false;

        if(curEditText.getText().toString().isEmpty()) // If the editText is empty then it is invalid
        {
            curEditText.setText(originalString);
            Toast.makeText(getApplicationContext(),"Error with " + editTextType + " input, please ensure value is in range of " + min + " - " + max,Toast.LENGTH_SHORT).show();
        }
        else // If EditText not empty
        {
            try
            {
                int newInputVal = Integer.parseInt(curEditText.getText().toString());
                if(newInputVal > max || newInputVal < min) // If value not in range provided
                {
                    curEditText.setText(originalString);
                    Toast.makeText(getApplicationContext(),"Error with " + editTextType + " input, please ensure value is in range of " + min + " - " + max,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    valid = true;
                }
            }
            catch (NumberFormatException e)
            {
                curEditText.setText(originalString);
                Toast.makeText(getApplicationContext(),"Error with " + editTextType + " input, please ensure value is in range of " + min + " - " + max,Toast.LENGTH_SHORT).show();
            }

        }
        return valid;
    }

    //Validates City Name EditText input to make sure it is not empty and within range of max characters limit
    public boolean validateCityName(String originalString, EditText curEditText, int max)
    {
        boolean valid = false;
        if(curEditText.getText().toString().isEmpty()) // If the editText is empty then it is invalid
        {
            curEditText.setText(originalString);
            Toast.makeText(getApplicationContext(),"Error with city name input, please ensure the city name is not empty", Toast.LENGTH_LONG).show();
        }
        else // If EditText not empty
        {
            String newCityName = curEditText.getText().toString();
            if(newCityName.length() > max) // If value larger than max limit
            {
                curEditText.setText(originalString);
                Toast.makeText(getApplicationContext(),"Error with city name input, please ensure the city name is less than "+ (max+1) + " characters long", Toast.LENGTH_LONG).show();
            }
            else
            {
                valid = true;
            }
        }
        return valid;
    }

    //Goes back to Title Screen but starts new activity and not default back button to prevent issues
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(SettingScreenActivity.this, TitleScreenActivity.class));
    }
}