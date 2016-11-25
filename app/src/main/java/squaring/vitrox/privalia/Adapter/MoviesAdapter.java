package squaring.vitrox.privalia.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import squaring.vitrox.privalia.Common.Config;
import squaring.vitrox.privalia.Model.FullMovie;
import squaring.vitrox.privalia.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<FullMovie> mDataSet;
    private Context mContext;

    public MoviesAdapter(Context context) {
        mDataSet = new ArrayList<>();
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView mTitleView;
        private ImageView mThumbImageView;
        private TextView mYearView;
        private TextView mDescriptionView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbImageView = (ImageView) view.findViewById(R.id.thumbnail);
            mDescriptionView = (TextView) view.findViewById(R.id.movie_description);
            mTitleView = (TextView) view.findViewById(R.id.movie_title);
            mYearView = (TextView) view.findViewById(R.id.movie_year);
        }

        public void bind(final FullMovie fullMovieDetail) {
            mDescriptionView.setText(fullMovieDetail.getMovieDetail().getOverview());
            mTitleView.setText(fullMovieDetail.getMovieDetail().getTitle());
            mYearView.setText(String.valueOf(fullMovieDetail.getMovieDetail().getYear()));
            String urlImage = Config.TMDB_IMAGE_URL + fullMovieDetail.getPosterUrl();
            Glide.with(mContext).load(urlImage)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                    .centerCrop().into(mThumbImageView);
        }
    }

    public void addOneData(FullMovie data) {
        mDataSet.add(data);
        notifyDataSetChanged();
    }

    public void resetData() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public List<FullMovie> getAllDataList()
    {
      return mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.bind(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) {
            return 0;
        }
        return mDataSet.size();
    }
}