package squaring.vitrox.privalia.Model;


public class FullMovie {
    private MovieDetail movieDetail;
    private String posterUrl;

    public FullMovie(MovieDetail movdetail,String url)
    {
        this.movieDetail=movdetail;
        this.posterUrl=url;
    }

    public MovieDetail getMovieDetail() {
        return movieDetail;
    }

    public void setMovieDetail(MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

}
