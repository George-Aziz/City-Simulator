/*****************************************************************************************************************
 * Author: George Aziz
 * Date Created: 07/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible of handing MapDetails Fragment which is fragment between map and structure selector
 ****************************************************************************************************************/

package curtin.edu.au.assignment2.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import curtin.edu.au.assignment2.models.GameData;
import curtin.edu.au.assignment2.R;
import curtin.edu.au.assignment2.URLException;

public class MapDetailsFragment extends Fragment
{
    private static GameData data;
    private static boolean demolishToggle, detailsToggle, collapseToggle;
    private static int activeColour, unActiveColour;
    private ImageView expandBtn;
    private static Button demolishBtn, detailsBtn, timeStepBtn;
    private static TextView timeValue, tempValue, balance, popValue, empRate, cityName, incomeVal;
    private ConstraintLayout statsDisplay;

    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.fragment_stats, ui, false);

        //Class fields Initialisation
        data = GameData.get();
        demolishToggle = false;
        detailsToggle = false;
        collapseToggle = true;

        //Colours for Button Activated/Deactivated feedback
        activeColour = getResources().getColor(R.color.colorActive);
        unActiveColour = getResources().getColor(R.color.colorUnActive);

        //Fragment Buttons
        demolishBtn = (Button) view.findViewById(R.id.demolishBtn);
        detailsBtn = (Button) view.findViewById(R.id.detailsBtn);
        timeStepBtn = (Button) view.findViewById(R.id.timeBtn);
        expandBtn = (ImageView) view.findViewById(R.id.arrowBtn);

        //Fragment stats view
        timeValue = (TextView) view.findViewById(R.id.curTime);
        tempValue = (TextView) view.findViewById(R.id.tempValue);
        balance = (TextView) view.findViewById(R.id.balValue);
        popValue = (TextView) view.findViewById(R.id.popVal);
        empRate = (TextView) view.findViewById(R.id.empValue);
        cityName = (TextView) view.findViewById(R.id.cityName);
        incomeVal = (TextView) view.findViewById(R.id.incomeVal);
        timeValue = (TextView) view.findViewById(R.id.timeValue);

        //Expanded Stats Constraint Layout
        statsDisplay = (ConstraintLayout) view.findViewById(R.id.statsLayout);
        statsDisplay.setVisibility(View.GONE); //By Default, the fragment is collapsed

        updateStats(getActivity());

        demolishBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Only one button at a time can be activated
                if (demolishToggle == true)
                {
                    demolishToggle = false;
                    ViewCompat.setBackgroundTintList(demolishBtn, ColorStateList.valueOf(unActiveColour));
                }
                else
                {
                    demolishToggle = true;
                    detailsToggle = false;
                    ViewCompat.setBackgroundTintList(demolishBtn, ColorStateList.valueOf(activeColour));
                    ViewCompat.setBackgroundTintList(detailsBtn, ColorStateList.valueOf(unActiveColour));
                }
            }
        });

        detailsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Only one button at a time can be activated
                if(detailsToggle == true)
                {
                    detailsToggle = false;
                    ViewCompat.setBackgroundTintList(detailsBtn, ColorStateList.valueOf(unActiveColour));
                }
                else
                {
                    detailsToggle = true;
                    demolishToggle = false;
                    ViewCompat.setBackgroundTintList(detailsBtn, ColorStateList.valueOf(activeColour));
                    ViewCompat.setBackgroundTintList(demolishBtn, ColorStateList.valueOf(unActiveColour));
                }
            }
        });

        timeStepBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Increment Time Value
                data.timeStep();
                updateStats(getActivity());
            }
        });

        expandBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(!collapseToggle)
                {
                    statsDisplay.setVisibility(View.GONE);
                    expandBtn.setRotation(180);
                    collapseToggle = true;
                }
                else
                {
                    statsDisplay.setVisibility(View.VISIBLE);
                    expandBtn.setRotation(0);
                    collapseToggle = false;
                }
            }
        });


        return view;
    }

    //Method that updates all TextViews for stats
    public static void updateStats(Context context)
    {
        timeValue.setText(String.valueOf(data.getGameTime()));
        try
        {
            data.updateTemperature();
        }
        catch (URLException e) { }

        String temperature = data.getTemperature();
        if (temperature == null)
        {
            tempValue.setText("N/A");
        }
        else
        {
            tempValue.setText((temperature +  "°C"));
        }

        double employVal = data.getEmploymentRate();
        if(employVal == -1)
        {
            empRate.setText("N/A");
        }
        else
        {
            empRate.setText(String.valueOf(employVal));
        }

        double income = data.getIncome();
        if(income == 0.0)
        {
            incomeVal.setText("$0.0"); //Normally displays '$-0.0'
        }
        else
        {
            incomeVal.setText(("$" + data.getIncome()));
        }

        balance.setText(String.valueOf(data.getMoney()));
        if(data.getMoney() < 0)
        {
            Toast toast = Toast.makeText(context,"Game Over: You are now Bankrupt!\nTo start a new game, go back to settings screen and create a new game", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, toast.getXOffset(), toast.getYOffset());
            toast.show();
        }
        popValue.setText(String.valueOf(data.getPopulation()));
        cityName.setText(data.getGameSettings().getCityName());
        data.updateGameData();
    }

    //Button Methods to be used outside of this fragment
    public static boolean getDemolishBtnPress()
    {
        ViewCompat.setBackgroundTintList(demolishBtn, ColorStateList.valueOf(unActiveColour));
        return demolishToggle;
    }

    public static boolean getDetailsBtnPress()
    {
        ViewCompat.setBackgroundTintList(detailsBtn, ColorStateList.valueOf(unActiveColour));
        return detailsToggle;
    }

    public static void setDemolishFalse()
    {
        demolishToggle = false;
    }

    public static void setDetailsFalse()
    {
        detailsToggle = false;
    }
}
