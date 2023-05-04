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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
public abstract class AdminBaseController<T extends BaseItem, R extends BaseProvider<T>, S extends BaseService<T, R>> {
    protected S service;
    @Autowired
    protected ImportJobService importService;
    protected JwtTokenUtil jwtUtillity;
    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(getGenericName());
    private final int defaultPageSize = 100;

    @GetMapping("/")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public List<T> all(@RequestParam Optional<String> page, @RequestParam Optional<String> size){
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
        return list;
    }
    
    @PostMapping("/")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public T newItem(@RequestBody T newItem){
        return service.getNewItem(newItem);
    }
    
    @GetMapping("/{id}")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public T one(@RequestHeader HttpHeaders headers, @PathVariable UUID id){
        T c = service.getItem(id);
        if(c != null){
            return c;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }
    
    @PutMapping("/{id}")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public T replaceItem(@RequestBody T replaceItem, @PathVariable UUID id){
        T c = service.replaceItem(replaceItem, id);
            return c;
    }
    
    @DeleteMapping("/{id}")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public ResponseEntity<?> deleteItem(@PathVariable UUID id){
        this.service.deleteItem(id);
        return ResponseEntity.accepted().build();
    }
    
    @PostMapping("/search")
    @RolesAllowed({ Role.ADMIN_ROLE })
    public List<T> search(@RequestBody T item, @RequestParam Optional<String> page, @RequestParam Optional<String> size){
        List<T> list;
        if(page.isPresent() && size.isEmpty()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), defaultPageSize);
            Page<T> p = this.service.search(item, pageable);
            list = p.toList();
        } else if(page.isPresent() && size.isPresent()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), Integer.getInteger(size.get(), 10));
            Page<T> p = this.service.search(item, pageable);
            list = p.toList();
        } else {
            list =service.search(item);
        }
        return list;
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
