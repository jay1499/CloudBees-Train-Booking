package com.train.tickets.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateTicketRequest {

    String emailAddress;
    int seatNumber;
}
