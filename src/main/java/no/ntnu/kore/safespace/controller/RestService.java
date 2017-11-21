package no.ntnu.kore.safespace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface RestService<T, I> {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll();

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public ResponseEntity getOne(I id);

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity add(T t);

    public ValidCheckResult validPost(T newEntity);

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public ResponseEntity update(I id, T t);

    public ValidCheckResult validPut(T newEntity, I id);

}
