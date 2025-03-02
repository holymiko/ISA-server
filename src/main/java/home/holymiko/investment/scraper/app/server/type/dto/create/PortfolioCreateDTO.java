package home.holymiko.investment.scraper.app.server.type.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PortfolioCreateDTO {

    private final String owner;
    private final List<Long> investmentIds;

}
