package squaring.vitrox.privalia.Network;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import squaring.vitrox.privalia.Model.Poster;


public interface TmbdService {

    @GET("/3/movie/{id}")
    Observable<Poster> getMoviesImageList(@Path("id")String id);
}
