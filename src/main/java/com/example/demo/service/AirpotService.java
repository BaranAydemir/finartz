package com.example.demo.service;

import com.example.demo.repository.GeneralOperations;
import com.example.demo.util.SessionUtil;
import com.example.demo.entity.Airport;
import com.example.demo.model.Response;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Component
public class AirpotService implements GeneralOperations<Airport> {
    @Override
    public Response<Airport> create(Airport airport) {
        Response<Airport> response = new Response();
        try{
            Session session = SessionUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Response<Airport> airportResponse = findByCode(airport.getCode());
            if(Objects.nonNull(airportResponse) && Objects.isNull(airportResponse.getData())){
                airport.setCreateTime();
                session.saveOrUpdate(airport);
                session.getTransaction().commit();
                response.setCode("100");
                response.setMessage("success");
                response.setData(airport);
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
    public Response<Airport> findByCode(String code) {
        Response<Airport> response = new Response();
        try{
            Session session = SessionUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Airport airport1 = (Airport)session.createCriteria(Airport.class)
                    .add(Property.forName("code").eq(code))
                    .uniqueResult();
            if(Objects.nonNull(airport1)){
                session.getTransaction().commit();
                response.setCode("100");
                response.setMessage("success");
                response.setData(airport1);
            }
            else {
                response.setCode(Response.BUSINESS_ERROR);
                response.setMessage("Havaalanı bulunamadı.");
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
