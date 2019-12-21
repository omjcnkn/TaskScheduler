package com.example.taskschedulerproject;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListFragment extends Fragment {


    public ItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> listNamesArrayList = new ArrayList<>();

        listNamesArrayList.add("List 1");
        listNamesArrayList.add("List 2");
        listNamesArrayList.add("List 3");
        listNamesArrayList.add("List 4");
        listNamesArrayList.add("List 5");
        listNamesArrayList.add("List 6");

        RecyclerView rcListNames = view.findViewById(R.id.listsContainer);

        ListsAdapter adapter = new ListsAdapter(listNamesArrayList);

        rcListNames.setAdapter(adapter);
        rcListNames.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {
        private ArrayList<String> listsNamesArrayList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView listNameTextView, creationDateTextView;
            public ImageView deleteImageButton;

            public ViewHolder(View itemView) {
                super(itemView);

                listNameTextView = itemView.findViewById(R.id.listNameTextView);
                creationDateTextView = itemView.findViewById(R.id.creationDateTextView);

                deleteImageButton = itemView.findViewById(R.id.deleteImageButton);
            }
        }


        public ListsAdapter(ArrayList<String> names) {
            listsNamesArrayList = names;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View cardListView = inflater.inflate(R.layout.card_view_layout, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(cardListView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.listNameTextView.setText(listsNamesArrayList.get(position));
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            holder.creationDateTextView.setText("Created in "+formatter.format(date));
            holder.deleteImageButton.setImageResource(R.drawable.delete_button);
        }

        @Override
        public int getItemCount() {
            return listsNamesArrayList.size();
        }
    }
}
