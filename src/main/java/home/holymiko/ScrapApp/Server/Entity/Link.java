package home.holymiko.ScrapApp.Server.Entity;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Link {
    @Id
    @GeneratedValue
    private Long Id;

    @NotNull
    private String link;

    public Link() {
    }

    public Link(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
