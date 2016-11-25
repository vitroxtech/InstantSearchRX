package squaring.vitrox.privalia.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by miguelgomez on 11/22/16.
 */

public class Poster {

    @JsonProperty("poster_path")
    private String poster_path;

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
