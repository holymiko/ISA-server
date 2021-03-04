package home.holymiko.ScrapApp.Server.Controller;

import home.holymiko.ScrapApp.Server.Scrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/link")         // Na url/api/v1/herbivores se zavola HTTP request
public class LinkController {
    private final Scrap scrap;

    @Autowired
    public LinkController(Scrap scrap) {
        this.scrap = scrap;
    }

    public void saveGoldLinks() {
        this.scrap.scrapGoldLinks();
    }

    public void saveAllLinks() {
        this.scrap.scrapAllLinks();
    }
}
