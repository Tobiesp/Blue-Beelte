package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.csv.CSVPreference;
import com.tspdevelopment.bluebeetle.csv.CSVWriter;
import com.tspdevelopment.bluebeetle.data.model.BaseItem;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.helpers.JwtTokenUtil;
import com.tspdevelopment.bluebeetle.provider.interfaces.BaseProvider;
import com.tspdevelopment.bluebeetle.services.ImportJobService;
import com.tspdevelopment.bluebeetle.services.controllerservice.BaseService;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 * @param <T>
 * @param <R>
 * @param <S>
 */
public abstract class AdminHALBaseController<T extends BaseItem, R extends BaseProvider<T>, S extends BaseService<T, R>> {
    protected S service;
    @Autowired
    protected ImportJobService importService;
    protected JwtTokenUtil jwtUtillity;
    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(getGenericName());
    protected final int defaultPageSize = 100;
    protected final Optional<String> placeHolder = Optional.empty();
    protected final Optional<LocalDate> datePlaceHolder = Optional.empty();
    protected final Optional<UUID> uuidPlaceHolder = Optional.empty();

    private List<Link> SingleBuilderLinkList = null;
    private List<Link> ListBuilderLinkListItem = null;
    private List<Link> ListBuilderLinkList = null;

    @GetMapping("/")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public CollectionModel<EntityModel<T>> all(@RequestParam Optional<String> page, @RequestParam Optional<String> size){
        List<T> list;
        if(page.isPresent() && size.isEmpty()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), defaultPageSize);
            Page<T> p = this.service.getAllItems(pageable);
            list = p.toList();
        } else if(page.isPresent() && size.isPresent()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), Integer.getInteger(size.get(), 10));
            Page<T> p = this.service.getAllItems(pageable);
            list = p.toList();
        } else {
            list = service.getAllItems();
        }
        List<EntityModel<T>> cList = list.stream()
        .map(c -> getModelForListItem(c))
        .collect(Collectors.toList());

        return getModelForList(cList);
    }
    
    @PostMapping("/")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public EntityModel<T> newItem(@RequestBody T newItem){
        return getModelForSingle(service.getNewItem(newItem));
    }
    
    @GetMapping("/{id}")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public EntityModel<T> one(@RequestHeader HttpHeaders headers, @PathVariable UUID id){
        T c = service.getItem(id);
        if(c != null){
            return getModelForSingle(c);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }
    
    @PutMapping("/{id}")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public EntityModel<T> replaceItem(@RequestBody T replaceItem, @PathVariable UUID id){
        T c = service.replaceItem(replaceItem, id);
            return getModelForSingle(c);
    }
    
    @DeleteMapping("/{id}")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public ResponseEntity<?> deleteItem(@PathVariable UUID id){
        this.service.deleteItem(id);
        return ResponseEntity.accepted().build();
    }
    
    @PostMapping("/search")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public CollectionModel<EntityModel<T>> search(@RequestBody T item, @RequestParam Optional<String> page, @RequestParam Optional<String> size){
        List<T> list;
        if(page.isPresent() && size.isEmpty()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), defaultPageSize);
            Page<T> p = this.service.getAllItems(pageable);
            list = p.toList();
        } else if(page.isPresent() && size.isPresent()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), Integer.getInteger(size.get(), 10));
            Page<T> p = this.service.getAllItems(pageable);
            list = p.toList();
        } else {
            list = service.getAllItems();
        }
        List<EntityModel<T>> cList = list.stream()
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
                list[0] = linkTo(methodOn(this.getClass()).one(null, id)).withSelfRel();
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
                list[0] = linkTo(methodOn(this.getClass()).one(null, id)).withSelfRel();
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
            list[0] = linkTo(methodOn(this.getClass()).one(null, id)).withSelfRel();
            for(int i = 0; i<this.ListBuilderLinkListItem.size(); i++) {
                list[i+1] = this.ListBuilderLinkListItem.get(i);
            }
        } else {
           list = new Link[1];
           list[0] = linkTo(methodOn(this.getClass()).one(null, id)).withSelfRel();
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
    
    protected CollectionModel<EntityModel<T>> getModelForList(List<EntityModel<T>> l) {
        Link list[];
        if(ListBuilderLinkList != null) {
            list = new Link[2+this.ListBuilderLinkList.size()];
            list[0] = linkTo(methodOn(this.getClass()).all(placeHolder, placeHolder)).withSelfRel();
            list[1] = linkTo(methodOn(this.getClass()).search(null, placeHolder, placeHolder)).withSelfRel();
            for(int i = 0; i<this.ListBuilderLinkList.size(); i++) {
                list[i+2] = this.ListBuilderLinkList.get(i);
            }
        } else {
           list = new Link[2];
           list[0] = linkTo(methodOn(this.getClass()).all(placeHolder, placeHolder)).withSelfRel();
            list[1] = linkTo(methodOn(this.getClass()).search(null, placeHolder, placeHolder)).withSelfRel();
        }
        return CollectionModel.of(l, list);
    }
    
    protected EntityModel<T> getModelForSingle(T c) {
        Link list[];
        if(SingleBuilderLinkList != null) {
            list = new Link[3+this.SingleBuilderLinkList.size()];
            list[0] = linkTo(methodOn(this.getClass()).one(null, c.getId())).withSelfRel();
            list[1] = linkTo(methodOn(this.getClass()).search(null, placeHolder, placeHolder)).withSelfRel();
            list[2] = linkTo(methodOn(this.getClass()).all(placeHolder, placeHolder)).withSelfRel();
            for(int i = 0; i<this.SingleBuilderLinkList.size(); i++) {
                list[i+3] = this.SingleBuilderLinkList.get(i);
            }
        } else {
           list = new Link[3];
           list[0] = linkTo(methodOn(this.getClass()).one(null, c.getId())).withSelfRel();
           list[1] = linkTo(methodOn(this.getClass()).search(null, placeHolder, placeHolder)).withSelfRel();
           list[2] = linkTo(methodOn(this.getClass()).all(placeHolder, placeHolder)).withSelfRel();
        }
        return EntityModel.of(c, list);
    }
    
    protected EntityModel<T> getModelForListItem(T c) {
        Link list[];
        if(ListBuilderLinkListItem != null) {
            list = new Link[1+this.ListBuilderLinkListItem.size()];
            list[0] = linkTo(methodOn(this.getClass()).one(null, c.getId())).withSelfRel();
            for(int i = 0; i<this.ListBuilderLinkListItem.size(); i++) {
                list[i+3] = this.ListBuilderLinkListItem.get(i);
            }
        } else {
           list = new Link[1];
           list[0] = linkTo(methodOn(this.getClass()).one(null, c.getId())).withSelfRel();
        }
        return EntityModel.of(c, list);
    }

    protected void AddLinkForList(Link link) {
        if(this.ListBuilderLinkList == null) {
            this.ListBuilderLinkList = new ArrayList<>();
        }
        this.ListBuilderLinkList.add(link);
    }

    protected void AddLinkForListItem(Link link) {
        if(this.ListBuilderLinkListItem == null) {
            this.ListBuilderLinkListItem = new ArrayList<>();
        }
        this.ListBuilderLinkListItem.add(link);
    }

    protected void AddLinkForSingle(Link link) {
        if(this.SingleBuilderLinkList == null) {
            this.SingleBuilderLinkList = new ArrayList<>();
        }
        this.SingleBuilderLinkList.add(link);
    }
    
    private String getGenericName()
    {
        return ((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]).getTypeName();
    }
    
    protected ResponseEntity<?> baseExportToCSV(HttpServletResponse response, String[] csvHeader, String[] nameMapping) {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + getGenericName() + "_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
         
        List<T> list = this.service.getAllItems();
 
        try {
            CSVWriter csvWriter = new CSVWriter(response.getWriter(), CSVPreference.STANDARD_PREFERENCE);

            csvWriter.writeHeader(csvHeader);

            for (T i : list) {
                try {
                    csvWriter.write(i, nameMapping);
                } catch (NoSuchFieldException ex) {
                    logger.error("Unable to find the Specified field in the Object.", ex);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Export failed!");
                }
            }
        } catch (IOException ex) {
            logger.error("Unable to read CSV file.", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Export failed!");
        }
        return ResponseEntity.ok().build();
    }
    
    protected abstract User getUser(UUID id);
    
    protected UUID getUserIdFromToken(HttpHeaders headers) {
        List<String> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access resource.");
        }
        if(authHeader.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access resource.");
        }
        String[] s = authHeader.get(0).split(" ");
        if(s.length == 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access resource.");
        }
        return this.jwtUtillity.getUserId(s[1]);
    }
}
