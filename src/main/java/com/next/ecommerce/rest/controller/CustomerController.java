package com.next.ecommerce.rest.controller;

import java.util.List;

import javax.validation.Valid;

import com.next.ecommerce.domain.entity.Customer;
import com.next.ecommerce.domain.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/customer")
@Api("Customer API")
public class CustomerController {

    private static String NOT_FOUND_MESSAGE = "Not Found";

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("{id}")
    @ApiOperation("Get customer details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description =  "Success."),
        @ApiResponse(responseCode = "404", description = "Customer not found.")
    })
    public Customer getCustomerById(@PathVariable @ApiParam("Customer id") Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Save a new customer")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Success."),
        @ApiResponse(responseCode = "400", description = "Validation error(s).")
    })
    public Customer save(@RequestBody @Valid @ApiParam("Customer") Customer customer) {
        return customerRepository.save(customer);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Update a customer")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description =  "Success."),
        @ApiResponse(responseCode = "404", description = "Customer not found.")
    })
    public void update(@PathVariable Integer id, @RequestBody @Valid @ApiParam("Customer") Customer customer) {
        customerRepository.findById(id)
                .map(searchedCustomer -> {
                    customer.setId(searchedCustomer.getId());
                    customerRepository.save(customer);
                    return customer;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete a customer")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description =  "Success."),
        @ApiResponse(responseCode = "404", description = "Customer not found.")
    })
    public void delete(@PathVariable @ApiParam("Customer id") Integer id) {
        customerRepository.findById(id)
                .map(searchedCustomer -> {
                    customerRepository.delete(searchedCustomer);
                    return searchedCustomer;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE));
    }

    @GetMapping
    @ApiOperation("Find all customers that containig the data")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description =  "Success."),
        @ApiResponse(responseCode = "404", description = "Customers not found.")
    })
    public List<Customer> findAllContaining(@ApiParam("Customer") Customer customer) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                                    .withIgnoreCase()
                                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(customer, matcher);
        return customerRepository.findAll(example);
    }
    
}
