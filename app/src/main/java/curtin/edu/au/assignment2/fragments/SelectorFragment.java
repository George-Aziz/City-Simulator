/************************************************************************************************************
 * Author: George Aziz
 * Date Created: 07/10/2020
 * Date Last Modified : 13/10/2020
 * Purpose: Class responsible of handing SelectorFragment which is below MapDetailsFragment (Stats Fragment)
 ***********************************************************************************************************/
/* Class retrieved from: Aziz, George. (2020). RecyclerView Practical 3. */

package curtin.edu.au.assignment2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import curtin.edu.au.assignment2.R;
import curtin.edu.au.assignment2.models.Structure;
import curtin.edu.au.assignment2.models.StructureData;

public class SelectorFragment extends Fragment
{
    private static Structure selectedStruct;

    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.fragment_selector, ui, false);

        RecyclerView rv;
        rv = (RecyclerView) view.findViewById(R.id.selectorRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        StructureData data = StructureData.get();
        SelectorFragment.StructureAdapter adapter = new SelectorFragment.StructureAdapter(data);
        rv.setAdapter(adapter);

        return view;
    }

    private class StructureVHolder extends RecyclerView.ViewHolder
    {
        ImageView structImg;
        TextView structDesc;

        public StructureVHolder(LayoutInflater li, ViewGroup parent)
        {
            super(li.inflate(R.layout.list_selection, parent, false));
            structImg = (ImageView) itemView.findViewById(R.id.structureImage);
            structDesc = (TextView) itemView.findViewById(R.id.structureDesc);

        }

        public void bind(final Structure data)
        {
            structImg.setImageResource(data.getDrawableId());
            structDesc.setText(data.getLabel());
            //OnClick for eahc structure in the selector
            structImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedStruct = data;
                }
            });
        }
    }

    private class StructureAdapter extends RecyclerView.Adapter<SelectorFragment.StructureVHolder>
    {
        private StructureData data;

        public StructureAdapter(StructureData data)
        {
            this.data = data;
        }

        @Override
        public int getItemCount()
        {
            return data.size();
        }

        @Override
        public SelectorFragment.StructureVHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new SelectorFragment.StructureVHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(SelectorFragment.StructureVHolder vh, int index)
        {
            vh.bind(data.get(index));
        }
    }

    //Method which provides current selected structure by user
    public static Structure getSelectedStruct()
    {
        return selectedStruct;
    }
}
