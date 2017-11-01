package no.ntnu.kore.safespace.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public interface RestService<T, I> {

    @RequestMapping(method = RequestMethod.GET)
    public List<T> getAll();

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public T getOne(I id);

    @RequestMapping(method = RequestMethod.POST)
    public T add(T t);

    @RequestMapping(path = "{}", method = RequestMethod.PUT)
    public T update(T t);

}
