package gachon.termproject.danggeun.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import gachon.termproject.danggeun.Customer.BreadDTO;
import gachon.termproject.danggeun.R;
import gachon.termproject.danggeun.Util.Firebase;
import gachon.termproject.danggeun.Util.Model.ReservatoinRequest;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.VerticalViewHolder>{

    private ArrayList<ReservatoinRequest> reservationRequest;
    private Context context;

    public VerticalAdapter(Context context, ArrayList<ReservatoinRequest> reservationRequest)
    {
        this.context = context;
        this.reservationRequest = reservationRequest;
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder{
        protected RecyclerView recyclerView;
        protected TextView userName;
        protected TextView time;
        protected TextView Totalprice;

        public VerticalViewHolder(View view)
        {
            super(view);
            userName=view.findViewById(R.id.userName);
            time = view.findViewById(R.id.Time);
            Totalprice = view.findViewById(R.id.Totalprice);
            this.recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewVertical);
        }
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reservationlist_manager2, null);
        return new VerticalAdapter.VerticalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder verticalViewHolder, int position) {

        ReservatoinRequest currentReservation=reservationRequest.get(position);
        ArrayList<BreadDTO> breadList=currentReservation.getBreadArrayList();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(currentReservation.getUserId());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                verticalViewHolder.userName.setText(documentSnapshot.get("nickname").toString());

            }

        });
//        verticalViewHolder.userName.setText(documentSnapshot.get("nickname"));

        SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );
        Date date = currentReservation.getTimestamp().toDate();
        verticalViewHolder.time.setText(sdf.format(date));
        int totalPrice=0;
        for(BreadDTO bread:breadList){
            totalPrice=totalPrice+Integer.parseInt(bread.getTotalPrice());
        }
        verticalViewHolder.Totalprice.setText("총 "+ Integer.toString(totalPrice)+"원");


        HorizontalAdapter adapter = new HorizontalAdapter(breadList);

        verticalViewHolder.recyclerView.setHasFixedSize(true);
        verticalViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context
                , LinearLayoutManager.VERTICAL
                ,false));
        verticalViewHolder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return reservationRequest.size();
    }
}