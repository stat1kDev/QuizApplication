package stat1kDev.quizapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import stat1kDev.quizapp.R;
import stat1kDev.quizapp.listeners.ListitemClickListener;
import stat1kDev.quizapp.models.quiz.notification.NotificationModel;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<NotificationModel> mNotificationList;
    private ListitemClickListener mItemClickListener;

    public NotificationAdapter(Context mContext, ArrayList<NotificationModel> mNotificationList) {
        this.mContext = mContext;
        this.mNotificationList = mNotificationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvSubTitle;
        private ImageView imgVw;

        // handle interface for item listener
        private ListitemClickListener itemClickListener;

        public ViewHolder(View itemView, int viewType, ListitemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_noti_title);
            tvSubTitle = (TextView) itemView.findViewById(R.id.tv_noti_sub_title);
            imgVw = (ImageView) itemView.findViewById(R.id.img_noti);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view, viewType, mItemClickListener);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String title = mNotificationList.get(position).getTitle();
        String message = mNotificationList.get(position).getMessage();

        if (title != null) {
            if (mNotificationList.get(position).isUnread()) {
                holder.tvTitle.setTypeface(null, Typeface.BOLD);
            } else {
                holder.tvTitle.setTypeface(null, Typeface.NORMAL);
            }
            holder.tvTitle.setText(title);
            holder.tvSubTitle.setText(message);
        }
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    public void setItemClickListener(ListitemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
}