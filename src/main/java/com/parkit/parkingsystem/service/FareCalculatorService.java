package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.LocalDateTime;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {

        if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        LocalDateTime inHour = ticket.getInTime();
        LocalDateTime outHour = ticket.getOutTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct

        Duration duration = Duration.between(inHour, outHour);
        long durationSec = duration.getSeconds();
        double durationHour = (double) durationSec / 3600;

        TicketDAO ticketDAO = new TicketDAO();

        switch (ticket.getParkingSpot().getParkingType()) {

            case CAR: {
                if (durationHour < 0.5) {
                    ticket.setPrice(0);
                } else if(ticketDAO.getTicketRegularUser(ticket.getVehicleRegNumber())>2){
                    ticket.setPrice((durationHour*Fare.CAR_RATE_PER_HOUR)*Fare.DISCOUNT_FIVE_PERCENT);

                }

                else {
                    ticket.setPrice(durationHour * Fare.CAR_RATE_PER_HOUR);
                }
                break;

            }
            case BIKE: {
                if (durationHour < 0.5) {
                    ticket.setPrice(0);
                } else if(ticketDAO.getTicketRegularUser(ticket.getVehicleRegNumber())>2){
                    ticket.setPrice((durationHour*Fare.BIKE_RATE_PER_HOUR)*Fare.DISCOUNT_FIVE_PERCENT);

                }
                else {
                    ticket.setPrice(durationHour * Fare.BIKE_RATE_PER_HOUR);
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }


}
