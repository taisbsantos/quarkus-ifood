package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import org.acme.dto.AdicionarRestauranteDTO;
import org.acme.dto.AtualizarRestauranteDTO;
import org.acme.dto.RestauranteMapper;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurante")

@RolesAllowed("proprietario")
@SecurityScheme(securitySchemeName = "ifood-oauth", type = SecuritySchemeType.OAUTH2, flows =
@OAuthFlows(password = @OAuthFlow(tokenUrl = "http://localhost:8080/auth/realms/ifood/protocol/openid-connect/token")))
@SecurityRequirement(name = "ifood-oauth")
public class RestauranteResource {

    @Inject
    RestauranteMapper restauranteMapper;

    @GET
    public <List>Restaurante buscar(){
        return (Restaurante) Restaurante.listAll();
    }

    @POST
    @Transactional
    public Response adicionar( Restaurante dto){
        dto.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id,  AtualizarRestauranteDTO dto){
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
        if(restauranteOp.isEmpty()){
            throw new NotFoundException();
        }
        Restaurante restaurante =restauranteOp.get();
        restauranteMapper.toRestaurante(dto, restaurante);

        restaurante.persist();
    }


    @DELETE
    @Path("{id}")
    @Transactional
    public void deletar(@PathParam("id") Long id,  Restaurante dto){
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
        restauranteOp.ifPresentOrElse(Restaurante::delete,
            () -> { throw new NotFoundException();
        } );
        Restaurante restaurante =restauranteOp.get();
        restaurante.nome = dto.nome;
        restaurante.persist();
    }
    //Pratos
    @GET
    @Path("{idRestaurante}/pratos")
    @Tag(name = "prato")
    public List<Restaurante> buscarPratos(@PathParam("idRestaurante") Long idRestaurante){

        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if(restauranteOp.isEmpty()){
            throw new NotFoundException("Restaurante não existe");
        }
        return Prato.list("restaurante", restauranteOp.get());
    }

    @POST
    @Tag(name = "prato")
    @Path("{idRestaurante}/pratos")
    @Transactional
    public Response adicionarPratos( @PathParam("idRestaurante") Long idRestaurante, Prato dto){
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if(restauranteOp.isEmpty()){
            throw new NotFoundException("Restaurante não existe");
        }
        Prato prato = new Prato();
        prato.nome = dto.nome;
        prato.descricao = dto.descricao;
        prato.preco = dto.preco;
        prato.restaurante = restauranteOp.get();
        prato.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Tag(name = "prato")
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    public void atualizarPratos(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id,  Prato dto){
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if(restauranteOp.isEmpty()){
            throw new NotFoundException("Restaurante não existe");
        }

        Optional<Prato> pratoOp = Prato.findByIdOptional(id);
        if(pratoOp.isEmpty()){
            throw new NotFoundException("Prato não existe");
        }

        Prato prato =pratoOp.get();
        prato.preco = dto.preco;
        prato.persist();
    }


    @DELETE
    @Tag(name = "prato")

    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    public void deletarPratos(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id){
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if(restauranteOp.isEmpty()){
            throw new NotFoundException("Restaurante não existe");
        }

        Optional<Prato> pratoOp = Prato.findByIdOptional(id);
        pratoOp.ifPresentOrElse(Prato::delete,
                () -> { throw new NotFoundException("restaurante não existe");
                } );

    }

}
