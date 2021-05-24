package gachon.termproject.danggeun.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gachon.termproject.danggeun.Customer.BreadDTO;
import gachon.termproject.danggeun.R;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder> {

    private ArrayList<BreadDTO> breadList;

    public HorizontalAdapter(ArrayList<BreadDTO> data)
    {
        this.breadList = data;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{
        protected TextView breadName;
        protected TextView breadCount;
        protected TextView breadPrice;

        public HorizontalViewHolder(View view)
        {
            super(view);
            breadName = view.findViewById(R.id.breadName);
            breadCount = view.findViewById(R.id.Count);
            breadPrice = view.findViewById(R.id.textView2);
        }
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.reservationlist_manager, null);

        return new HorizontalAdapter.HorizontalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder horizontalViewHolder, int position)
    {
        horizontalViewHolder.breadName.setText(breadList.get(position).getBreadName());

        Log.v("개수",breadList.get(position).getBreadName());
        horizontalViewHolder.breadCount.setText(breadList.get(position).getCount()+"개");
        horizontalViewHolder.breadPrice.setText(breadList.get(position).getTotalPrice()+"원");
    }

    @Override
    public int getItemCount() {
        return breadList.size();
    }
}