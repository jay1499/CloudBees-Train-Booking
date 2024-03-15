package com.train.tickets.service;

import com.train.tickets.domain.Ticket;
import com.train.tickets.dto.Section;
import com.train.tickets.dto.request.BookTicketRequest;
import com.train.tickets.dto.request.UpdateTicketRequest;
import com.train.tickets.dto.response.BookTicketResponse;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

interface TicketService {
    BookTicketResponse bookTicketForUser(BookTicketRequest bookTicketRequest) throws Exception;

    Ticket getTicketDetails(String emailAddress) throws Exception;

    Ticket getTicketDetailsById(int ticketId) throws Exception;

    List<Ticket> getSectionDetails(Section section);

    void deleteUserTicket(String emailAddress) throws Exception;

    Ticket updateUserTicket(UpdateTicketRequest updateTicketRequest) throws Exception;

}
