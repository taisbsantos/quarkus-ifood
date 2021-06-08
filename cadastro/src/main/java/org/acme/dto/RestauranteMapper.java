package org.acme.dto;

import org.acme.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    @Mapping(target = "nome", source = "nomeFantasia" )
    @Mapping(target = "id", ignore = true )
    @Mapping(target = "dataCriacao", ignore = true )
    @Mapping(target = "dataAtualizacao", ignore = true )
    @Mapping(target = "localizacao.id", ignore = true )


    @Mapping(target = "nome", source = "nomeFantasia" )
    public void toRestaurante(AtualizarRestauranteDTO dto, @MappingTarget Restaurante restaurante);

    @Mapping(target = "nome", source = "nomeFantasia" )
    @Mapping(target = "dataCriacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
    public RestauranteDTO toRestauranteDTO(Restaurante r);

}
