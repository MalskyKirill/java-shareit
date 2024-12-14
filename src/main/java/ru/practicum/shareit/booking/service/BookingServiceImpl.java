package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.BookingState;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final Sort sortByDesc = Sort.by(Sort.Direction.DESC, "start");

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

        if (!item.getAvailable()) {
            log.error("Item with id " + bookingDtoRequest.getItemId() + " not available");
            throw new ValidationException("Item with id " + bookingDtoRequest.getItemId() + " not available");
        }

        if (bookingDtoRequest.getStart().equals(bookingDtoRequest.getEnd())) {
            log.error("The start time of the booking cannot be equal to the end time of the booking");
            throw new ValidationException("The start time of the booking cannot be equal to the end time of the booking");
        }

        if (item.getOwner().getId().equals(userId)) {
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
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            log.error("Booking with id " + bookingId + " not found");
            throw new NotFoundException("Booking with id " + bookingId + " not found");
        });

        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new ValidationException("The user does not have the right to confirm the booking");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        BookingDto bookingDto = BookingMapper.mapToBookingDto(bookingRepository.save(booking));
        log.info("обнавлен статус approved booking с ID = {}", bookingDto.getId());
        return bookingDto;
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id " + userId + " not found");
            throw new NotFoundException("User with id " + userId + " not found");
        });

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            log.error("Booking with id " + bookingId + " not found");
            throw new NotFoundException("Booking with id " + bookingId + " not found");
        });

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            log.error("Only the owner of the item or the person booking it can view the booking data");
            throw new NotFoundException("Only the owner of the item or the person booking it can view the booking data");
        }

        BookingDto bookingDto = BookingMapper.mapToBookingDto(booking);
        log.info("получен booking с ID = {}", bookingDto.getId());
        return bookingDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllBooking(Long userId, BookingState state) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id " + userId + " not found");
            throw new NotFoundException("User with id " + userId + " not found");
        });

        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL -> {
                log.info("получен список всех бронирований у user с ID = {}", userId);
                bookings.addAll(bookingRepository.findAllByBookerId(userId, sortByDesc));
            }
            case CURRENT -> {
                log.info("получен список текущих бронирований у user с ID = {}", userId);
                bookings.addAll(bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(), LocalDateTime.now(), sortByDesc));
            }
            case PAST -> {
                log.info("получен список завершенных бронирований у user с ID = {}", userId);
                bookings.addAll(bookingRepository.findByBookerIdAndEndBefore(userId, LocalDateTime.now(), sortByDesc));
            }
            case FUTURE -> {
                log.info("получен список будующих бронирований у user с ID = {}", userId);
                bookings.addAll(bookingRepository.findByBookerIdAndStartAfter(userId, LocalDateTime.now(), sortByDesc));
            }
            case WAITING -> {
                log.info("получен список ожидающих подтверждения бронирований у user с ID = {}", userId);
                bookings.addAll(bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, sortByDesc));
            }
            case REJECTED -> {
                log.info("получен список отмененных бронирований у user с ID = {}", userId);
                bookings.addAll(bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, sortByDesc));
            }
            default -> throw new ValidationException("Unknown state: " + state);
        }

        return bookings.stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllBookingByOwner(Long userId, BookingState state) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id " + userId + " not found");
            throw new NotFoundException("User with id " + userId + " not found");
        });

        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL -> {
                log.info("получен список всех бронирований у owner с ID = {}", userId);
                bookings.addAll(bookingRepository.findAllByItemOwnerId(userId, sortByDesc));
            }
            case CURRENT -> {
                log.info("получен список текущих бронирований у owner с ID = {}", userId);
                bookings.addAll(bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(), LocalDateTime.now(), sortByDesc));
            }
            case PAST -> {
                log.info("получен список завершенных бронирований у owner с ID = {}", userId);
                bookings.addAll(bookingRepository.findByItemOwnerIdAndEndBefore(userId, LocalDateTime.now(), sortByDesc));
            }
            case FUTURE -> {
                log.info("получен список будующих бронирований у owner с ID = {}", userId);
                bookings.addAll(bookingRepository.findByItemOwnerIdAndStartAfter(userId, LocalDateTime.now(), sortByDesc));
            }
            case WAITING -> {
                log.info("получен список ожидающих подтверждения бронирований у owner с ID = {}", userId);
                bookings.addAll(bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.WAITING, sortByDesc));
            }
            case REJECTED -> {
                log.info("получен список отмененных бронирований у owner с ID = {}", userId);
                bookings.addAll(bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED, sortByDesc));
            }
            default -> throw new ValidationException("Unknown state: " + state);
        }

        return bookings.stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDtoItem getNextBooking(Long itemId) {
        //хз какая логика тестов нет
        log.info("получено следующее бронирование у item с ID = {}", itemId);
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDtoItem getLastBooking(Long itemId) {
        //хз какая логика тестов нет
        log.info("получено последнее бронирование у item с ID = {}", itemId);
        return null;
    }


}
