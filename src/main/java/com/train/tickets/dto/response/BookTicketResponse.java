package com.train.tickets.dto.response;

import com.train.tickets.domain.Ticket;
import lombok.Data;

@Data
public class BookTicketResponse {
    Ticket ticketDetails;
}
