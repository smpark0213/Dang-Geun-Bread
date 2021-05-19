package gachon.termproject.danggeun;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BreadAdpater extends RecyclerView.Adapter<BreadAdpater.ViewHolder> {
    private ArrayList<BreadInfo> breadData = null;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView bread_name;
        AppCompatTextView bread_price;

        ViewHolder(View itemView){
            super(itemView);
            bread_name = itemView.findViewById(R.id.bread_name);
            bread_price = itemView.findViewById(R.id.bread_price);
        }
    }

    public BreadAdpater(ArrayList<BreadInfo> breadData) {
        this.breadData = breadData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.bread_store, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreadAdpater.ViewHolder holder, int position) {

        BreadInfo currentBread = breadData.get(position);

        holder.bread_name.setText(currentBread.getBreadName());
        holder.bread_price.setText(currentBread.getPrice());
    }

    @Override
    public int getItemCount() {
        return breadData.size();
    }
}
