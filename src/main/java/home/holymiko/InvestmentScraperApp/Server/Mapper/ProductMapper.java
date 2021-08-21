package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toProductDTO(Product entity);

    ProductDTO_AllPrices toProductDTO_AllPrices(Product entity);
//
//    Address mapModelToEntity(com.blueplustechnologies.core.model.Address model);
//
//    Address mergeEntities(@MappingTarget Address targetEntity, Address sourceEntity);
//
//    // required to keep hibernate's managed bag
//    default List<AddressField> mergeAddressFieldLists(@MappingTarget List<AddressField> targetEntityList, List<AddressField> sourceEntityList) {
//        Map<Integer, AddressField> sourceMap = sourceEntityList.stream().filter(f -> f.getId() != null).collect(Collectors.toMap(AddressField::getId, f -> f));
//
//        // remove deleted
//        targetEntityList.removeIf(f -> !sourceMap.containsKey(f.getId()));
//
//        // update
//        targetEntityList.stream().filter(f -> sourceMap.containsKey(f.getId())).forEach(f -> mergeAddressFields(f, sourceMap.get(f.getId())));
//
//        // add new
//        sourceEntityList.stream().filter(f -> f.getId() == null).forEach(f -> targetEntityList.add(mergeAddressFields(new AddressField(), f)));
//
//        return targetEntityList;
//    }
//
//    @Mapping(target = "address", ignore = true) // breaks circular dependency
//    AddressField mergeAddressFields(@MappingTarget AddressField targetEntity, AddressField sourceEntity);
//
//    @AfterMapping
//    static void setAddressToAddressFields(@MappingTarget Address entity) {
//        // fix AddressField's address reference
//        entity.getAddressFields().forEach(f -> f.setAddress(entity));
//    }
//
//    @AfterMapping
//    static void setFieldAddressId(@MappingTarget com.blueplustechnologies.core.model.Address model) {
//        // fix AddressField's address reference
//        model.getAddressFields().forEach(f -> f.setAddressId(model.getId()));
//    }

}
