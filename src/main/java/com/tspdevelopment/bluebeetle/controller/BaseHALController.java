package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.data.model.BaseItem;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.provider.interfaces.BaseProvider;
import com.tspdevelopment.bluebeetle.services.controllerservice.BaseService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author tobiesp
 * @param <T>
 * @param <R>
 * @param <S>
 */
public abstract class BaseHALController<T extends BaseItem, R extends BaseProvider<T>, S extends BaseService<T, R>> extends BaseController<T, R, S> {
    protected final Optional<String> placeHolder = Optional.empty();
    protected final Optional<LocalDate> datePlaceHolder = Optional.empty();
    protected final Optional<UUID> uuidPlaceHolder = Optional.empty();

    private List<Link> SingleBuilderLinkList = null;
    private List<Link> ListBuilderLinkListItem = null;
    private List<Link> ListBuilderLinkList = null;

    @GetMapping(value = "/", produces = { "application/hal+json" })
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public CollectionModel<EntityModel<T>> halAll(@RequestParam Optional<String> page, @RequestParam Optional<String> size){
        List<EntityModel<T>> cList = this.all(page, size).stream()
        .map(c -> getModelForListItem(c))
        .collect(Collectors.toList());

        return getModelForList(cList);
    }
    
    @PostMapping(value = "/", produces = { "application/hal+json" })
    @RolesAllowed({ Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public EntityModel<T> halNewItem(@RequestBody T newItem){
        return getModelForSingle(this.newItem(newItem));
    }
    
    @GetMapping(value = "/{id}", produces = { "application/hal+json" })
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public EntityModel<T> halOne(@PathVariable UUID id){
        return getModelForSingle(this.one(id));
    }
    
    @PutMapping(value = "/{id}", produces = { "application/hal+json" })
    @RolesAllowed({ Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public EntityModel<T> halReplaceItem(@RequestBody T replaceItem, @PathVariable UUID id){
        return getModelForSingle(this.replaceItem(replaceItem, id));
    }
    
    @PostMapping(value = "/search", produces = { "application/hal+json" })
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public CollectionModel<EntityModel<T>> halSearch(@RequestBody T item, @RequestParam Optional<String> page, @RequestParam Optional<String> size){
        List<EntityModel<T>> cList = this.search(item, page, size).stream()
				.map(c -> getModelForListItem(c))
				.collect(Collectors.toList());

        return getModelForList(cList);
    }
    
    protected Link[] getLinkListForSingle(UUID id) {
        Link list[];
        int preListSize;
        if(id == null) {
            preListSize = 2;
        } else {
            preListSize = 3;
        }
        if(SingleBuilderLinkList != null) {
            list = new Link[preListSize+this.SingleBuilderLinkList.size()];
            if(id != null) {
                list[0] = linkTo(methodOn(this.getClass()).one(id)).withSelfRel();
                list[1] = linkTo(methodOn(this.getClass()).all(placeHolder, placeHolder)).withRel("all");
                list[2] = linkTo(methodOn(this.getClass()).search(null, placeHolder, placeHolder)).withRel("search");
            } else {
                list[0] = linkTo(methodOn(this.getClass()).all(placeHolder, placeHolder)).withRel("all");
                list[1] = linkTo(methodOn(this.getClass()).search(null, placeHolder, placeHolder)).withRel("search");
            }
            for(int i = 0; i<this.SingleBuilderLinkList.size(); i++) {
                list[i+preListSize] = this.SingleBuilderLinkList.get(i);
            }
        } else {
           list = new Link[preListSize];
           if(id != null) {
                list[0] = linkTo(methodOn(this.getClass()).one(id)).withSelfRel();
                list[1] = linkTo(methodOn(this.getClass()).all(placeHolder, placeHolder)).withRel("all");
                list[2] = linkTo(methodOn(this.getClass()).search(null, placeHolder, placeHolder)).withRel("search");
           } else {
                list[0] = linkTo(methodOn(this.getClass()).all(placeHolder, placeHolder)).withRel("all");
                list[1] = linkTo(methodOn(this.getClass()).search(null, placeHolder, placeHolder)).withRel("search");
           }
        }
        return list;
    }
    
    protected Link[] getLinkListForListItem(UUID id) {
        Link list[];
        if(ListBuilderLinkListItem != null) {
            list = new Link[1+this.ListBuilderLinkListItem.size()];
            list[0] = linkTo(methodOn(this.getClass()).one(id)).withSelfRel();
            for(int i = 0; i<this.ListBuilderLinkListItem.size(); i++) {
                list[i+1] = this.ListBuilderLinkListItem.get(i);
            }
        } else {
           list = new Link[1];
           list[0] = linkTo(methodOn(this.getClass()).one(id)).withSelfRel();
        }
        return list;
    }
    
    protected Link[] getLinkListForList() {
        Link list[];
        if(ListBuilderLinkList != null) {
            list = new Link[2+this.ListBuilderLinkList.size()];
            list[0] = linkTo(methodOn(this.getClass()).all(placeHolder, placeHolder)).withRel("all");
            list[1] = linkTo(methodOn(this.getClass()).search(null, placeHolder, placeHolder)).withRel("search");
            for(int i = 0; i<this.ListBuilderLinkList.size(); i++) {
                list[i+2] = this.ListBuilderLinkList.get(i);
            }
        } else {
           list = new Link[2];
           list[0] = linkTo(methodOn(this.getClass()).all(placeHolder, placeHolder)).withRel("all");
           list[1] = linkTo(methodOn(this.getClass()).search(null, placeHolder, placeHolder)).withRel("search");
        }
        return list;
    }
    
    protected EntityModel<T> getModelForSingle(T c) {
        return EntityModel.of(c, getLinkListForSingle(c.getId()));
    }
    
    protected EntityModel<T> getModelForListItem(T c) {
        return EntityModel.of(c, getLinkListForListItem(c.getId()));
    }
    
    protected CollectionModel<EntityModel<T>> getModelForList(List<EntityModel<T>> l) {
        return CollectionModel.of(l, getLinkListForList());
    }

    protected void AddLinkForListItem(Link link) {
        if(this.ListBuilderLinkListItem == null) {
            this.ListBuilderLinkListItem = new ArrayList<>();
        }
        this.ListBuilderLinkListItem.add(link);
    }

    protected void AddLinkForList(Link link) {
        if(this.ListBuilderLinkList == null) {
            this.ListBuilderLinkList = new ArrayList<>();
        }
        this.ListBuilderLinkList.add(link);
    }

    protected void AddLinkForSingle(Link link) {
        if(this.SingleBuilderLinkList == null) {
            this.SingleBuilderLinkList = new ArrayList<>();
        }
        this.SingleBuilderLinkList.add(link);
    }
}
