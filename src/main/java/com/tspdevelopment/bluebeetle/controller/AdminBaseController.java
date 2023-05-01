package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.csv.CSVPreference;
import com.tspdevelopment.bluebeetle.csv.CSVWriter;
import com.tspdevelopment.bluebeetle.data.model.BaseItem;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.helpers.JwtTokenUtil;
import com.tspdevelopment.bluebeetle.provider.interfaces.BaseProvider;
import com.tspdevelopment.bluebeetle.services.ImportJobService;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 * @param <T>
 * @param <R>
 */
public abstract class AdminBaseController<T extends BaseItem, R extends BaseProvider<T>> {
    protected R provider;
    @Autowired
    protected ImportJobService importService;
    protected JwtTokenUtil jwtUtillity;
    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(getGenericName());

    private List<Link> SingleBuilderLinkList = null;
    private List<Link> ListBuilderLinkList = null;

    @GetMapping("/")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public CollectionModel<EntityModel<T>> all(){
        List<EntityModel<T>> cList = provider.findAll().stream()
        .map(c -> getModelForList(c))
        .collect(Collectors.toList());

        return CollectionModel.of(cList, 
                    linkTo(methodOn(this.getClass()).search(null)).withSelfRel(),
                    linkTo(methodOn(this.getClass()).all()).withSelfRel());
    }
    
    @PostMapping("/")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public EntityModel<T> newItem(@RequestBody T newItem){
        return getModelForSingle(provider.create(newItem));
    }
    
    @GetMapping("/{id}")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public EntityModel<T> one(@RequestHeader HttpHeaders headers, @PathVariable UUID id){
        
        Optional<T> c = provider.findById(id);
        if(c.isPresent()){
            return getModelForSingle(c.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }
    
    @PutMapping("/{id}")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public EntityModel<T> replaceItem(@RequestBody T replaceItem, @PathVariable UUID id){
        T c = provider.update(replaceItem, id);
            return getModelForSingle(c);
    }
    
    @DeleteMapping("/{id}")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public ResponseEntity<?> deleteItem(@PathVariable UUID id){
        this.provider.delete(id);
        return ResponseEntity.accepted().build();
    }
    
    @PostMapping("/search")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public CollectionModel<EntityModel<T>> search(@RequestBody T item){
        List<EntityModel<T>> cList = provider.search(item).stream()
				.map(c -> getModelForList(c))
				.collect(Collectors.toList());

		return CollectionModel.of(cList, 
                    linkTo(methodOn(this.getClass()).search(null)).withSelfRel(),
                    linkTo(methodOn(this.getClass()).all()).withSelfRel());
    }
    
    protected EntityModel<T> getModelForSingle(T c) {
        Link list[];
        if(SingleBuilderLinkList != null) {
            list = new Link[3+this.SingleBuilderLinkList.size()];
            list[0] = linkTo(methodOn(this.getClass()).one(null, c.getId())).withSelfRel();
            list[1] = linkTo(methodOn(this.getClass()).search(null)).withSelfRel();
            list[2] = linkTo(methodOn(this.getClass()).all()).withSelfRel();
            for(int i = 0; i<this.SingleBuilderLinkList.size(); i++) {
                list[i+3] = this.SingleBuilderLinkList.get(i);
            }
        } else {
           list = new Link[3];
           list[0] = linkTo(methodOn(this.getClass()).one(null, c.getId())).withSelfRel();
           list[1] = linkTo(methodOn(this.getClass()).search(null)).withSelfRel();
           list[2] = linkTo(methodOn(this.getClass()).all()).withSelfRel();
        }
        return EntityModel.of(c, list);
    }
    
    protected EntityModel<T> getModelForList(T c) {
        Link list[];
        if(ListBuilderLinkList != null) {
            list = new Link[1+this.ListBuilderLinkList.size()];
            list[0] = linkTo(methodOn(this.getClass()).one(null, c.getId())).withSelfRel();
            for(int i = 0; i<this.ListBuilderLinkList.size(); i++) {
                list[i+3] = this.ListBuilderLinkList.get(i);
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
    
    protected ResponseEntity<?> baseExportToCSV(HttpServletResponse response, String[] csvHeader, String[] nameMapping) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + getGenericName() + "_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
         
        List<T> list = this.provider.findAll();
 
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
