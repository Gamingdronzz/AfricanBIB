package biz.africanbib.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        holder.buttonBusinessName.setText(model.getBusinessName());
        holder.addedOn.setText("Added on : " + model.getDateOfAddition() + "at " + model.getTimeOfAddition());

        Log.v(TAG, "Status = " + model.isUploadStatus());
        if (model.isUploadStatus()) {
            holder.buttonBusinessName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_tick, 0);
            holder.uploadedOn.setText("Uploaded on : " + model.getDateOfUploading() + " at " + model.getTimeOfUploading());
            holder.uploadedOn.setVisibility(View.VISIBLE);
        } else {

            holder.buttonBusinessName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_cross, 0);
            holder.uploadedOn.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return previousBusinessList == null ? 0 : previousBusinessList.size();
    }

    public void updateList(List<PreviousBusiness> temp) {
        previousBusinessList = temp;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private Button buttonBusinessName;
        private TextView addedOn;
        private TextView uploadedOn;
        //private ImageView imageViewUploadStatus;

        private MyViewHolder(View itemView) {
            super(itemView);
            buttonBusinessName = (Button) itemView.findViewById(R.id.previous_business_name);
            addedOn = (TextView) itemView.findViewById(R.id.txtview_addedon);
            uploadedOn = (TextView) itemView.findViewById(R.id.txtview_uploadedon);
            //imageViewUploadStatus = (ImageView) itemView.findViewById(R.id.imageViewUploadStatus);

        }

    }
}


