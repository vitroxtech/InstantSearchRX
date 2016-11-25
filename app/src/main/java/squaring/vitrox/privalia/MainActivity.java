package squaring.vitrox.privalia;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import retrofit.Response;

import javax.inject.Inject;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.okhttp.Headers;

import java.util.List;

import rx.Observable;
import rx.Observer;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import squaring.vitrox.privalia.Adapter.MoviesAdapter;
import squaring.vitrox.privalia.Common.Config;
import squaring.vitrox.privalia.Common.PaginationHelper;
import squaring.vitrox.privalia.Model.FullMovie;
import squaring.vitrox.privalia.Model.MovieDetail;
import squaring.vitrox.privalia.Model.Poster;
import squaring.vitrox.privalia.Model.SearchResult;
import squaring.vitrox.privalia.Network.ApiService;
import squaring.vitrox.privalia.Network.TmbdService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MainActivity extends BaseActivity {

    private RecyclerView mMovieListRecyclerView;
    private MoviesAdapter mMovieAdapter;
    private LinearLayoutManager mLayoutManager;
    private EditText searchText;
    private Boolean mIsLoading;
    private boolean isPopular;
    private ProgressBar progressBar;
    RxTextView rxTextView;

    @Inject
    ApiService apiService;

    @Inject
    TmbdService tmbdService;

    @Inject
    PaginationHelper paginationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getComponent().inject(this);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mMovieListRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchText = (EditText) findViewById(R.id.search_edittext);
        mMovieListRecyclerView.addOnScrollListener(myScrollListener);
        mLayoutManager = new LinearLayoutManager(this);
        mMovieListRecyclerView.setLayoutManager(mLayoutManager);
        mMovieAdapter = new MoviesAdapter(this);
        mMovieListRecyclerView.setAdapter(mMovieAdapter);
        //Since the begin  will subscribe to the textView textchanges events with the method:
        startHandleSearch();
        //then it will be loaded the popular items as was recquired
        loadPopularItems();
    }

    //load popular items is the method that manage the first page objects and the next page on scroll using a paginationHelper
    //then we have a flatmap in this case because we dont need to cancel previous subscriptions or previous network calls
    private void loadPopularItems() {
        loading(true);
        isPopular = true;
        final String page = paginationHelper.getCurrentPage() == 0 ? null : String.valueOf(paginationHelper.getNextPage());
        apiService.getPopularMovies(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<List<MovieDetail>>, Observable<MovieDetail>>() {
                    @Override
                    public Observable<MovieDetail> call(Response<List<MovieDetail>> listResponse) {
                        if (page == null) {
                            Headers headerList = listResponse.headers();
                            paginationHelper = new PaginationHelper(Integer.parseInt(headerList.get(Config.TOTAL_PAG_NUMBER)), Integer.parseInt(headerList.get(Config.ITEMS_PAG_LIMIT)));
                            paginationHelper.setCurrentPage(1);
                        }
                        return Observable.from(listResponse.body());
                    }
                }).subscribe(new Action1<MovieDetail>() {
            @Override
            public void call(final MovieDetail movieDetail) {
                int id = movieDetail.getIds().getTmdb();
                tmbdService.getMoviesImageList(String.valueOf(id)).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread()).subscribe(new Observer<Poster>() {
                    @Override
                    public void onCompleted() {
                        loading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onNext(Poster poster) {
                        mMovieAdapter.addOneData(new FullMovie(movieDetail, poster.getPoster_path()));
                    }
                });

            }
        });
    }

    /*This method allows to manage the textchanges subscription, it's being used a switchmap that automatically desubscribe from the old subscription
     this means that on unsubscribe automatically calls the cancel method on retrofit and we have the desired behaviour
    When the usercontinues typing, the old search should be cancelled and a search for the new termmust be started .*/

    private void startHandleSearch() {
        final String page = paginationHelper.getCurrentPage() == 0 ? null : String.valueOf(paginationHelper.getNextPage());
        rxTextView.textChanges(searchText)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        loading(true);
                        return charSequence.length() > 0;
                    }
                })
                .debounce(250, MILLISECONDS)
                .switchMap(new Func1<CharSequence, Observable<SearchResult>>() {
                    @Override
                    public Observable<SearchResult> call(CharSequence charSequence) {
                        isPopular = false;
                        return apiService.getMoviebySearch(charSequence.toString(), page)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .switchMap(new Func1<Response<List<SearchResult>>, Observable<SearchResult>>() {
                                    @Override
                                    public Observable<SearchResult> call(Response<List<SearchResult>> listResponse) {
                                        if (page == null) {
                                            Headers headerList = listResponse.headers();
                                            paginationHelper = new PaginationHelper(Integer.parseInt(headerList.get(Config.TOTAL_PAG_NUMBER)), Integer.parseInt(headerList.get(Config.ITEMS_PAG_LIMIT)));
                                            paginationHelper.setCurrentPage(1);
                                        }
                                        resetAdapter();
                                        return Observable.from(listResponse.body());
                                    }
                                });
                    }
                }).subscribe(new Action1<SearchResult>() {
            @Override
            public void call(final SearchResult searchResult) {
                MovieDetail movieDetail = searchResult.getMovieDetail();
                int id = movieDetail.getIds().getTmdb();
                tmbdService.getMoviesImageList(String.valueOf(id)).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread()).subscribe(new Observer<Poster>() {
                    @Override
                    public void onCompleted() {
                        loading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onNext(Poster poster) {
                        mMovieAdapter.addOneData(new FullMovie(searchResult.getMovieDetail(), poster.getPoster_path()));
                    }
                });
            }
        });
    }

    //it was necessary to create this extra method because actually the workflow of rxJava is
    // different when we are just getting more pages
    private void loadMoreTextedItems() {
        loading(true);
        final String page = String.valueOf(paginationHelper.getNextPage());
        apiService.getMoviebySearch(searchText.getText().toString(), page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .switchMap(new Func1<Response<List<SearchResult>>, Observable<SearchResult>>() {
                    @Override
                    public Observable<SearchResult> call(Response<List<SearchResult>> listResponse) {
                        return Observable.from(listResponse.body());
                    }

                }).subscribe(new Action1<SearchResult>() {
            @Override
            public void call(final SearchResult searchResult) {
                MovieDetail movieDetail = searchResult.getMovieDetail();
                int id = movieDetail.getIds().getTmdb();
                tmbdService.getMoviesImageList(String.valueOf(id)).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread()).subscribe(new Observer<Poster>() {
                    @Override
                    public void onCompleted() {
                        loading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onNext(Poster poster) {
                        mMovieAdapter.addOneData(new FullMovie(searchResult.getMovieDetail(), poster.getPoster_path()));
                    }
                });

            }
        });
    }


    //manage the pagination on scrolling
    private RecyclerView.OnScrollListener myScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
            if (!mIsLoading && !paginationHelper.isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= paginationHelper.getMaxItems()) {
                    if (isPopular) {
                        loadPopularItems();
                    } else {
                        loadMoreTextedItems();
                    }
                }
            }
        }
    };

    private void loading(Boolean b) {
        mIsLoading = b;
        if (mIsLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void resetAdapter() {
        paginationHelper.setCurrentPage(0);
        this.runOnUiThread(new Runnable() {
            public void run() {
                mMovieAdapter.resetData();
            }
        });
    }
}
