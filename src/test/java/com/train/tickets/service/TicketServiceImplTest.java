package com.train.tickets.service;

import com.train.tickets.domain.Ticket;
import com.train.tickets.domain.User;
import com.train.tickets.dto.Section;
import com.train.tickets.dto.request.BookTicketRequest;
import com.train.tickets.dto.request.UpdateTicketRequest;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TicketServiceImplTest {

    @Mock
    private Map<Integer, Pair<Section, Boolean>> seatStatusMock;

    @Mock
    private Map<String, Ticket> userTicketMapMock;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("When booking new ticket is successful")
    void bookTicketForUser_validSeatAvailable() throws Exception {
        BookTicketRequest request = new BookTicketRequest("London", "Paris", 500.23f, 12, "John", "john@example.com");
        when(seatStatusMock.containsKey(12)).thenReturn(true);
        when(seatStatusMock.get(12)).thenReturn(Pair.of(Section.A, false));

        Ticket ticket = new Ticket();
        ticket.setSeatNumber(12);
        when(userTicketMapMock.containsKey("john@example.com")).thenReturn(false);

        Ticket bookedTicket = ticketService.bookTicketForUser(request).getTicketDetails();

        assertNotNull(bookedTicket);
        assertEquals(request.getEmailAddress(), bookedTicket.getUser().getEmailAddress());
        assertEquals(request.getFirstName(), bookedTicket.getUser().getFirstName());
        assertEquals(request.getLastName(), bookedTicket.getUser().getLastName());
        assertEquals(request.getSource(), bookedTicket.getSource());
        assertEquals(Section.A, bookedTicket.getSection());
        assertEquals(12, bookedTicket.getSeatNumber());

        verify(userTicketMapMock).containsKey("john@example.com");
        verify(userTicketMapMock).put("john@example.com", bookedTicket);
        verify(seatStatusMock).put(12, Pair.of(Section.A, true));
    }

    @Test
    @DisplayName("When fetching user ticket details")
    void getTicketDetails_existingUserTicket() throws Exception {
        String emailAddress = "john@example.com";
        Ticket expectedTicket = new Ticket();
        when(userTicketMapMock.containsKey(emailAddress)).thenReturn(true);
        when(userTicketMapMock.get(emailAddress)).thenReturn(expectedTicket);

        Ticket actualTicket = ticketService.getTicketDetails(emailAddress);

        assertEquals(expectedTicket, actualTicket);
        verify(userTicketMapMock).containsKey(emailAddress);
        verify(userTicketMapMock).get(emailAddress);
    }

    @Test
    @DisplayName("When fetching user ticket details that do not exist")
    void getTicketDetails_nonExistingUserTicket() {
        String emailAddress = "nonexistent@example.com";
        when(userTicketMapMock.containsKey(emailAddress)).thenReturn(false);

        assertThrows(Exception.class, () -> ticketService.getTicketDetails(emailAddress));
        verify(userTicketMapMock).containsKey(emailAddress);
    }

    @Test
    @DisplayName("When fetching ticket details of all users for a particular section")
    void getSectionDetails() {
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        Ticket ticket3 = new Ticket();
        ticket1.setSection(Section.A);
        ticket2.setSection(Section.A);
        ticket3.setSection(Section.B);

        when(userTicketMapMock.values()).thenReturn(List.of(ticket1, ticket2));

        assertEquals(2, ticketService.getSectionDetails(Section.A).size());
        verify(userTicketMapMock).values();
    }


    @Test
    @DisplayName("When updating ticket details")
    void updateUserTicket() throws Exception {
        UpdateTicketRequest updateTicketRequest = new UpdateTicketRequest("john@example.com", 2);
        User user = new User();
        user.setEmailAddress("john@example.com");

        Ticket ticket = new Ticket();
        ticket.setSeatNumber(1);
        ticket.setSection(Section.A);
        ticket.setUser(user);

        when(userTicketMapMock.containsKey("john@example.com")).thenReturn(true);
        when(userTicketMapMock.get("john@example.com")).thenReturn(ticket);
        when(seatStatusMock.get(1)).thenReturn(Pair.of(Section.A, true));
        when(seatStatusMock.get(2)).thenReturn(Pair.of(Section.B, false));

        Ticket updatedTicket = ticketService.updateUserTicket(updateTicketRequest);

        verify(userTicketMapMock).get("john@example.com");
        verify(seatStatusMock).put(1, Pair.of(Section.A, false));
        verify(seatStatusMock).put(2, Pair.of(Section.B, true));

        assertEquals(2, updatedTicket.getSeatNumber());
        assertEquals(Section.B, updatedTicket.getSection());
    }

}
