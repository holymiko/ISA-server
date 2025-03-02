package home.holymiko.investment.scraper.app.server.mapper;

import home.holymiko.investment.scraper.app.server.type.dto.simple.LinkDTO;
import home.holymiko.investment.scraper.app.server.type.entity.Link;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LinkMapper {

    public abstract LinkDTO toDTO(Link entity);

    public abstract List<LinkDTO> toDTO(List<Link> entities);
}
