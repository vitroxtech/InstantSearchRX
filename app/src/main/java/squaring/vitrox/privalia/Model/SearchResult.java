package squaring.vitrox.privalia.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by miguelgomez on 11/23/16.
 */

public class SearchResult {

    @JsonProperty("movie")
   private MovieDetail movieDetail;

    public MovieDetail getMovieDetail() {
        return movieDetail;
    }

    public void setMovieDetail(MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
    }
}
