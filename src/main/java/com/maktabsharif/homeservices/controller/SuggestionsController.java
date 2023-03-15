package com.maktabsharif.homeservices.controller;

import com.maktabsharif.homeservices.domain.Suggestions;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.dto.SuggestionsDto;
import com.maktabsharif.homeservices.mapper.SuggestionsMapper;
import com.maktabsharif.homeservices.service.SuggestionsService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPERT')")
    public Suggestions create(@RequestBody SuggestionsDto suggestionsDto) throws Exception {
        Suggestions suggestions = SuggestionsMapper.INSTANCE.dtoToModel(suggestionsDto);
        return suggestionsService.create(suggestions);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPERT')")
    public Suggestions update(@RequestBody SuggestionsDto suggestionsDto) throws Exception {
        Suggestions suggestions = SuggestionsMapper.INSTANCE.dtoToModel(suggestionsDto);
        return suggestionsService.update(suggestions);
    }

    @GetMapping("/findById/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Suggestions findById(@PathVariable Long suggestionsId) throws Exception {
        return suggestionsService.findById(suggestionsId);
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<Suggestions> findAll() throws Exception {
        return suggestionsService.findAll();
    }

    @DeleteMapping("/deleteById/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String deleteById(@PathVariable Long userId) throws Exception {
        suggestionsService.deleteById(userId);
        return "OK";
    }

    @GetMapping("/findByUserIdAndOrdersId")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXPERT')")
    public Suggestions findByExpertIdAndOrdersId(@RequestParam Long userId, @RequestParam Long ordersId) throws Exception {
        return suggestionsService.findByExpertIdAndOrdersId(userId, ordersId);
    }

    @GetMapping("/findOrdersSuggestionsSortedByExpertSuggestedPrice")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public List<Suggestions> findOrdersSuggestionsSortedByExpertSuggestedPrice(@RequestParam Long ordersId) throws Exception {
        return suggestionsService.findOrdersSuggestionsSortedByExpertSuggestedPrice(ordersId);
    }

    @GetMapping("/findOrdersSuggestionsSortedByExpertPoint")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public List<Suggestions> findOrdersSuggestionsSortedByExpertPoint(@RequestParam Long ordersId) throws Exception {
        return suggestionsService.findOrdersSuggestionsSortedByExpertPoint(ordersId);
    }

    @GetMapping("/findOrdersSuggestionsSortedByExpertPointAndSuggestedPrice")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public List<Suggestions> findOrdersSuggestionsSortedByExpertPointAndSuggestedPrice(@RequestParam Long ordersId) throws Exception {
        return suggestionsService.findOrdersSuggestionsSortedByExpertPointAndSuggestedPrice(ordersId);
    }
    @GetMapping("/selectSuggestion")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public void selectSuggestion(@RequestParam Long ordersId, @RequestParam Long suggestionId) throws Exception {
        suggestionsService.selectSuggestion(ordersId, suggestionId);
    }

}
