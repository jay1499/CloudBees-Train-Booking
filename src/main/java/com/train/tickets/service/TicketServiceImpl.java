package com.train.tickets.service;

import com.train.tickets.domain.Ticket;
import com.train.tickets.domain.User;
import com.train.tickets.dto.Section;
import com.train.tickets.dto.request.BookTicketRequest;
import com.train.tickets.dto.request.UpdateTicketRequest;
import com.train.tickets.dto.response.BookTicketResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService {

    Map<Integer, Pair<Section, Boolean>> seatStatus;
    Map<String, Ticket> userTicketMap; //user emailAddress as the key

    TicketServiceImpl() {
        seatStatus = new HashMap<>();
        for (int seat = 1; seat <= 200; seat++) {
            Section section = seat <= 100 ? Section.A : Section.B;
            seatStatus.put(seat, Pair.of(section, false));
        }
        userTicketMap = new HashMap<>();
    }

    public BookTicketResponse bookTicketForUser(BookTicketRequest bookTicketRequest) throws Exception {
        validateSeatStatus(bookTicketRequest.getSeatNumber());

        if(userTicketMap.containsKey(bookTicketRequest.getEmailAddress())) {
            throw new Exception("User has already booked ticket, seat number: " + userTicketMap.get(bookTicketRequest.getEmailAddress()).getSeatNumber());
        }
        Ticket ticket = setTicketDetails(bookTicketRequest);

        BookTicketResponse bookTicketResponse = new BookTicketResponse();
        bookTicketResponse.setTicketDetails(ticket);
        return bookTicketResponse;
    }

    public Ticket getTicketDetails(String emailAddress) throws Exception {
        validateUser(emailAddress);
        return userTicketMap.get(emailAddress);
    }

    public Ticket getTicketDetailsById(int ticketId) throws Exception {
        if(seatStatus.containsKey(ticketId) && seatStatus.get(ticketId).getRight().equals(Boolean.FALSE)) {
            throw new Exception("Ticket details not found for ticket id: " + ticketId);
        }
        return userTicketMap.values()
                .stream()
                .filter(ticket -> ticket.getSeatNumber().equals(ticketId)).findFirst().orElse(null);
    }

    private void validateUser(String emailAddress) throws Exception {
        if(!userTicketMap.containsKey(emailAddress)) {
            throw new Exception("Ticket details not found for user: " + emailAddress);
        }
    }

    private void validateSeatStatus(int seatNumber) throws Exception {
        if(seatNumber < 1 || seatNumber > 200) {
            throw new Exception("Seat number: " + seatNumber + " invalid");
        }
        if(seatStatus.get(seatNumber).getRight().equals(Boolean.TRUE)) {
            throw new Exception("Seat number: " + seatNumber + " unavailable");
        }
    }

    private Ticket setTicketDetails(BookTicketRequest bookTicketRequest) {
        int seatNumber = bookTicketRequest.getSeatNumber();

        User user = new User();
        user.setEmailAddress(bookTicketRequest.getEmailAddress());
        user.setFirstName(bookTicketRequest.getFirstName());
        user.setLastName(bookTicketRequest.getLastName());

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setAmount(bookTicketRequest.getAmount());
        ticket.setSource(bookTicketRequest.getSource());
        ticket.setDestination(bookTicketRequest.getDestination());
        ticket.setSeatNumber(seatNumber);
        ticket.setSection(seatStatus.get(seatNumber).getLeft());

        //mark seat number as booked
        seatStatus.put(seatNumber, Pair.of(seatStatus.get(seatNumber).getLeft(), Boolean.TRUE));
        userTicketMap.put(bookTicketRequest.getEmailAddress(), ticket);

        return ticket;
    }

    public List<Ticket> getSectionDetails(Section section) {
        return userTicketMap.values().stream().filter(ticket -> ticket.getSection().equals(section)).toList();
    }

    public void deleteUserTicket(String emailAddress) throws Exception {
        validateUser(emailAddress);
        Ticket ticket = userTicketMap.get(emailAddress);

        //mark seat as unbooked
        seatStatus.put(ticket.getSeatNumber(), Pair.of(seatStatus.get(ticket.getSeatNumber()).getLeft(), Boolean.FALSE));
        userTicketMap.remove(emailAddress);
    }

    public Ticket updateUserTicket(UpdateTicketRequest updateTicketRequest) throws Exception {
        String emailAddress = updateTicketRequest.getEmailAddress();
        int seatNumber = updateTicketRequest.getSeatNumber();
        validateUser(emailAddress);
        validateSeatStatus(seatNumber);

        Ticket ticket = userTicketMap.get(emailAddress);

        //mark old seat as unbooked
        seatStatus.put(ticket.getSeatNumber(), Pair.of(seatStatus.get(ticket.getSeatNumber()).getLeft(), Boolean.FALSE));

        //update ticket with near details
        ticket.setSeatNumber(seatNumber);
        ticket.setSection(seatStatus.get(seatNumber).getLeft());
        userTicketMap.put(emailAddress, ticket);

        //mark new seat as booked
        seatStatus.put(seatNumber, Pair.of(seatStatus.get(seatNumber).getLeft(), Boolean.TRUE));
        return ticket;
    }
}
