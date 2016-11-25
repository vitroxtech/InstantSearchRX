package squaring.vitrox.privalia.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieDetail {
    @JsonProperty("title")
    private String title;
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("ids")
    private Ids ids;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Ids getIds() {
        return ids;
    }

    public void setIds(Ids ids) {
        this.ids = ids;
    }

    public class Ids {

        @JsonProperty("tmdb")
        private Integer tmdb;

        public Integer getTmdb() {
            return tmdb;
        }

        public void setTmdb(Integer tmdb) {
            this.tmdb = tmdb;
        }
    }
}
