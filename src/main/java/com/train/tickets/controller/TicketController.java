package com.train.tickets.controller;

import com.train.tickets.domain.Ticket;
import com.train.tickets.dto.Section;
import com.train.tickets.dto.request.BookTicketRequest;
import com.train.tickets.dto.request.UpdateTicketRequest;
import com.train.tickets.dto.response.BookTicketResponse;
import com.train.tickets.service.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {

    @Autowired
    TicketServiceImpl ticketServiceImpl;

    @PostMapping("/book")
    public BookTicketResponse bookTicket(@RequestBody @Validated BookTicketRequest bookTicketRequest) throws Exception {
        return ticketServiceImpl.bookTicketForUser(bookTicketRequest);
    }

    @GetMapping("/details")
    public Ticket getTicket(@RequestParam(value = "emailAddress") String emailAddress) throws Exception {
        return ticketServiceImpl.getTicketDetails(emailAddress);
    }

    @GetMapping("/details/{ticketId}")
    public Ticket getTicketById(@PathVariable(value = "ticketId") int ticketId) throws Exception {
        return ticketServiceImpl.getTicketDetailsById(ticketId);
    }

    @GetMapping("/section/{sectionName}")
    public List<Ticket> getSection(@PathVariable(value = "sectionName") Section section) {
        return ticketServiceImpl.getSectionDetails(section);
    }

    @DeleteMapping("/delete-user/{emailAddress}")
    public void deleteUser(@PathVariable(value = "emailAddress") String emailAddress) throws Exception {
        ticketServiceImpl.deleteUserTicket(emailAddress);
    }

    @PatchMapping("/modify-seat")
    public Ticket modifySeat(@RequestBody @Validated UpdateTicketRequest updateTicketRequest) throws Exception {
        return ticketServiceImpl.updateUserTicket(updateTicketRequest);
    }
}
