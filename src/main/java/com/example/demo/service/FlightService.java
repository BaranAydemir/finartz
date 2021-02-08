package com.example.demo.service;

import com.example.demo.entity.Flight;
import com.example.demo.entity.Route;
import com.example.demo.model.Response;
import com.example.demo.repository.GeneralOperations;
import com.example.demo.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class FlightService implements GeneralOperations<Flight> {
    @Autowired
    RouteService routeService;

    @Override
    public Response<Flight> create(Flight flight) {
        Response<Flight> response = new Response();
        try{
            Session session = SessionUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Response<Route> routeResponse = routeService.findByCode(flight.getRoute().getCode());
            if(Objects.nonNull(routeResponse) && Objects.nonNull(routeResponse.getData())){
                flight.setCode();
                flight.setTicketCount();
                flight.setRoute(routeResponse.getData());
                session.saveOrUpdate(flight);
                session.getTransaction().commit();
                response.setCode("100");
                response.setMessage("success");
                response.setData(flight);
            }
            else{
                response.setCode(Response.TECHNICAL_ERROR_101);
                response.setMessage("Belirtilen rota bulunamadı.");
            }

        }
        catch (Exception e){
            e.printStackTrace();
            response.setCode(Response.TECHNICAL_ERROR_101);
            response.setMessage(e.getMessage());
        }
        finally {

        }
        return response;
    }

    @Override
    public Response<Flight> findByCode(String code) {
        Response<Flight> response = new Response();
        try{
            Session session = SessionUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Flight flight = (Flight)session.createCriteria(Flight.class)
                    .add(Property.forName("code").eq(code))
                    .uniqueResult();
            if(Objects.nonNull(flight)){
                session.getTransaction().commit();
                response.setCode("100");
                response.setMessage("success");
                response.setData(flight);
            }
            else {
                response.setCode(Response.BUSINESS_ERROR);
                response.setMessage("Belirtilen uçuş bulunamadı.");
            }

        }
        catch (Exception e){
            e.printStackTrace();
            response.setCode(Response.TECHNICAL_ERROR_101);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public Response<Flight> updateQouta(String flightCode,int quota) {
        Response<Flight> response = new Response();
        try{
            Session session = SessionUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Flight flight = (Flight)session.createCriteria(Flight.class)
                    .add(Property.forName("code").eq(flightCode))
                    .uniqueResult();
            if(Objects.nonNull(flight)){
                flight = calculateTicketPrice(flight,quota);
                session.saveOrUpdate(flight);
                session.getTransaction().commit();
                response.setCode("100");
                response.setMessage("success");
                response.setData(flight);
            }
            else {
                response.setCode(Response.BUSINESS_ERROR);
                response.setMessage("Belirtilen uçuş bulunamadı.");
            }

        }
        catch (Exception e){
            e.printStackTrace();
            response.setCode(Response.TECHNICAL_ERROR_101);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    private Flight calculateTicketPrice(Flight flight,int quota){
        int calculatedQuota = flight.getQuota()+ flight.getQuota()*10/100;
        if(calculatedQuota <= quota){
            double calculatedTicketPrice = flight.getTicketPrice() + flight.getTicketPrice()*10/100;
            flight.setTicketPrice(calculatedTicketPrice);
            int newTicketCount = flight.getTicketCount() + (quota - flight.getQuota());
            flight.setTicketCount(newTicketCount);
            flight.setQuota(quota);
        }
        return flight;
    }

}
