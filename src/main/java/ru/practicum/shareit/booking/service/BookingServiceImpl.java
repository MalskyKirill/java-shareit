package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingDto createBooking(BookingDtoRequest bookingDtoRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id " + userId + " not found");
            throw new NotFoundException("User with id " + userId + " not found");
        });
        Item item = itemRepository.findById(bookingDtoRequest.getItemId()).orElseThrow(() -> {
            log.error("Item with id " + bookingDtoRequest.getItemId() + " not found");
            throw new NotFoundException("Item with id " + bookingDtoRequest.getItemId() + " not found");
        });

        if(!item.getAvailable()) {
            log.error("Item with id " + bookingDtoRequest.getItemId() + " not available");
            throw new ValidationException("Item with id " + bookingDtoRequest.getItemId() + " not available");
        }

        if(bookingDtoRequest.getStart().equals(bookingDtoRequest.getEnd())) {
            log.error("The start time of the booking cannot be equal to the end time of the booking");
            throw new ValidationException("The start time of the booking cannot be equal to the end time of the booking");
        }

        if(item.getOwner().getId().equals(userId)) {
            log.error("The user cannot book an item belonging to him");
            throw new ValidationException("The user cannot book an item belonging to him");
        }

        Booking booking = BookingMapper.mapToBooking(bookingDtoRequest, item, user);
        BookingDto bookingDto = BookingMapper.mapToBookingDto(bookingRepository.save(booking));
        log.info("создан новый booking с ID = {}", bookingDto.getId());
        return bookingDto;
    }

    @Transactional
    @Override
    public BookingDto updateBookingApproved(Long userId, Long bookingId, Boolean approved) {
        userRepository.findById(userId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            log.error("Booking with id " + bookingId + " not found");
            throw new NotFoundException("Booking with id " + bookingId + " not found");
        });

        if(!userId.equals(booking.getItem().getOwner().getId())) {
            throw new ValidationException("The user does not have the right to confirm the booking");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        BookingDto bookingDto = BookingMapper.mapToBookingDto(bookingRepository.save(booking));
        log.info("обнавлен статус approved booking с ID = {}", bookingDto.getId());
        return bookingDto;
    }


}
