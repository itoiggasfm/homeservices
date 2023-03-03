package com.maktabsharif.homeservices.controller;

import com.maktabsharif.homeservices.domain.Suggestions;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.dto.SuggestionsDto;
import com.maktabsharif.homeservices.mapper.SuggestionsMapper;
import com.maktabsharif.homeservices.service.SuggestionsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suggestions")
public class SuggestionsController {

    private final SuggestionsService suggestionsService;

    public SuggestionsController(SuggestionsService suggestionsService) {
        this.suggestionsService = suggestionsService;
    }


    @PostMapping("/create")
    public Suggestions create(@RequestBody SuggestionsDto suggestionsDto) throws Exception {
        Suggestions suggestions = SuggestionsMapper.INSTANCE.dtoToModel(suggestionsDto);
        return suggestionsService.create(suggestions);
    }

    @PutMapping("/update")
    public Suggestions update(@RequestBody SuggestionsDto suggestionsDto) throws Exception {
        Suggestions suggestions = SuggestionsMapper.INSTANCE.dtoToModel(suggestionsDto);
        return suggestionsService.update(suggestions);
    }

    @GetMapping("/findById/{userId}")
    public Suggestions findById(@PathVariable Long suggestionsId) throws Exception {
        return suggestionsService.findById(suggestionsId);
    }

    @GetMapping("/findAll")
    public List<Suggestions> findAll() throws Exception {
        return suggestionsService.findAll();
    }

    @DeleteMapping("/deleteById/{userId}")
    public String deleteById(@PathVariable Long userId) throws Exception {
        suggestionsService.deleteById(userId);
        return "OK";
    }

    @GetMapping("/findByUserIdAndOrdersId")
    public Suggestions findByUserIdAndOrdersId(@RequestParam Long userId, @RequestParam Long ordersId) throws Exception {
        return suggestionsService.findByUserIdAndOrdersId(userId, ordersId);
    }

    @GetMapping("/findOrdersSuggestions")
    public List<Suggestions> findOrdersSuggestions(@RequestParam Long ordersId) throws Exception {
        return suggestionsService.findOrdersSuggestions(ordersId);
    }

    @GetMapping("/selectSuggestion")
    public void selectSuggestion(@RequestParam Long ordersId, @RequestParam Long suggestionId) throws Exception {
        suggestionsService.selectSuggestion(ordersId, suggestionId);
    }

}
