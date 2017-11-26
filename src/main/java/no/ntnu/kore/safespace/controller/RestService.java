package no.ntnu.kore.safespace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Interface containing methods for a rest service can implement. Used mostly to
 * keep the methods and mappings of different RestControllers consistent.
 * @author robert
 * @param <T> type of objects the rest service will handle
 * @param <I> type of id the rest service will use
 */
public interface RestService<T, I> {

    /**
     * Returns all instances of T
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll();

    /**
     * Returns the instance of T with given id
     * @param id id of instance for fetch
     * @return instance of T with given id
     */
    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public ResponseEntity getOne(I id);

    /**
     * Adds a new instance of T. Returns a ResponseEntity containing the newly added instance of T with its id or if a error occur of there is
     * a problem with the submitted instance of T a ValidCheckResult object will be returned. Should check response code
     * of the request to decide which is returned.
     * @param t the instance to add
     * @return ResponseEntity containing the instance of T that was added or a instance of ValidCheckResult if an error occured.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity add(T t);

    /**
     * Checks if the new instance of T is valid for addition
     * @param newEntity the new instance of T to be added.
     * @return ValidCheckResult that will indicate if the new instance of T is valid or not, if not it should also include
     * a message to why it's not valid
     */
    public ValidCheckResult validPost(T newEntity);

    /**
     * Updates an existing instance of T with the given instance. Will return a ResponseEntity containing the newly updated
     * instance of T or if an error occured a ValidCheckResult will be returned, this should contain a message to what error
     * occurec
     * @param id id of the instance to update
     * @param t the new instance of T that should replace the existing with given id
     * @return ResponseEntity containing the instance of T that was updated or a instance of ValidCheckResult if an error occured.
     */
    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public ResponseEntity update(I id, T t);

    /**
     * Check if the new instance is valid to update an existing one.
     * @param newEntity the new instance of T to update the old one.
     * @param id id of the instance to be updated.
     * @return ValidCheckResult that will indicate if the new instance of T is valid or not, if not it should also include
     * a message to why it's not valid
     */
    public ValidCheckResult validPut(T newEntity, I id);

}
