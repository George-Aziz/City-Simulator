/***********************************************************************************************
 * Author: George Aziz
 * Date Created: 07/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class allows game to be able to get game map contents when loading at start of game
 **********************************************************************************************/

package curtin.edu.au.assignment2.databases;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;

import curtin.edu.au.assignment2.models.Commercial;
import curtin.edu.au.assignment2.models.MapElement;
import curtin.edu.au.assignment2.models.Residential;
import curtin.edu.au.assignment2.models.Road;
import curtin.edu.au.assignment2.models.Structure;
import curtin.edu.au.assignment2.databases.DBSchemas.MapTable;

public class GameMapCursor extends CursorWrapper
{
    public GameMapCursor(Cursor cursor) { super(cursor); }

    public MapElement getMapCell()
    {
        //Gets All the Game's Map Elements From Database
        int id = getInt(getColumnIndex(MapTable.Cols.ID));
        int buildable = getInt(getColumnIndex(MapTable.Cols.BUILDABLE));
        int terrainNorthWest = getInt(getColumnIndex(MapTable.Cols.TERRAIN_NW));
        int terrainNorthEast = getInt(getColumnIndex(MapTable.Cols.TERRAIN_NE));
        int terrainSouthWest = getInt(getColumnIndex(MapTable.Cols.TERRAIN_SW));
        int terrainSouthEast = getInt(getColumnIndex(MapTable.Cols.TERRAIN_SE));
        String editName = getString(getColumnIndex(MapTable.Cols.EDIT_NAME));

        int structID = getInt(getColumnIndex(MapTable.Cols.STRUCTURE_ID));
        String label = getString(getColumnIndex(MapTable.Cols.STRUCTURE_LABEL));

        Structure newStruct = null;
        //Uses label of struct to determine what type of Structure
        if(label.equals("House"))
        {
            newStruct = new Residential(structID, label);
        }
        else if (label.equals("Factory"))
        {
            newStruct = new Commercial(structID,label);
        }
        else if (label.equals("Road"))
        {
            newStruct = new Road(structID,label);
        }

        boolean isBuildable;
        //True = 1 False = 0
        if(buildable == 0)
        {
            isBuildable = false;
        }
        else
        {
            isBuildable = true;
        }

        //if editname is "" (empty) make it null as other aspects of program rely on editName being null
        if(editName.isEmpty())
        {
           editName = null;
        }

        Bitmap customImage;
        if(getBlob(getColumnIndex(MapTable.Cols.IMAGE)).toString().isEmpty())
        {
            customImage = null;
        }
        else
        {
            customImage = MapElement.getImage(getBlob(getColumnIndex(MapTable.Cols.IMAGE)));
        }

        return new MapElement(id,isBuildable,terrainNorthWest,terrainNorthEast,terrainSouthWest,terrainSouthEast, newStruct, editName, customImage);
    }
}
