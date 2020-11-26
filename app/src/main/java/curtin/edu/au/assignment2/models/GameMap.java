/************************************************************************
 * Author: George Aziz
 * Date Created: 06/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible for creating and storing the game map
 ***********************************************************************/
/* Most of Class retrieved from: Aziz, George. (2020). RecyclerView Practical 3. */

package curtin.edu.au.assignment2.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import curtin.edu.au.assignment2.R;
import curtin.edu.au.assignment2.databases.DBSchemas.MapTable;
import curtin.edu.au.assignment2.databases.GameMapCursor;
import curtin.edu.au.assignment2.databases.GameMapDBHelper;

import java.util.Random;

public class GameMap
{
    public static int mapWidth = 30;
    public static int mapHeight = 10;

    private static final int WATER = R.drawable.ic_water;
    private static final int[] GRASS = {R.drawable.ic_grass1, R.drawable.ic_grass2,
            R.drawable.ic_grass3, R.drawable.ic_grass4};

    private static final Random rng = new Random();
    private MapElement[][] grid;

    private static GameMap instance = null;
    private SQLiteDatabase db;
    private GameMapCursor cursor;

    public static GameMap get()
    {
        if(instance == null)
        {
            instance = new GameMap(generateGrid());
        }
        return instance;
    }

    private static MapElement[][] generateGrid()
    {
        final int HEIGHT_RANGE = 256;
        final int WATER_LEVEL = 112;
        final int INLAND_BIAS = 24;
        final int AREA_SIZE = 1;
        final int SMOOTHING_ITERATIONS = 2;

        int[][] heightField = new int[mapHeight][mapWidth];
        for(int i = 0; i < mapHeight; i++)
        {
            for(int j = 0; j < mapWidth; j++)
            {
                heightField[i][j] =
                        rng.nextInt(HEIGHT_RANGE)
                                + INLAND_BIAS * (
                                Math.min(Math.min(i, j), Math.min(mapHeight - i - 1, mapWidth - j - 1)) -
                                        Math.min(mapHeight, mapWidth) / 4);
            }
        }

        int[][] newHf = new int[mapHeight][mapWidth];
        for(int s = 0; s < SMOOTHING_ITERATIONS; s++)
        {
            for(int i = 0; i < mapHeight; i++)
            {
                for(int j = 0; j < mapWidth; j++)
                {
                    int areaSize = 0;
                    int heightSum = 0;

                    for(int areaI = Math.max(0, i - AREA_SIZE);
                        areaI < Math.min(mapHeight, i + AREA_SIZE + 1);
                        areaI++)
                    {
                        for(int areaJ = Math.max(0, j - AREA_SIZE);
                            areaJ < Math.min(mapWidth, j + AREA_SIZE + 1);
                            areaJ++)
                        {
                            areaSize++;
                            heightSum += heightField[areaI][areaJ];
                        }
                    }

                    newHf[i][j] = heightSum / areaSize;
                }
            }

            int[][] tmpHf = heightField;
            heightField = newHf;
            newHf = tmpHf;
        }

        MapElement[][] grid = new MapElement[mapHeight][mapWidth];
        int id = 0; // id for each map element for database
        for(int i = 0; i < mapHeight; i++)
        {
            for(int j = 0; j < mapWidth; j++)
            {
                MapElement element;

                if(heightField[i][j] >= WATER_LEVEL)
                {
                    boolean waterN = (i == 0)          || (heightField[i - 1][j] < WATER_LEVEL);
                    boolean waterE = (j == mapWidth - 1)  || (heightField[i][j + 1] < WATER_LEVEL);
                    boolean waterS = (i == mapHeight - 1) || (heightField[i + 1][j] < WATER_LEVEL);
                    boolean waterW = (j == 0)          || (heightField[i][j - 1] < WATER_LEVEL);

                    boolean waterNW = (i == 0) ||          (j == 0) ||         (heightField[i - 1][j - 1] < WATER_LEVEL);
                    boolean waterNE = (i == 0) ||          (j == mapWidth - 1) || (heightField[i - 1][j + 1] < WATER_LEVEL);
                    boolean waterSW = (i == mapHeight - 1) || (j == 0) ||         (heightField[i + 1][j - 1] < WATER_LEVEL);
                    boolean waterSE = (i == mapHeight - 1) || (j == mapWidth - 1) || (heightField[i + 1][j + 1] < WATER_LEVEL);

                    boolean coast = waterN || waterE || waterS || waterW ||
                            waterNW || waterNE || waterSW || waterSE;

                    grid[i][j] = new MapElement(
                            id, !coast,
                            choose(waterN, waterW, waterNW,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_northwest, R.drawable.ic_coast_northwest_concave),
                            choose(waterN, waterE, waterNE,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_northeast, R.drawable.ic_coast_northeast_concave),
                            choose(waterS, waterW, waterSW,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_southwest, R.drawable.ic_coast_southwest_concave),
                            choose(waterS, waterE, waterSE,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_southeast, R.drawable.ic_coast_southeast_concave),
                            null, null, null);
                }
                else
                {
                    grid[i][j] = new MapElement(
                            id, false, WATER, WATER, WATER, WATER, null, null, null);
                }
                id++; //Increment id once a new element has been created
            }
        }
        return grid;
    }

    private static int choose(boolean nsWater, boolean ewWater, boolean diagWater,
                              int nsCoastId, int ewCoastId, int convexCoastId, int concaveCoastId)
    {
        int id;
        if(nsWater)
        {
            if(ewWater)
            {
                id = convexCoastId;
            }
            else
            {
                id = nsCoastId;
            }
        }
        else
        {
            if(ewWater)
            {
                id = ewCoastId;
            }
            else if(diagWater)
            {
                id = concaveCoastId;
            }
            else
            {
                id = GRASS[rng.nextInt(GRASS.length)];
            }
        }
        return id;
    }

    protected GameMap(MapElement[][] grid)
    {
        this.grid = grid;
    }

    public void regenerate()
    {
        this.grid = generateGrid();
    }

    //ACCESSORS
    public MapElement[][] getMap()
    {
        return this.grid;
    }

    public MapElement get(int i, int j)
    {
        return grid[i][j];
    }

    public int getMapWidth()
    {
        return mapWidth;
    }

    public int getMapHeight()
    {
        return mapHeight;
    }

    //MUTATORS
    public static void setMapWidth(int mWidth)
    {
        mapWidth = mWidth;
    }

    public static void setMapHeight(int mHeight)
    {
        mapHeight = mHeight;
    }

    //Database Section
    public void load(Context context)
    {
        this.db = new GameMapDBHelper(context.getApplicationContext()).getWritableDatabase();
        //db.delete(MapTable.NAME, null, null);
        this.cursor = new GameMapCursor(
                db.query(MapTable.NAME, // FROM Map Table
                        null, // SELECT ALL COLUMNS
                        null, // WHERE CLAUSE (null = all rows)
                        null, // WHERE ARGUMENTS
                        null, // GROUP BY ARGUMENTS
                        null, // HAVING clause
                        null // ORDER BY clause
                ));
        try
        {
            int row = 0, col = 0;
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                //If we have reached last column of map then we reset col count and increment to next row(level) of map
                if (col > mapWidth-1)
                {
                    col = 0;
                    row++;
                }
                this.grid[row][col] = cursor.getMapCell();
                col++;
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
    }

    public void saveMap()
    {
        // Only adds map if database is empty
        if(cursor.getCount() < 1)
        {
            for (int row = 0; row < mapHeight; row++)
            {
                for (int col = 0; col < mapWidth; col++)
                {
                    addMapElement(grid[row][col]);
                }
            }
        }
    }

    public void addMapElement(MapElement curElement)
    {
        ContentValues cv = new ContentValues();
        boolean buildBool = curElement.isBuildable();
        int buildableValue;
        if(buildBool)
        {
            buildableValue = 1;
        }
        else
        {
            buildableValue = 0;
        }
        cv.put(MapTable.Cols.ID, curElement.getId());
        cv.put(MapTable.Cols.BUILDABLE, buildableValue);
        cv.put(MapTable.Cols.TERRAIN_NW, curElement.getNorthWest());
        cv.put(MapTable.Cols.TERRAIN_NE, curElement.getNorthEast());
        cv.put(MapTable.Cols.TERRAIN_SE, curElement.getSouthEast());
        cv.put(MapTable.Cols.TERRAIN_SW, curElement.getSouthWest());

        Structure curStruct = curElement.getStructure();
        if(curStruct == null) //If there is no structure
        {
            cv.put(MapTable.Cols.STRUCTURE_ID, 0);
            cv.put(MapTable.Cols.STRUCTURE_LABEL, "");
            cv.put(MapTable.Cols.EDIT_NAME, "");
            cv.put(MapTable.Cols.IMAGE, "");
        }
        else //If there is a structure
        {
            cv.put(MapTable.Cols.STRUCTURE_ID, curElement.getStructure().getDrawableId());
            cv.put(MapTable.Cols.STRUCTURE_LABEL, curElement.getStructure().getLabel());
            if (curElement.getEditName() == null)
            {
                cv.put(MapTable.Cols.EDIT_NAME, "");
            }
            else
            {
                cv.put(MapTable.Cols.EDIT_NAME, curElement.getEditName());
            }

            if (curElement.getCustomImage() == null)
            {
                cv.put(MapTable.Cols.IMAGE, "");
            }
            else
            {
                cv.put(MapTable.Cols.IMAGE, curElement.getBytes());
            }
        }
        db.insert(MapTable.NAME, null, cv);
    }

    public void updateMapElement(MapElement curElement)
    {
        ContentValues cv = new ContentValues();
        boolean buildBool = curElement.isBuildable();
        int buildableValue;
        if(buildBool)
        {
            buildableValue = 1;
        }
        else
        {
            buildableValue = 0;
        }
        cv.put(MapTable.Cols.ID, curElement.getId());
        cv.put(MapTable.Cols.BUILDABLE, buildableValue);
        cv.put(MapTable.Cols.TERRAIN_NW, curElement.getNorthWest());
        cv.put(MapTable.Cols.TERRAIN_NE, curElement.getNorthEast());
        cv.put(MapTable.Cols.TERRAIN_SE, curElement.getSouthEast());
        cv.put(MapTable.Cols.TERRAIN_SW, curElement.getSouthWest());

        Structure curStruct = curElement.getStructure();
        if(curStruct == null) //If there is no structure
        {
            cv.put(MapTable.Cols.STRUCTURE_ID, 0);
            cv.put(MapTable.Cols.STRUCTURE_LABEL, "");
            cv.put(MapTable.Cols.EDIT_NAME, "");
            cv.put(MapTable.Cols.IMAGE, "");
        }
        else //If there is structure
        {
            cv.put(MapTable.Cols.STRUCTURE_ID, curElement.getStructure().getDrawableId());
            cv.put(MapTable.Cols.STRUCTURE_LABEL, curElement.getStructure().getLabel());
            if (curElement.getEditName() == null)
            {
                cv.put(MapTable.Cols.EDIT_NAME, "");
            }
            else
            {
                cv.put(MapTable.Cols.EDIT_NAME, curElement.getEditName());
            }

            if (curElement.getCustomImage() == null)
            {
                cv.put(MapTable.Cols.IMAGE, "");
            }
            else
            {
                cv.put(MapTable.Cols.IMAGE, curElement.getBytes());
            }
        }

        String[] whereValue = {String.valueOf(curElement.getId())};

        db.update(MapTable.NAME, cv, MapTable.Cols.ID + " =?", whereValue);
    }

    //Removes all map elements from Game Map database
    public void removeMapElements()
    {
        db.delete(MapTable.NAME, null,null);
    }
}
