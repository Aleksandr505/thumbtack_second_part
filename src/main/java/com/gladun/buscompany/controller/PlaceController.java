package com.gladun.buscompany.controller;

import com.gladun.buscompany.dto.request.ChoicePlaceDtoRequest;
import com.gladun.buscompany.dto.response.ChoicePlaceDtoResponse;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.service.PlaceService;
import com.gladun.buscompany.service.ServiceUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping(value = "/places/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Integer> getAvailablePlaces(@PathVariable int orderId, @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        return placeService.getAvailablePlaces(orderId, cookieValue);
    }

    @PostMapping(value = "/places", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChoicePlaceDtoResponse choicePlace(@Valid @RequestBody ChoicePlaceDtoRequest choicePlaceDtoRequest, @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException, OptimisticLockException {
        return placeService.choosePlace(choicePlaceDtoRequest, cookieValue);
    }

}
