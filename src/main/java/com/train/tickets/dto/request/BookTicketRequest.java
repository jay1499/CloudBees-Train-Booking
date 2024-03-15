package com.train.tickets.dto.request;

import com.train.tickets.dto.Section;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NonNull;

@Data
public class BookTicketRequest {

    @NonNull
    String source;

    @NonNull
    String destination;

    @NonNull
    Float amount;

    @NonNull
    Integer seatNumber;

    @NonNull
    String firstName;

    String lastName;

    @Email
    @NonNull
    String emailAddress;

}
