package com.gladun.buscompany.service;

import com.gladun.buscompany.dao.PlaceDao;
import com.gladun.buscompany.dao.SessionDao;
import com.gladun.buscompany.dao.TripDao;
import com.gladun.buscompany.dto.request.TripDtoRequest;
import com.gladun.buscompany.dto.response.TripDtoResponse;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mapstruct.TripMapStruct;
import com.gladun.buscompany.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class TripService {

    private final TripDao tripDao;
    private final SessionDao sessionDao;
    private final PlaceDao placeDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    @Autowired
    public TripService(TripDao tripDao, SessionDao sessionDao, PlaceDao placeDao) {
        this.tripDao = tripDao;
        this.sessionDao = sessionDao;
        this.placeDao = placeDao;
    }

    @Transactional
    public TripDtoResponse addTrip(TripDtoRequest tripDtoRequest, String cookie) throws ServerException {
        ServiceUtils.getAdminUserByCookie(cookie, sessionDao);

        List<TripDate> tripDates = createTripDates(tripDtoRequest);
        Trip trip = TripMapStruct.INSTANCE.fromTripDtoRequestToTrip(tripDtoRequest);
        Bus bus = tripDao.getBusByName(tripDtoRequest.getBusName());
        if (bus == null)
            throw new ServerException(ServerErrorCode.BUS_NOT_FOUND);
        trip.setBus(bus);
        for (TripDate tripDate : tripDates) {
            tripDate.setFreePlaces(bus.getPlaceCount());
        }
        trip.setTripDates(tripDates);

        tripDao.insert(trip);
        insertDefaultPlaces(tripDates, bus);

        TripDtoResponse response = TripMapStruct.INSTANCE.fromTripToTripDtoResponse(trip);
        if (tripDtoRequest.getScheduleDto() != null) {
            response.setScheduleDto(tripDtoRequest.getScheduleDto());
        }
        String[] dates = new String[tripDates.size()];
        for (int i = 0; i < tripDates.size(); i++) {
            dates[i] = tripDates.get(i).getDate().toString();
        }
        response.setDates(dates);

        return response;
    }

    @Transactional
    public TripDtoResponse editTrip(int id, TripDtoRequest tripDtoRequest, String cookie) throws ServerException {
        ServiceUtils.getAdminUserByCookie(cookie, sessionDao);
        Trip tripDb = tripDao.getById(id);
        if (tripDb == null)
            throw new ServerException(ServerErrorCode.TRIP_NOT_FOUND);

        List<TripDate> tripDates = createTripDates(tripDtoRequest);
        Trip trip = TripMapStruct.INSTANCE.fromTripDtoRequestToTrip(tripDtoRequest);
        trip.setId(id);
        Bus bus = tripDao.getBusByName(tripDtoRequest.getBusName());
        if (bus == null)
            throw new ServerException(ServerErrorCode.BUS_NOT_FOUND);
        trip.setBus(bus);
        for (TripDate tripDate : tripDates) {
            tripDate.setFreePlaces(bus.getPlaceCount());
        }
        trip.setTripDates(tripDates);

        tripDao.update(trip);

        if (!tripDb.getBus().getBusName().equals(bus.getBusName())) {
            placeDao.deletePlacesByTripDates(tripDb.getTripDates());
        }
        insertDefaultPlaces(tripDates, bus);

        TripDtoResponse response = TripMapStruct.INSTANCE.fromTripToTripDtoResponse(trip);
        if (tripDtoRequest.getScheduleDto() != null) {
            response.setScheduleDto(tripDtoRequest.getScheduleDto());
        }
        String[] dates = new String[tripDates.size()];
        for (int i = 0; i < tripDates.size(); i++) {
            dates[i] = tripDates.get(i).getDate().toString();
        }
        response.setDates(dates);

        return response;
    }

    private void insertDefaultPlaces(List<TripDate> tripDates, Bus bus) throws ServerException {
        List<Place> places = new ArrayList<>();
        for (int i = 1; i <= bus.getPlaceCount(); i++) {
            for (TripDate tripDate : tripDates) {
                places.add(new Place(i, null, tripDate));
            }
        }
        placeDao.insertPlaces(places);
    }

    @Transactional
    public void deleteTrip(int id, String cookie) throws ServerException {
        ServiceUtils.getAdminUserByCookie(cookie, sessionDao);
        tripDao.deleteById(id);
    }

    @Transactional
    public TripDtoResponse getTrip(int id, String cookie) throws ServerException {
        ServiceUtils.getAdminUserByCookie(cookie, sessionDao);
        Trip trip = tripDao.getById(id);
        if (trip == null)
            throw new ServerException(ServerErrorCode.TRIP_NOT_FOUND);
        return TripMapStruct.INSTANCE.fromTripToTripDtoResponse(trip);
    }

    @Transactional
    public TripDtoResponse approveTrip(int id, String cookie) throws ServerException {
        ServiceUtils.getAdminUserByCookie(cookie, sessionDao);
        Trip trip = tripDao.getById(id);
        if (trip == null)
            throw new ServerException(ServerErrorCode.TRIP_NOT_FOUND);
        trip.setApproved(true);
        tripDao.approveTrip(id);
        return TripMapStruct.INSTANCE.fromTripToTripDtoResponse(trip);
    }

    @Transactional
    public List<TripDtoResponse> getTripsByParams(String fromStation, String toStation, String busName, String fromDate,
                                                  String toDate, String cookie) throws ServerException {
        User user = sessionDao.getUserByCookie(cookie);
        if (user == null)
            throw new ServerException(ServerErrorCode.WRONG_COOKIE);

        LocalDate fromDateObj = null;
        LocalDate toDateObj = null;
        if (fromDate != null && !fromDate.isEmpty())
            fromDateObj = LocalDate.parse(fromDate);
        if (toDate != null && !toDate.isEmpty())
            toDateObj = LocalDate.parse(toDate);
        List<Trip> trips;
        boolean isClient = (user.getUserType() == RoleEnum.CLIENT);
        trips = tripDao.getAllWithParams(fromStation, toStation, busName, fromDateObj, toDateObj, isClient);

        List<TripDtoResponse> responseList = new ArrayList<>();
        for (Trip trip : trips) {
            responseList.add(TripMapStruct.INSTANCE.fromTripToTripDtoResponse(trip));
        }
        return responseList;
    }

    private static List<TripDate> createTripDates(TripDtoRequest request) throws ServerException {
        TripDtoRequest.ScheduleDto schedule = request.getScheduleDto();
        List<TripDate> tripDates = new ArrayList<>();

        if (schedule != null) {
            LocalDate fromDate = LocalDate.parse(schedule.getFromDate());
            LocalDate toDate = LocalDate.parse(schedule.getToDate());
            long offset = toDate.toEpochDay() - fromDate.toEpochDay();
            String[] dayOfWeekArray = schedule.getPeriod().split(",");
            String[] dayOfMonthArray = schedule.getPeriod().split(",");

            for (String dayOfWeek : dayOfWeekArray) {
                String trimDayOfWeek = dayOfWeek.trim();
                if (!trimDayOfWeek.equals("Mon") && !trimDayOfWeek.equals("Tue") && !trimDayOfWeek.equals("Wed")
                        && !trimDayOfWeek.equals("Thu") && !trimDayOfWeek.equals("Fri") && !trimDayOfWeek.equals("Sat")
                        && !trimDayOfWeek.equals("Sun")) {
                    dayOfWeekArray = null;
                    break;
                }
            }
            for (String day : dayOfMonthArray) {
                if (!day.matches("\\d+(\\.\\d+)?")) {
                    dayOfMonthArray = null;
                    break;
                }
            }

            LOGGER.debug("fromDate = {}, toDate = {}", fromDate, toDate);
            LOGGER.debug("Period: {}", schedule.getPeriod());
            LOGGER.debug("dayOfWeekArray: {}", (Object) dayOfWeekArray);
            LOGGER.debug("dayOfMonthArray: {}", (Object) dayOfMonthArray);

            if (schedule.getPeriod().equalsIgnoreCase("daily")) {
                for (int i = 0; i < offset; i++)
                    tripDates.add(new TripDate(fromDate.plusDays(i)));
                return tripDates;
            } else if (schedule.getPeriod().equalsIgnoreCase("odd")) {
                for (int i = 0; i < offset; i++) {
                    int dayOfMonth = fromDate.plusDays(i).getDayOfMonth();
                    if (dayOfMonth % 2 == 1)
                        tripDates.add(new TripDate(fromDate.plusDays(i)));
                }
                return tripDates;
            } else if (schedule.getPeriod().equalsIgnoreCase("even")) {
                for (int i = 0; i < offset; i++) {
                    int dayOfMonth = fromDate.plusDays(i).getDayOfMonth();
                    if (dayOfMonth % 2 == 0)
                        tripDates.add(new TripDate(fromDate.plusDays(i)));
                }
                return tripDates;
            } else if (dayOfWeekArray != null && dayOfWeekArray.length > 0) {
                for (int i = 0; i < offset; i++) {
                    for (String day : dayOfWeekArray) {
                        if (fromDate.plusDays(i).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).equalsIgnoreCase(day)) {
                            LOGGER.debug("Add date {} with dayOfWeekPattern {}", fromDate.plusDays(i), day);
                            tripDates.add(new TripDate(fromDate.plusDays(i)));
                        }
                    }
                }
                return tripDates;
            } else if (dayOfMonthArray != null && dayOfMonthArray.length > 0) {
                int[] days = new int[dayOfMonthArray.length];
                for (int i = 0; i < dayOfMonthArray.length; i++) {
                    days[i] = Integer.parseInt(dayOfMonthArray[i].trim());
                }
                for (int i = 0; i < offset; i++) {
                    for (int day : days) {
                        if (fromDate.plusDays(i).getDayOfMonth() == day) {
                            LOGGER.debug("Add date {} with dayOfMonth {}", fromDate.plusDays(i), day);
                            tripDates.add(new TripDate(fromDate.plusDays(i)));
                        }
                    }
                }
                return tripDates;
            } else {
                throw new ServerException(ServerErrorCode.INCORRECT_SCHEDULE_PATTERN);
            }
        } else if (request.getDates() != null) {
            try {
                String[] dates = request.getDates();
                for (String date : dates) {
                    tripDates.add(new TripDate(LocalDate.parse(date.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
                }
                return tripDates;
            } catch (DateTimeParseException ex) {
                throw new ServerException(ServerErrorCode.ILLEGAL_DATE_FORMAT);
            }
        }
        //LOGGER.debug("TripDates after service: {}", tripDates);

        return tripDates;
    }

}
