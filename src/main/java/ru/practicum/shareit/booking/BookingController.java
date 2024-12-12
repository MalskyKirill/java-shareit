package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;

/**
 * TODO Sprint add-bookings.
 */

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createNewBooking(@Valid @RequestBody BookingDtoRequest bookingDtoRequest, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST-запрос к эндпоинту: '/bookings' на добавление booking");
        return bookingService.createBooking(bookingDtoRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        log.info("PATCH-запрос к эндпоинту: '/bookings/bookingId' на обновление booking");
        return bookingService.updateBookingApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.info("GET-запрос к эндпоинту: '/bookings/bookingId' на получение booking");
        return bookingService.getBooking(userId, bookingId);
    }

}
