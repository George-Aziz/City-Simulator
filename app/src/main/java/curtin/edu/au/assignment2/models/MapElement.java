/********************************************************************
 * Author: George Aziz
 * Date Created: 06/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible for for storing MapElement details
 *******************************************************************/
/* Parts of code retrieved from: Aziz, George. (2020). RecyclerView Practical 3. */

package curtin.edu.au.assignment2.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class MapElement
{
    private int id;
    private final boolean buildable;
    private final int terrainNorthWest;
    private final int terrainSouthWest;
    private final int terrainNorthEast;
    private final int terrainSouthEast;

    private Structure structure;
    private String editName;
    private Bitmap customImage;

    public MapElement(int id, boolean buildable, int northWest, int northEast,
                      int southWest, int southEast, Structure structure, String editName,
                      Bitmap customImage)
    {
        this.id = id;
        this.buildable = buildable;
        this.terrainNorthWest = northWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthWest = southWest;
        this.terrainSouthEast = southEast;
        this.structure = structure;
        this.editName = editName;
        this.customImage = customImage;
    }

    public int getId()
    {
        return id;
    }

    public boolean isBuildable()
    {
        return buildable;
    }

    public int getNorthWest()
    {
        return terrainNorthWest;
    }

    public int getSouthWest()
    {
        return terrainSouthWest;
    }

    public int getNorthEast()
    {
        return terrainNorthEast;
    }

    public int getSouthEast()
    {
        return terrainSouthEast;
    }

    public String getEditName()
    {
        return editName;
    }

    public Bitmap getCustomImage() { return  customImage; }

    public byte[] getBytes()
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        customImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image)
    {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    /**
     * Retrieves the structure built on this map element.
     *
     * @return The structure, or null if one is not present.
     */
    public Structure getStructure()
    {
        return structure;
    }

    public void setStructure(Structure structure)
    {
        this.structure = structure;

    }

    public void setEditName (String editName)
    {
        this.editName = editName;
    }

    public void setCustomImage(Bitmap customImage)
    {
        this.customImage = customImage;
    }
}
