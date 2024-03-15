package com.train.tickets.domain;

import com.train.tickets.dto.Section;
import lombok.Data;

@Data
public class Ticket {
    User user;
    String source;
    String destination;
    Float amount;
    Section section;
    Integer seatNumber;
}
