package biz.africanbib.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import biz.africanbib.Models.PreviousBusiness;
import biz.africanbib.R;


public class RecyclerViewAdapterPreviousBusiness extends RecyclerView.Adapter<RecyclerViewAdapterPreviousBusiness.MyViewHolder> {
    private List<PreviousBusiness> previousBusinessList;
    final String TAG = "PreviousBusiness";
    Context context;

    public RecyclerViewAdapterPreviousBusiness() {

    }
    public RecyclerViewAdapterPreviousBusiness(List<PreviousBusiness> previousBusinesses) {
        this.previousBusinessList = previousBusinesses;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_previous_business, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        context = parent.getContext();
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PreviousBusiness model = previousBusinessList.get(position);
        holder.textViewBusinessName.setText(model.getBusinessName());

    }


    @Override
    public int getItemCount() {
        return previousBusinessList == null ? 0 : previousBusinessList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewBusinessName;

        private MyViewHolder(View itemView) {
            super(itemView);
            textViewBusinessName = (TextView) itemView.findViewById(R.id.previous_business_name);

        }

    }
}


