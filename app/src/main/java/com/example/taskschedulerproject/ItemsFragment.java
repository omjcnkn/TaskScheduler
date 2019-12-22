package com.example.taskschedulerproject;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment {
    private RecyclerView rvItems;
    public ItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_items, container, false);

        rvItems = rootView.findViewById(R.id.itemsRecyclerView);

        ArrayList<String> items = new ArrayList<String>();
        items.add("Task1");
        items.add("Task2");
        items.add("Task3");
        items.add("Task4");
        items.add("Task5");
        items.add("Task6");
        items.add("Task7");
        ItemsAdapter adapter = new ItemsAdapter(items);
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItems.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        return rootView;
    }


    /* Creating RecyclerView Adapter and ViewHolder */
    public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private ArrayList<String> itemsNamesArrayList;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView itemNameTextView;
            public TextView itemDueDateTextView;
            public ImageView priorityImageView;
            public ImageView checkImageView;
            public ImageView deleteImageView;


            public ViewHolder(View itemView) {
                super(itemView);

                itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
                itemDueDateTextView = itemView.findViewById(R.id.itemDueDateTextView);
                checkImageView = itemView.findViewById(R.id.checkImageView);
                deleteImageView = itemView.findViewById(R.id.deleteImageView);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Item" + getAdapterPosition(), Toast.LENGTH_LONG).show();
            }


        }

        public ItemsAdapter(ArrayList<String> items) {
            itemsNamesArrayList = items;
        }

        @Override
        public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();

            LayoutInflater inflater = LayoutInflater.from(context);

            View itemView = inflater.inflate(R.layout.items_layout, parent, false);

            ViewHolder holder = new ViewHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(ItemsAdapter.ViewHolder holder, int position) {
            holder.itemNameTextView.setText(itemsNamesArrayList.get(position));
            holder.itemDueDateTextView.setText(itemsNamesArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return itemsNamesArrayList.size();
        }
    }
}
