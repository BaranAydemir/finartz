package com.example.demo.service;

import com.example.demo.entity.AirlineCompany;
import com.example.demo.entity.Airport;
import com.example.demo.model.Response;
import com.example.demo.repository.GeneralOperations;
import com.example.demo.util.SessionUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AirlineCompanyService implements GeneralOperations<AirlineCompany> {

    @Override
    public Response<AirlineCompany> create(AirlineCompany airlineCompany) {
        Response response = new Response();
        try{
            Session session = SessionUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Response<AirlineCompany> airlineCompanyResponse = findByCode(airlineCompany.getCode());
            if(Objects.nonNull(airlineCompany) && Objects.isNull(airlineCompanyResponse.getData())){
                session.saveOrUpdate(airlineCompany);
                session.getTransaction().commit();
                response.setCode("100");
                response.setMessage("success");
                response.setData(airlineCompany);
            }
            else {
                response.setCode(Response.BUSINESS_ERROR);
                response.setMessage("Kaydetmek istediğiniz zaten var.");
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
    public Response<AirlineCompany> findByCode(String code) {
        Response<AirlineCompany> response = new Response();
        try{
            Session session = SessionUtil.getSessionFactory().openSession();
            session.beginTransaction();
            AirlineCompany airlineCompany = (AirlineCompany)session.createCriteria(AirlineCompany.class)
                    .add(Property.forName("code").eq(code))
                    .uniqueResult();
            if(Objects.nonNull(airlineCompany)){
                session.getTransaction().commit();
                response.setCode("100");
                response.setMessage("success");
                response.setData(airlineCompany);
            }
            else {
                response.setCode(Response.BUSINESS_ERROR);
                response.setMessage("Şirket bulunamadı.");
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
