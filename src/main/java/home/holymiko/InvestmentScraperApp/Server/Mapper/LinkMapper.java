package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.simple.LinkDTO;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Link;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LinkMapper {

    @Mapping(target = "productId", source = "product.id")
    public abstract LinkDTO toDTO(Link entity);

    public abstract List<LinkDTO> toDTO(List<Link> entities);
}
