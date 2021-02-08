package com.example.demo.service;

import com.example.demo.entity.AirlineCompany;
import com.example.demo.entity.Airport;
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
public class RouteService implements GeneralOperations<Route> {

    @Autowired AirlineCompanyService airlineCompanyService;
    @Autowired AirpotService airpotService;
    @Override
    public Response<Route> create(Route route) {
        Response<Route> response = new Response();
        try{
            Session session = SessionUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Response<Route> routeResponse = findByCode(route.getCode());
            if(Objects.nonNull(routeResponse) && Objects.isNull(routeResponse.getData())){
                route.setCode();
                Response<AirlineCompany> airlineCompanyResponse = airlineCompanyService.findByCode(route.getAirlineCompany().getCode());
                route.setAirlineCompany(airlineCompanyResponse.getData());
                if(Objects.nonNull(airlineCompanyResponse) && Objects.nonNull(airlineCompanyResponse)){
                    Response<Airport> placeOfDepartureAirport = airpotService.findByCode(route.getPlaceOfDeparture().getCode());
                    route.setPlaceOfDeparture(placeOfDepartureAirport.getData());
                    Response<Airport> destinationAirport = airpotService.findByCode(route.getDestination().getCode());
                    route.setDestination(destinationAirport.getData());
                    if((Objects.nonNull(placeOfDepartureAirport) && Objects.nonNull(placeOfDepartureAirport.getData())
                    && (Objects.nonNull(destinationAirport) && Objects.nonNull(destinationAirport.getData())))){
                        session.saveOrUpdate(route);
                        session.getTransaction().commit();
                        response.setCode("100");
                        response.setMessage("success");
                        response.setData(route);
                    }
                    else {
                        response.setCode(Response.BUSINESS_ERROR);
                        response.setMessage("Rota eklemek için en az iki tane havaalanı belirtmelisiniz.");
                    }

                }
                else {
                    response.setCode(Response.BUSINESS_ERROR);
                    response.setMessage("Havayolu şirketine ait kayıt bulunamamıştır.");
                }

            }
            else {
                response.setCode(Response.BUSINESS_ERROR);
                response.setMessage("Kaydetmek istediğiniz havaalanı zaten var.");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            response.setCode(Response.TECHNICAL_ERROR_101);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response<Route> findByCode(String code) {
        Response<Route> response = new Response();
        try{
            Session session = SessionUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Route route = (Route) session.createCriteria(Route.class)
                    .add(Property.forName("code").eq(code))
                    .uniqueResult();
            if(Objects.nonNull(route)){
                session.getTransaction().commit();
                response.setCode("100");
                response.setMessage("success");
                response.setData(route);
            }
            else {
                response.setCode(Response.BUSINESS_ERROR);
                response.setMessage("Belirtilen rota bulunamadı.");
            }

        }
        catch (Exception e){
            e.printStackTrace();
            response.setCode(Response.TECHNICAL_ERROR_101);
            response.setMessage(e.getMessage());
        }
        return response;
    }

}
