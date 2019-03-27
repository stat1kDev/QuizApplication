package stat1kDev.quizapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import stat1kDev.quizapp.R;
import stat1kDev.quizapp.listeners.ListitemClickListener;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private Context mContext;
    private Activity mActivity;

    private ArrayList<String> mItemList;
    private ArrayList<String> mColorList;
    private ListitemClickListener mItemClickListener;

    public QuizAdapter(Context mContext, Activity mActivity, ArrayList<String> mItemList, ArrayList<String> mColorList) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mItemList = mItemList;
        this.mColorList = mColorList;
    }

    public void setItemClickListener(ListitemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }


    @Override
    public QuizAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizAdapter.ViewHolder(view, viewType, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvItemTitle;
        private CardView lytContainer;
        private ListitemClickListener itemClickListener;


        public ViewHolder(View itemView, int viewType, ListitemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            // Find all views ids
            tvItemTitle = (TextView) itemView.findViewById(R.id.answer_text);
            lytContainer = (CardView) itemView.findViewById(R.id.card_view);

            lytContainer.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != mItemList ? mItemList.size() : 0);

    }

    @Override
    public void onBindViewHolder(QuizAdapter.ViewHolder mainHolder, int position) {
        final String model = mItemList.get(position);
        final String model1 = mColorList.get(position);

        // setting data over views
        mainHolder.tvItemTitle.setText(Html.fromHtml(model));
        mainHolder.tvItemTitle.setBackgroundResource(mActivity.getResources().getIdentifier(model1, "drawable", mActivity.getPackageName()));

    }
}