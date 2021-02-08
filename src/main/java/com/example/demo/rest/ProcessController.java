package com.example.demo.rest;


import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/rest")
public class ProcessController {

    @Autowired
    AirpotService airpotService;
    @Autowired
    AirlineCompanyService airlineCompanyService;
    @Autowired
    FlightService flightService;
    @Autowired
    TicketService ticketService;
    @Autowired
    RouteService routeService;

    @RequestMapping(
            value = "/create-airport",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<Airport> createAirport(@RequestBody Airport airport) {
        return airpotService.create(airport);
    }

    @RequestMapping(
            value = "/search-airport",
            method = RequestMethod.POST)
    public Response<Airport> searchAirport(@RequestParam("code") String code) {
        return airpotService.findByCode(code);
    }


    @RequestMapping(
            value = "/create-airline-company",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<AirlineCompany> createAirlineCompany(@RequestBody AirlineCompany airlineCompany) {
        return airlineCompanyService.create(airlineCompany);
    }

    @RequestMapping(
            value = "/search-airline-company",
            method = RequestMethod.POST)
    public Response<AirlineCompany> searchAirlineCompany(@RequestParam("code") String code) {
        return airlineCompanyService.findByCode(code);
    }

    @RequestMapping(
            value = "/create-flight",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<Flight> createFlight(@RequestBody Flight flight) {
        return flightService.create(flight);
    }

    @RequestMapping(
            value = "/search-flight",
            method = RequestMethod.POST)
    public Response<Flight> searchFlight(@RequestParam("code") String code) {
        return flightService.findByCode(code);
    }

    @RequestMapping(
            value = "/update-quota",
            method = RequestMethod.POST)
    public Response<Flight> searchFlight(@RequestParam("code") String flightCode,@RequestParam("quota") int quota) {
        return flightService.updateQouta(flightCode,quota);
    }


    @RequestMapping(
            value = "/create-route",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<Route> createRoute(@RequestBody Route route) {
        return routeService.create(route);
    }

    @RequestMapping(
            value = "/search-route",
            method = RequestMethod.POST)
    public Response<Route> searchRoute(@RequestParam("code") String code) {
        return routeService.findByCode(code);
    }


    @RequestMapping(
            value = "/buy-ticket",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<Ticket> buyTicket(@RequestBody Ticket ticket) {
        return ticketService.buyTicket(ticket);
    }

    @RequestMapping(
            value = "/search-ticket",
            method = RequestMethod.POST)
    public Response<Ticket> searchTicket(@RequestParam("ticketNumber") String ticketNumber) {
        return ticketService.searchTicket(ticketNumber);
    }

    @RequestMapping(
            value = "/cancel-ticket",
            method = RequestMethod.POST)
    public Response<Ticket> cancelTicket(@RequestParam("ticketNumber") String ticketNumber) {
        return ticketService.cancelTicket(ticketNumber);
    }

}
