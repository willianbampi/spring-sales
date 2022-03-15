package com.next.ecommerce.rest.controller;

import java.util.List;

import javax.validation.Valid;

import com.next.ecommerce.domain.entity.Item;
import com.next.ecommerce.domain.repository.ItemRepository;

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
@RequestMapping("/api/item")
@Api("Item API")
public class ItemController {

    private static String NOT_FOUND_MESSAGE = "Not Found";

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("{id}")
    @ApiOperation("Get item details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description =  "Success."),
        @ApiResponse(responseCode = "404", description = "Item not found.")
    })
    public Item getItemById(@PathVariable @ApiParam("Item id") Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Save a new item")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Success."),
        @ApiResponse(responseCode = "400", description = "Validation error(s).")
    })
    public Item save(@RequestBody @Valid @ApiParam("Item") Item item){
        return itemRepository.save(item);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Update an item")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description =  "Success."),
        @ApiResponse(responseCode = "404", description = "Item not found.")
    })
    public void update(@PathVariable @ApiParam("Item id") Integer id, @RequestBody @Valid @ApiParam("Item") Item item) {
        itemRepository.findById(id)
            .map(searchedItem -> {
                item.setId(searchedItem.getId());
                itemRepository.save(item);
                return item;
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete an item")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description =  "Success."),
        @ApiResponse(responseCode = "404", description = "Item not found.")
    })
    public void delete(@PathVariable @ApiParam("Item id") Integer id) {
        itemRepository.findById(id)
            .map(searchedItem -> {
                itemRepository.delete(searchedItem);
                return searchedItem;
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE));
    }

    @GetMapping
    @ApiOperation("Find all items that containig the data")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description =  "Success."),
        @ApiResponse(responseCode = "404", description = "Items not found.")
    })
    public List<Item> findAllContaining(@ApiParam("Item") Item item) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                                    .withIgnoreCase()
                                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(item, matcher);
        return itemRepository.findAll(example);
    }
    
}
