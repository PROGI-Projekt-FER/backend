package com.ticketswap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketswap.dto.event.EventDto;
import com.ticketswap.dto.event_entity.EventEntityDto;
import com.ticketswap.dto.location.LocationDto;
import com.ticketswap.dto.ticket.TicketDetailsDto;
import com.ticketswap.dto.ticket.TicketInsertDto;
import com.ticketswap.dto.venue.VenueDto;
import com.ticketswap.model.*;
import com.ticketswap.repository.CategoryRepository;
import com.ticketswap.repository.TicketRepository;
import com.ticketswap.repository.UserRepository;
import com.ticketswap.service.AuthService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class TicketSwapTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void testCreateTicket() throws Exception {
        User user = new User();
        user = userRepository.save(user);
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));


        TicketInsertDto requestBody = new TicketInsertDto();
        EventDto eventDto = new EventDto();
        EventEntityDto eventEntityDto = new EventEntityDto();
        VenueDto venueDto = new VenueDto();
        LocationDto locationDto = new LocationDto();
        locationDto.setCity("Zagreb");
        locationDto.setAddress("Ul. Vice Vukova 6");
        locationDto.setCountry("Croatia");
        venueDto.setName("Arena Zagreb");
        venueDto.setCapacity(18000);
        venueDto.setLocation(locationDto);
        eventEntityDto.setName("Name Surname");
        eventEntityDto.setType("Music artist");
        eventDto.setEventDate(LocalDateTime.now().plusDays(2));
        eventDto.setEventEntity(eventEntityDto);
        eventDto.setVenue(venueDto);
        requestBody.setEvent(eventDto);
        requestBody.setStatus(TicketStatus.SWAP);
        requestBody.setDescription("Sjedalo 123a");
        requestBody.setCategoryIds(List.of());
        requestBody.setInterestedInCategoryIds(List.of());

        String response = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TicketDetailsDto createdTicket = objectMapper.readValue(response, TicketDetailsDto.class);

        Long ticketId = createdTicket.getId();

        List<Ticket> tickets = ticketRepository.findAll();
        assertTrue(tickets.stream().anyMatch(ticket -> ticket.getId().equals(ticketId)));

    }

    @Test
    void testCreateTicketWithLongDescriptionButUnder256Characters() throws Exception {
        User user = new User();
        user = userRepository.save(user);
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));


        TicketInsertDto requestBody = new TicketInsertDto();
        EventDto eventDto = new EventDto();
        EventEntityDto eventEntityDto = new EventEntityDto();
        VenueDto venueDto = new VenueDto();
        LocationDto locationDto = new LocationDto();
        locationDto.setCity("Zagreb");
        locationDto.setAddress("Ul. Vice Vukova 6");
        locationDto.setCountry("Croatia");
        venueDto.setName("Arena Zagreb");
        venueDto.setCapacity(18000);
        venueDto.setLocation(locationDto);
        eventEntityDto.setName("Name Surname");
        eventEntityDto.setType("Music artist");
        eventDto.setEventDate(LocalDateTime.now().plusDays(2));
        eventDto.setEventEntity(eventEntityDto);
        eventDto.setVenue(venueDto);
        requestBody.setEvent(eventDto);
        requestBody.setStatus(TicketStatus.SWAP);
        requestBody.setDescription("A".repeat(255));
        requestBody.setCategoryIds(List.of());
        requestBody.setInterestedInCategoryIds(List.of());

        String response = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TicketDetailsDto createdTicket = objectMapper.readValue(response, TicketDetailsDto.class);

        Long ticketId = createdTicket.getId();

        List<Ticket> tickets = ticketRepository.findAll();
        assertTrue(tickets.stream().anyMatch(ticket -> ticket.getId().equals(ticketId)));

    }

    @Test
    void testCreateTicketWithEventInPastThrowsIllegalArgumentException() throws Exception {
        User user = new User();
        user = userRepository.save(user);
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));


        TicketInsertDto requestBody = new TicketInsertDto();
        EventDto eventDto = new EventDto();
        EventEntityDto eventEntityDto = new EventEntityDto();
        VenueDto venueDto = new VenueDto();
        LocationDto locationDto = new LocationDto();
        locationDto.setCity("Zagreb");
        locationDto.setAddress("Ul. Vice Vukova 6");
        locationDto.setCountry("Croatia");
        venueDto.setName("Arena Zagreb");
        venueDto.setCapacity(18000);
        venueDto.setLocation(locationDto);
        eventEntityDto.setName("Name Surname");
        eventEntityDto.setType("Music artist");
        eventDto.setEventDate(LocalDateTime.now().minusDays(2));
        eventDto.setEventEntity(eventEntityDto);
        eventDto.setVenue(venueDto);
        requestBody.setEvent(eventDto);
        requestBody.setStatus(TicketStatus.SWAP);
        requestBody.setDescription("Sjedalo 123a");
        requestBody.setCategoryIds(List.of());
        requestBody.setInterestedInCategoryIds(List.of());

        String response = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().is(400))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(response.contains("Event date can't be in the past"));

    }

    @Test
    void testNonExistingEndpoint() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/tickets-for-sale"))
                .andExpect(status().is(400))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertTrue(responseContent.contains("No static resource api/tickets-for-sale"));
    }

    @Test
    void notLoggedInCantCreateTicket() throws Exception {
        TicketInsertDto requestBody = new TicketInsertDto();
        EventDto eventDto = new EventDto();
        EventEntityDto eventEntityDto = new EventEntityDto();
        VenueDto venueDto = new VenueDto();
        LocationDto locationDto = new LocationDto();
        locationDto.setCity("Zagreb");
        locationDto.setAddress("Ul. Vice Vukova 6");
        locationDto.setCountry("Croatia");
        venueDto.setName("Arena Zagreb");
        venueDto.setCapacity(18000);
        venueDto.setLocation(locationDto);
        eventEntityDto.setName("Name Surname");
        eventEntityDto.setType("Music artist");
        eventDto.setEventDate(LocalDateTime.now().minusDays(2));
        eventDto.setEventEntity(eventEntityDto);
        eventDto.setVenue(venueDto);
        requestBody.setEvent(eventDto);
        requestBody.setStatus(TicketStatus.SWAP);
        requestBody.setDescription("Sjedalo 123a");
        requestBody.setCategoryIds(List.of());
        requestBody.setInterestedInCategoryIds(List.of());

        String response = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().is(401))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(response.contains("You are unauthorized"));
    }

    @Test
    void regularUserCantMakeOtherUsersAdmin() throws Exception {
        User user = new User();
        user = userRepository.save(user);
        when(authService.getLoggedInUser()).thenReturn(Optional.of(user));

        User otherUser = new User();
        otherUser = userRepository.save(otherUser);


        String response = mockMvc.perform(post("/api/config/make-user-admin/" + otherUser.getId()))
                .andExpect(status().is(401))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(response.contains("You are unauthorized"));
        assertEquals(UserRole.REGULAR ,userRepository.findById(otherUser.getId()).get().getUserRole());

    }

}
