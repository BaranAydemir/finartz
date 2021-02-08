package com.example.demo.service;

import com.example.demo.entity.Card;
import com.example.demo.entity.Flight;
import com.example.demo.entity.Ticket;
import com.example.demo.model.Response;
import com.example.demo.repository.GeneralOperations;
import com.example.demo.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TicketService {

    @Autowired FlightService flightService;

    private static final String mask = "######******####";

    public Response<Ticket> buyTicket(Ticket ticket){
        Response<Ticket> response = new Response<>();
        try{
            String maskNumber = maskCardNumber(ticket.getCard().getCardNumber(), mask);
            Response<Flight> flightResponse = flightService.findByCode(ticket.getFlight().getCode());
            if(Objects.nonNull(flightResponse) && Objects.nonNull(flightResponse.getData())){
                if(Objects.nonNull(flightResponse.getData().getTicketCount()) && flightResponse.getData().getTicketCount() >0){
                    if(Objects.nonNull(maskNumber)){
                        Session session = SessionUtil.getSessionFactory().openSession();
                        session.beginTransaction();

                        Card card = new Card();
                        card.setCvcCode(ticket.getCard().getCvcCode());
                        card.setNameAndSurnameOntheCard(ticket.getCard().getNameAndSurnameOntheCard());
                        card.setValidDate(ticket.getCard().getValidDate());
                        card.setCardNumber(maskNumber);
                        session.saveOrUpdate(card);

                        ticket.setCard(card);
                        ticket.setTicketNumber();
                        ticket.setTicketStatus(true);

                        Flight flight = flightResponse.getData();
                        flight.setTicketCount(flight.getTicketCount()-1);
                        session.saveOrUpdate(flight);

                        ticket.setFlight(flight);
                        session.saveOrUpdate(ticket);
                        session.getTransaction().commit();
                        response.setCode("100");
                        response.setMessage("success");
                        response.setData(ticket);
                    }
                    else {
                        response.setCode(Response.BUSINESS_ERROR);
                        response.setMessage("Ödeme işlemleri sırasında bir sorun oluştu.");
                    }
                }
                else{
                    response.setCode(Response.BUSINESS_ERROR);
                    response.setMessage("Bilet işlemilerinde bir sorun oluştu.");
                }

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

    private String maskCardNumber(String cardNumber, String mask) {
        int index = 0;

        StringBuilder maskedNumber = new StringBuilder();
        if(cardNumber.length() >=16){
            String cardNumberCheckAndFix = cardNumberCheckAndFix(cardNumber);
            for (int i = 0; i < mask.length(); i++) {
                char c = mask.charAt(i);
                if (c == '#') {
                    maskedNumber.append(cardNumberCheckAndFix.charAt(index));
                    index++;
                } else if (c == '*') {
                    maskedNumber.append(c);
                    index++;
                } else {
                    maskedNumber.append(c);
                }
            }
        }
        else {
            maskedNumber = null;
        }

        return Objects.nonNull(maskedNumber)? maskedNumber.toString():null;
    }

    private String cardNumberCheckAndFix(String cardNumber){
        int index = 0;
        StringBuilder regulatedCardNumber = new StringBuilder();

        for(int i = 0; i < cardNumber.length(); i++){
            char c = cardNumber.charAt(i);
            try{
                Integer.parseInt(String.valueOf(c));
                regulatedCardNumber.append(cardNumber.charAt(index));
                index++;

            }catch(Exception ex){
                index++;
                continue;
            }
        }

        return regulatedCardNumber.toString();
    }

    public Response<Ticket> searchTicket(String ticketNumber) {
        Response<Ticket> response = new Response();
        try{
            Session session = SessionUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Ticket ticket = (Ticket) session.createCriteria(Ticket.class)
                    .add(Property.forName("ticketNumber").eq(ticketNumber))
                    .add( Property.forName("ticketStatus").eq(true))
                    .uniqueResult();
            if(Objects.nonNull(ticket)){
                session.getTransaction().commit();
                response.setCode("100");
                response.setMessage("success");
                response.setData(ticket);
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


    public Response<Ticket> cancelTicket(String ticketNumber) {
        Response<Ticket> response = new Response();
        try{
            Session session = SessionUtil.getSessionFactory().openSession();
            session.beginTransaction();
            response = searchTicket(ticketNumber);
            if(Objects.nonNull(response) && Objects.nonNull(response.getData())){
                Ticket ticket = response.getData();
                if(Objects.nonNull(ticket.getFlight())){
                    Response<Flight> flightResponse = flightService.findByCode(ticket.getFlight().getCode());
                    if(Objects.nonNull(flightResponse) && Objects.nonNull(flightResponse.getData())){
                        Flight flight = flightResponse.getData();
                        flight.setTicketCount(flight.getTicketCount()+1);
                        session.saveOrUpdate(flight);
                        ticket.setTicketStatus(false);
                        ticket.setFlight(flight);
                        session.saveOrUpdate(ticket);
                        session.getTransaction().commit();
                        response.setCode("100");
                        response.setMessage("success");
                        response.setData(ticket);
                    }
                    else {
                        response.setCode(Response.BUSINESS_ERROR);
                        response.setMessage("Uçuşa ait bilgiler bulunamadı.");
                    }
                }
                else {
                    response.setCode(Response.BUSINESS_ERROR);
                    response.setMessage("Belirtilen bilete ait bir uçuş bulunamadı.");
                }


            }
            else {
                response.setCode(Response.BUSINESS_ERROR);
                response.setMessage("Belirtilen bilet bulunamadı.");
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
