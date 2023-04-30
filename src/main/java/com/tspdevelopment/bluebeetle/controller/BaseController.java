package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.csv.CSVPreference;
import com.tspdevelopment.bluebeetle.csv.CSVWriter;
import com.tspdevelopment.bluebeetle.data.model.BaseItem;
import com.tspdevelopment.bluebeetle.data.model.ImportJob;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.provider.interfaces.BaseProvider;
import com.tspdevelopment.bluebeetle.response.ImportJobResponse;
import com.tspdevelopment.bluebeetle.response.RequestStatus;
import com.tspdevelopment.bluebeetle.services.ImportJobService;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 * @param <T>
 */
public abstract class BaseController<T extends BaseItem> {
    protected BaseProvider<T> provider;
    @Autowired
    protected ImportJobService importService;
    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(getGenericName());
    
    @GetMapping("/")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    CollectionModel<EntityModel<T>> all(){
        List<EntityModel<T>> cList = provider.findAll().stream()
        .map(c -> getModel(c))
        .collect(Collectors.toList());

        return CollectionModel.of(cList, 
                    linkTo(methodOn(this.getClass()).search(null)).withSelfRel(),
                    linkTo(methodOn(this.getClass()).all()).withSelfRel());
    }
    
    @PostMapping("/")
    @RolesAllowed({ Role.WRITE_ROLE, Role.ADMIN_ROLE })
    EntityModel<T> newItem(@RequestBody T newItem){
        return getModel(provider.create(newItem));
    }
    
    @GetMapping("/{id}")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    EntityModel<T> one(@PathVariable UUID id){
        Optional<T> c = provider.findById(id);
        if(c.isPresent()){
            return getModel(c.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }
    
    @PutMapping("/{id}")
    @RolesAllowed({ Role.WRITE_ROLE, Role.ADMIN_ROLE })
    EntityModel<T> replaceItem(@RequestBody T replaceItem, @PathVariable UUID id){
        T c = provider.update(replaceItem, id);
            return getModel(c);
    }
    
    @DeleteMapping("/{id}")
    @RolesAllowed({ Role.WRITE_ROLE, Role.ADMIN_ROLE })
    ResponseEntity<?> deleteItem(@PathVariable UUID id){
        this.provider.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/search")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    CollectionModel<EntityModel<T>> search(@RequestBody T item){
        List<EntityModel<T>> cList = provider.search(item).stream()
				.map(c -> getModel(c))
				.collect(Collectors.toList());

		return CollectionModel.of(cList, linkTo(methodOn(this.getClass()).all()).withSelfRel());
    }
    
    protected EntityModel<T> getModel(T c) {
        return EntityModel.of(c, //
                linkTo(methodOn(this.getClass()).one(c.getId())).withSelfRel(),
                linkTo(methodOn(this.getClass()).search(null)).withSelfRel(),
                linkTo(methodOn(this.getClass()).all()).withSelfRel());
    }
    
    private String getGenericName()
    {
        return ((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]).getTypeName();
    }
    
    protected ResponseEntity<?> exportToCSV(HttpServletResponse response, String[] csvHeader, String[] nameMapping) throws IOException {
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
    
    protected <K> ImportJobResponse importCSV(MultipartFile file, Class<K> clazz) {
        if(!isCSVFile(file)) {
            logger.error("File not of type CSV: " + file.getOriginalFilename());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File must be of type CSV!");
        }
        
        ImportJob jobImport = new ImportJob();
        jobImport.setFileName(file.getOriginalFilename());
        try {
            jobImport.setContent(file.getBytes());
            jobImport = importService.addJobToDB(jobImport);
            importService.postJobWithFile(jobImport.getId(), clazz);
            ImportJobResponse response = new ImportJobResponse(jobImport.getId(), RequestStatus.SUBMITTED);
            return response;
        } catch (Exception ex) {
            logger.error("Unable to read the CSV file", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Import failed!");
        } catch (Throwable ex) {
            logger.error("Unable to get job status", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Import failed!");
        }
    }
    
    private boolean isCSVFile(MultipartFile file) {
        return "text/csv".equals(file.getContentType()) || "application/csv".equals(file.getContentType());
    }
}