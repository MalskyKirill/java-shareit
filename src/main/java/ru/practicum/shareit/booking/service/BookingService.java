package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

public interface BookingService {
    BookingDto createBooking(BookingDtoRequest bookingDtoRequest, Long userId);


    BookingDto updateBookingApproved(Long userId, Long bookingId, Boolean approved);

    BookingDto getBooking(Long userId, Long bookingId);
}
