/************************************************************************
 * Author: George Aziz
 * Date Created: 02/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible for creating and storing the game data
 ***********************************************************************/
/* How to do API Call for temperature taken from MAD Lecture 6: Remote Data */
package curtin.edu.au.assignment2.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import curtin.edu.au.assignment2.URLException;
import curtin.edu.au.assignment2.databases.DBSchemas.GameDataTable;
import curtin.edu.au.assignment2.databases.GameDataCursor;
import curtin.edu.au.assignment2.databases.GameDataDBHelper;

import javax.net.ssl.HttpsURLConnection;

public class GameData
{
    private GameSettings gameSettings;

    private static GameData instance = null;
    private SQLiteDatabase db;
    private GameDataCursor cursor;
    private final int id = 1;
    private int money;
    private int gameTime;
    private int nCommercial;
    private int nResidential;
    private int population;
    private String temperature;

    private GameData() { }

    //Alternate Constructor for GameDataCursor to use
    public GameData(int money, int gameTime, int nCommercial, int nResidential, int population)
    {
        this.money = money;
        this.gameTime = gameTime;
        this.nCommercial = nCommercial;
        this.nResidential = nResidential;
        this.population = population;
    }

    public static GameData get()
    {
        if(instance == null)
        {
            instance = new GameData();
        }
        return instance;
    }

    //Method responsible to set the game up and load everything
    public void gameStartUp(Context context)
    {
        // Settings
        this.gameSettings = GameSettings.get();
        gameSettings.load(context); //Loads database & Updates Settings to match db
        gameSettings.saveSettings(); //Adds Settings into database (For First Time)

        //Set map dimension prior to object creation since map gets created at get()
        GameMap.setMapWidth(gameSettings.getMapWidth());
        GameMap.setMapHeight(gameSettings.getMapHeight());
        GameMap gameMap = GameMap.get(); //Creates Map
        gameMap.load(context); //Loads Database & Gets Saved Map if any
        gameMap.saveMap(); //Saves Current map into database (For First Time)

        this.money = gameSettings.getInitialMoney();
        load(context);
        saveGameData();
    }

    //Method responsible to restart the game
    public void restartGame()
    {
        //No need to load any databases again as they are already loaded
        this.gameSettings = GameSettings.get();
        gameSettings.updateSettings();

        GameMap gameMap = GameMap.get();
        gameMap.setMapWidth(gameSettings.getMapWidth());
        gameMap.setMapHeight(gameSettings.getMapHeight());
        gameMap.removeMapElements();
        gameMap.regenerate();
        gameMap.saveMap();

        this.gameTime = 0;
        this.nCommercial = 0;
        this.nResidential = 0;
        this.population = 0;
        this.temperature = null;
        this.money = gameSettings.getInitialMoney();
        updateGameData();
    }

    //Whenever 'Time Step' Button is clicked, this method will also be run
    public void timeStep()
    {
        gameTime += 1;
        this.population = getPopulation();
        getEmploymentRate();
        this.money += (int) getIncome();
    }

    //ACCESSORS
    public int getId()
    {
        return id;
    }

    public double getEmploymentRate()
    {
        double empRate;
        if(population == 0)
        {
            empRate = -1;
        }
        else
        {
            empRate = Math.min(1, (double) nCommercial * (double) gameSettings.getShopSize() / (double) population);
        }
        return empRate;
    }

    public int getPopulation()
    {
        return nResidential * gameSettings.getFamilySize();
    }


    public double getIncome()
    {
        return population * (getEmploymentRate() * gameSettings.getSalary() * gameSettings.getTaxRate()
                - gameSettings.getServiceCost());
    }

    public int getMoney()
    {
        return money;
    }

    public int getGameTime()
    {
        return gameTime;
    }

    public int getNCommercial()
    {
        return nCommercial;
    }
    public int getNResidential()
    {
        return nResidential;
    }

    public GameSettings getGameSettings()
    {
        return gameSettings;
    }

    public String getTemperature()
    {
        return temperature;
    }

    //MUTATORS
    public void setMoney(int money)
    {
        this.money = money;
    }

    public void setNCommercial(int nCommercial)
    {
        this.nCommercial = nCommercial;
    }

    public void setNResidential(int nResidential)
    {
        this.nResidential = nResidential;
    }

    public void setDb(SQLiteDatabase db)
    {
        this.db = db;
    }

    public void setGameSettings(GameSettings gameSettings)
    {
        this.gameSettings = gameSettings;
    }

    public void updateTemperature() throws URLException
    {
        String urlString =
                Uri.parse("https://api.openweathermap.org/data/2.5/weather").buildUpon()
                        .appendQueryParameter("q", gameSettings.getCityName())
                        .appendQueryParameter("appid", "4c390f079c86059d401f42a9f00c47a5")
                        .appendQueryParameter("units", "metric").build().toString();
        System.out.println(gameSettings.getCityName());
        try
        {
            GetTemperature getTemp = new GetTemperature();
            String tempString = getTemp.execute(urlString).get();
            this.temperature = tempString;
            System.out.println("\n\nTEMPERATURE VALUE: " + temperature + "\n\n");
        }
        catch (ExecutionException ex)
        {
            throw new URLException(ex.getMessage(), ex);
        }
        catch (InterruptedException ex)
        {
            throw new URLException(ex.getMessage(), ex);
        }
    }

    //Class Responsible to retrieve temperature of game city through API Call
    private class GetTemperature extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String temp = null;
            String urlString = params[0];
            try
            {
                URL url = new URL(urlString);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                try
                {
                    if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK)
                    {
                        throw new URLException("URL Loading Error");
                    } else
                    {
                        String theDownload = IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8);
                        JSONObject jBase = new JSONObject(theDownload);
                        temp = jBase.getJSONObject("main").getString("temp");
                    }
                }  finally
                {
                    conn.disconnect();
                }
            }
            catch (URLException | JSONException | IOException e)
            {
                e.printStackTrace();
            }

            return temp;
        }
    }

    //Database Section
    public void load(Context context)
    {
        this.db = new GameDataDBHelper(context.getApplicationContext()).getWritableDatabase();

        cursor = new GameDataCursor(
                db.query(GameDataTable.NAME, // FROM Game Data Table
                        null, // SELECT ALL COLUMNS
                        null, // WHERE CLAUSE (null = all rows)
                        null, // WHERE ARGUMENTS
                        null, // GROUP BY ARGUMENTS
                        null, // HAVING clause
                        null // ORDER BY clause
                ));

        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                instance = cursor.getGameData(); //Gets Game Data From the database
                instance.setGameSettings(this.gameSettings);
                instance.setDb(db);
                this.money = instance.getMoney();
                this.gameTime = instance .getGameTime();
                this.nCommercial = instance.getNCommercial();
                this.nResidential = instance.getNResidential();
                this.population = instance.getPopulation();

                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }

    }

    public void saveGameData()
    {
        //Only add if there is no other game data saved
        if (cursor.getCount() < 1)
        {
            ContentValues cv = new ContentValues();
            cv.put(GameDataTable.Cols.ID, getId());
            cv.put(GameDataTable.Cols.MONEY, getMoney());
            cv.put(GameDataTable.Cols.GAME_TIME, getGameTime());
            cv.put(GameDataTable.Cols.NUM_COMMERCIAL, getNCommercial());
            cv.put(GameDataTable.Cols.NUM_RESIDENTIAL, getNResidential());
            cv.put(GameDataTable.Cols.POPULATION, getPopulation());

            db.insert(GameDataTable.NAME, null, cv);
        }
    }

    public void updateGameData()
    {
        ContentValues cv = new ContentValues();
        cv.put(GameDataTable.Cols.ID, getId());
        cv.put(GameDataTable.Cols.MONEY, getMoney());
        cv.put(GameDataTable.Cols.GAME_TIME, getGameTime());
        cv.put(GameDataTable.Cols.NUM_COMMERCIAL, getNCommercial());
        cv.put(GameDataTable.Cols.NUM_RESIDENTIAL, getNResidential());
        cv.put(GameDataTable.Cols.POPULATION, getPopulation());

        String[] whereValue = {String.valueOf(getId())};

        db.update(GameDataTable.NAME, cv, GameDataTable.Cols.ID + " =?", whereValue);
    }
}
