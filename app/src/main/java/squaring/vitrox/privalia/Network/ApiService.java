package squaring.vitrox.privalia.Network;

import java.util.List;

import retrofit.Response;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import squaring.vitrox.privalia.Model.MovieDetail;
import squaring.vitrox.privalia.Model.SearchResult;

public interface ApiService {

    @GET("/movies/popular?extended=full")
    Observable<Response<List<MovieDetail>>> getPopularMovies( @Query("page") String page );

    @GET("/search/movie?extended=full")
    Observable<Response<List<SearchResult>>> getMoviebySearch(@Query("query")String movie, @Query("page") String page);


}
