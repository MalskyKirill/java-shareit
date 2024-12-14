package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(Long userId, Sort sortByDesc);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long userId, LocalDateTime now, LocalDateTime now1, Sort sortByDesc);

    List<Booking> findByBookerIdAndEndBefore(Long userId, LocalDateTime now, Sort sortByDesc);

    List<Booking> findByBookerIdAndStartAfter(Long userId, LocalDateTime now, Sort sortByDesc);

    List<Booking> findByBookerIdAndStatus(Long userId, BookingStatus bookingStatus, Sort sortByDesc);

    List<Booking> findAllByItemOwnerId(Long userId, Sort sortByDesc);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(Long userId, LocalDateTime now, LocalDateTime now1, Sort sortByDesc);

    List<Booking> findByItemOwnerIdAndEndBefore(Long userId, LocalDateTime now, Sort sortByDesc);

    List<Booking> findByItemOwnerIdAndStartAfter(Long userId, LocalDateTime now, Sort sortByDesc);

    List<Booking> findByItemOwnerIdAndStatus(Long userId, BookingStatus bookingStatus, Sort sortByDesc);

    Booking findByItemIdAndBookerIdAndStatusAndStartBefore(Long itemId, Long bookerId, BookingStatus bookingStatus,
                                                           LocalDateTime now);
}
