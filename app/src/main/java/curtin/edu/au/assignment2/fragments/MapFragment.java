/*****************************************************************************************************************
 * Author: George Aziz
 * Date Created: 07/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible of handing Map Fragment which is fragment above MapDetailsFragment (Stats Fragment)
 ****************************************************************************************************************/
/* Parts of code retrieved from: Aziz, George. (2020). RecyclerView Practical 3. */

package curtin.edu.au.assignment2.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import curtin.edu.au.assignment2.models.GameData;
import curtin.edu.au.assignment2.models.GameMap;
import curtin.edu.au.assignment2.models.MapElement;
import curtin.edu.au.assignment2.R;
import curtin.edu.au.assignment2.models.Structure;
import curtin.edu.au.assignment2.activities.DetailScreenActivity;

public class MapFragment extends Fragment
{
    private GameData game;
    private GameMap gameMap;
    private static MapAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.fragment_map, ui, false);

        game = GameData.get();
        gameMap = GameMap.get();

        RecyclerView rv;
        rv = (RecyclerView) view.findViewById(R.id.mapRecyclerView);
        rv.setLayoutManager(new GridLayoutManager( getActivity(), gameMap.getMapHeight(), GridLayoutManager.HORIZONTAL, false));

        adapter = new MapAdapter(gameMap);
        rv.setAdapter(adapter);

        return view;
    }

    public static void updateMapView()
    {
        adapter.notifyDataSetChanged();
    }

    private class MapElementVHolder extends RecyclerView.ViewHolder
    {
        private ImageView topL,topR,botL,botR,outer;
        public MapElementVHolder(LayoutInflater li, ViewGroup parent)
        {
            super(li.inflate(R.layout.grid_cell, parent, false));
            topL = (ImageView) itemView.findViewById(R.id.topLeft);
            topR = (ImageView) itemView.findViewById(R.id.topRight);
            botL = (ImageView) itemView.findViewById(R.id.bottomLeft);
            botR = (ImageView) itemView.findViewById(R.id.bottomRight);
            outer = (ImageView) itemView.findViewById(R.id.structureImg);

            int size = parent.getMeasuredHeight() / GameMap.mapHeight + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;
        }

        public void bind(final MapElement data, final int row, final int col)
        {
            topL.setImageResource(data.getNorthWest());
            topR.setImageResource(data.getNorthEast());
            botR.setImageResource(data.getSouthEast());
            botL.setImageResource(data.getSouthWest());

            if (data.getCustomImage() != null) //If custom image is found then display custom image
            {
                outer.setImageBitmap(data.getCustomImage());
            }
            else if (data.getStructure() != null) //Display structure only if custom image not found
            {
                outer.setImageResource(data.getStructure().getDrawableId());
            }
            else //If no custom image or structure is available then display nothing
            {
                outer.setImageDrawable(null);
            }

            outer.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (MapDetailsFragment.getDemolishBtnPress())
                    {
                        demolish(data);
                    }
                    else if (MapDetailsFragment.getDetailsBtnPress())
                    {
                        details(data, row, col);
                    }
                    else // Normal Build
                    {
                        build(data, row, col);
                    }
                }
            });
        }

        //Method to demolish a structure from Game Map
        public void demolish(MapElement data)
        {
            // If Area is buildable and there is a structure in it
            if (data.isBuildable() && data.getStructure() != null)
            {
                if(data.getStructure().getLabel().equals("Factory"))
                {
                    game.setNCommercial(game.getNCommercial() - 1); //If Commercial, then reduce count of commercial buildings
                }
                else if (data.getStructure().getLabel().equals("House"))
                {
                    game.setNResidential(game.getNResidential() - 1); // If Residential, then reduce count of residential buildings
                }
                outer.setImageResource(0); // Removes Image from map
                data.setStructure(null); // Makes element's structure to null to remove
                data.setCustomImage(null); // Makes element's custom image to null in case there is
                data.setEditName(null); // Makes the editable name of the element null
                gameMap.updateMapElement(data); // Updates the MapElement in database
                MapDetailsFragment.updateStats(getActivity()); // Updates stats for game
            }
            else if (data.isBuildable() && data.getStructure() == null)
            {
                Toast.makeText(getContext(), "No Structure to remove in this area!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "Can't demolish in non-buildable area!", Toast.LENGTH_SHORT).show();
            }
            MapDetailsFragment.setDemolishFalse();
        }

        //Method to view details of a structure built on the Game Map
        public void details(MapElement data, int row, int col)
        {
            //If the cell is buildable and there is a structure built
            if (data.isBuildable() && data.getStructure() != null)
            {
                startActivity(MapFragment.getIntent(getActivity(),row,col));
            }
            else
            {
                Toast.makeText(getContext(), "No Details for this area!", Toast.LENGTH_SHORT).show();
            }
            MapDetailsFragment.setDetailsFalse();
        }

        //Method to build a structure on the Game Map
        public void build(MapElement data, int row, int col)
        {
            Structure curStruct = SelectorFragment.getSelectedStruct();

            if (curStruct != null) // User has structure selected
            {
                //Area is empty and buildable
                if (data.isBuildable() && data.getStructure() == null)
                {
                    int cost = 0;
                    char structType = 'n'; //n for null
                    if(curStruct.getLabel().equals("House"))
                    {
                        cost = game.getGameSettings().getHouseBuildingCost();
                        structType = 'h';
                    }
                    else if (curStruct.getLabel().equals("Factory"))
                    {
                        cost = game.getGameSettings().getCommBuildingCost();
                        structType = 'c';
                    }
                    else if (curStruct.getLabel().equals("Road"))
                    {
                        cost = game.getGameSettings().getRoadBuildingCost();
                        structType = 'r';
                    }

                    //If element is adjacent to a road or selected structure to build is a road then it will build
                    if(roadAdjacent(row, col) || structType == 'r')
                    {
                        if (game.getMoney() >= cost) // Can't purchase if not enough money
                        {
                            game.setMoney(game.getMoney() - cost);
                            data.setStructure(curStruct);
                            outer.setImageResource(curStruct.getDrawableId());
                            if (structType == 'h')
                            {
                                game.setNResidential(game.getNResidential() + 1);
                            }
                            else if (structType == 'c')
                            {
                                game.setNCommercial(game.getNCommercial() + 1);
                            }
                            gameMap.updateMapElement(data);
                            MapDetailsFragment.updateStats(getActivity());
                        }
                        else
                        {
                            Toast.makeText(getContext(), "You do not have enough money to purchase this structure!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "To build a house or a factory please build a road adjacent to it first!", Toast.LENGTH_SHORT).show();
                    }

                }
                else if (data.isBuildable() && data.getStructure() != null)
                {
                    Toast.makeText(getContext(), "Area already has structure built on it, to build over it please demolish first!", Toast.LENGTH_SHORT).show();
                }
                else // Area is non-buildable
                {
                    Toast.makeText(getContext(), "Can't build in this area", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                if (data.isBuildable())
                {
                    Toast.makeText(getContext(), "Please select a structure to build on this area", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Please select a structure to build and select an area that is buildable", Toast.LENGTH_SHORT).show();
                }
            }
        }

        //Method which chekcs if current element is adjacent to a road
        public boolean roadAdjacent(int row, int col)
        {
            GameMap gameMap = GameMap.get();
            boolean isAdjacent = false;

            Structure above = gameMap.get((row-1),col).getStructure();
            Structure below = gameMap.get((row+1),col).getStructure();
            Structure left = gameMap.get((row),(col-1)).getStructure();
            Structure right = gameMap.get(row,(col+1)).getStructure();

            //Checks if the provided cell has a road above, below, right or left
            if((above != null && above.getLabel().equals("Road")) ||
               (below != null && below.getLabel().equals("Road")) ||
               (left != null && left.getLabel().equals("Road")) ||
               (right != null && right.getLabel().equals("Road")))

            {
                isAdjacent = true;
            }
            return isAdjacent;
        }
    }

    private class MapAdapter extends RecyclerView.Adapter<MapElementVHolder>
    {
        private GameMap data;

        public MapAdapter(GameMap data)
        {
            this.data = data;
        }

        @Override
        public int getItemCount()
        {
            return data.getMapWidth() * data.getMapHeight();
        }

        @Override
        public MapElementVHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new MapElementVHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(MapElementVHolder vh, int position)
        {
            int row = position % data.getMapHeight();
            int col = position / data.getMapHeight();

            vh.bind(data.get(row,col), row, col);
        }
    }

    public static Intent getIntent(Context c, int row, int col)
    {
        Intent intent = new Intent(c, DetailScreenActivity.class);
        intent.putExtra("row", row);
        intent.putExtra("col", col);
        return intent;
    }
}
