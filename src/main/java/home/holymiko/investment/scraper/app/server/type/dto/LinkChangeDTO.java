package home.holymiko.investment.scraper.app.server.type.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Doesn't include password
 */
@Getter
@Setter
@AllArgsConstructor
public class LinkChangeDTO {

    private Long toProductId;
    private Long fromProductId;
    private Long linkId;

}
