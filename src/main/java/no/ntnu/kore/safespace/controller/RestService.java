package no.ntnu.kore.safespace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public interface RestService<T, I> {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<T>> getAll();

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public ResponseEntity<T> getOne(I id);

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<T> add(T t);

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<T> update(I id, T t);

}
