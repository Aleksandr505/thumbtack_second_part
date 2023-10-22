package com.gladun.buscompany.controller;

import com.gladun.buscompany.dto.request.TripDtoRequest;
import com.gladun.buscompany.dto.response.TripDtoResponse;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.service.ServiceUtils;
import com.gladun.buscompany.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping(value = "/trips", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TripDtoResponse addTrip(@Valid @RequestBody TripDtoRequest tripDtoRequest, @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        return tripService.addTrip(tripDtoRequest, cookieValue);
    }

    @PutMapping(value = "/trips/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TripDtoResponse editTrip(@PathVariable int id, @Valid @RequestBody TripDtoRequest tripDtoRequest, @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        return tripService.editTrip(id, tripDtoRequest, cookieValue);
    }

    @DeleteMapping(value = "/trips/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteTrip(@PathVariable int id, @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        tripService.deleteTrip(id, cookieValue);
    }

    @GetMapping(value = "/trips/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TripDtoResponse getTrip(@PathVariable int id, @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        return tripService.getTrip(id, cookieValue);
    }

    @PutMapping(value = "/trips/{id}/approve", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TripDtoResponse approveTrip(@PathVariable int id, @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        return tripService.approveTrip(id, cookieValue);
    }

    @GetMapping(value = "/trips", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TripDtoResponse> getTripsByParams(@RequestParam(value = "fromStation", required = false) String fromStation,
                                                  @RequestParam(value = "toStation", required = false) String toStation,
                                                  @RequestParam(value = "busName", required = false) String busName,
                                                  @RequestParam(value = "fromDate", required = false) String fromDate,
                                                  @RequestParam(value = "toDate", required = false) String toDate,
                                                  @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        return tripService.getTripsByParams(fromStation, toStation, busName, fromDate, toDate, cookieValue);
    }

}
