package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final BookingService bookingService = new BookingServiceImpl(bookingRepository,
        userRepository, itemRepository);
    private static User booker;
    private static User owner;
    private static Item item;
    private static Booking booking;
    private static BookingDto bookingDto;

    private final Sort sortByDesc = Sort.by(Sort.Direction.DESC, "start");


    @BeforeAll
    static void setUp() {
        booker = new User(2L, "name", "user1@mail.com");
        owner = new User(1L, "owner", "user2@mail.com");
        item = new Item(1L, "item", "best", true, owner, null);
        booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, booker,
            BookingStatus.WAITING);
        bookingDto = BookingMapper.mapToBookingDto(booking);

    }

    @Test
    void createBooking() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest(item.getId(), bookingDto.getStart(), bookingDto.getEnd());
        when(bookingRepository.save(any(Booking.class)))
            .thenReturn(booking);
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong()))
            .thenReturn(Optional.of(item));
        BookingDto result = bookingService.createBooking(bookingDtoRequest, booker.getId());
        assertNotNull(result);
        assertEquals(bookingDto, result);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateBookingApproved() {
        Booking bookingApr = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, booker,
            BookingStatus.WAITING);
        when(bookingRepository.findById(anyLong()))
            .thenReturn(Optional.of(bookingApr));
        when(bookingRepository.save(any(Booking.class)))
            .thenReturn(bookingApr);
        BookingDto result = bookingService.updateBookingApproved(1L, 1L, true);
        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        verify(bookingRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void getBooking() {
        when(bookingRepository.save(any(Booking.class)))
            .thenReturn(booking);
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong()))
            .thenReturn(Optional.of(item));
        when(bookingRepository.findById(anyLong()))
            .thenReturn(Optional.of(booking));
        BookingDto result = bookingService.getBooking(1L, 1L);
        assertNotNull(result);
        assertEquals(bookingDto, result);
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllBookingsByItem() {
        when(bookingRepository.findAllByItemId(any(Long.class), any(Sort.class)))
            .thenReturn(List.of(booking));

        List<BookingDtoItem> result = bookingRepository.findAllByItemId(item.getId(), sortByDesc).stream()
            .map(BookingMapper::mapToBookingDtoItem)
            .collect(Collectors.toList());
        assertNotNull(result);
        assertEquals(List.of(BookingMapper.mapToBookingDtoItem(booking)), result);
        verify(bookingRepository, times(1)).findAllByItemId(any(Long.class), any(Sort.class));
    }

    @Test
    void getAllBooking() {
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong()))
            .thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerId(anyLong(), any(Sort.class)))
            .thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllBooking(1L, BookingState.ALL);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
            any(LocalDateTime.class), any(Sort.class)))
            .thenReturn(List.of(booking));
        result = bookingService.getAllBooking(1L, BookingState.CURRENT);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findByBookerIdAndEndBefore(anyLong(), any(LocalDateTime.class),
            any(Sort.class)))
            .thenReturn(List.of(booking));
        result = bookingService.getAllBooking(1L, BookingState.PAST);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findByBookerIdAndStartAfter(anyLong(), any(LocalDateTime.class), any(Sort.class)))
            .thenReturn(List.of(booking));
        result = bookingService.getAllBooking(1L, BookingState.FUTURE);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findByBookerIdAndStatus(anyLong(), any(BookingStatus.class),
            any(Sort.class)))
            .thenReturn(List.of(booking));
        result = bookingService.getAllBooking(1L, BookingState.WAITING);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);
        result = bookingService.getAllBooking(1L, BookingState.REJECTED);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);
    }

    @Test
    public void getAllBookingByOwner() {
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong()))
            .thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemOwnerId(anyLong(), any(Sort.class)))
            .thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllBookingByOwner(1L, BookingState.ALL);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
            any(LocalDateTime.class), any(Sort.class))).thenReturn(List.of(booking));
        result = bookingService.getAllBookingByOwner(1L, BookingState.CURRENT);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findByItemOwnerIdAndEndBefore(anyLong(), any(LocalDateTime.class),
            any(Sort.class)))
            .thenReturn(List.of(booking));
        result = bookingService.getAllBookingByOwner(1L, BookingState.PAST);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findByItemOwnerIdAndStartAfter(anyLong(), any(LocalDateTime.class), any(Sort.class)))
            .thenReturn(List.of(booking));
        result = bookingService.getAllBookingByOwner(1L, BookingState.FUTURE);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);

        when(bookingRepository.findByItemOwnerIdAndStatus(anyLong(), any(BookingStatus.class),
            any(Sort.class)))
            .thenReturn(List.of(booking));
        result = bookingService.getAllBookingByOwner(1L, BookingState.WAITING);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);
        result = bookingService.getAllBookingByOwner(1L, BookingState.REJECTED);
        assertNotNull(result);
        assertEquals(List.of(bookingDto), result);
    }

    @Test
    public void shouldExceptionUpdateBookingApprovedWithFailUser() {
        Booking bookingApr = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, booker,
            BookingStatus.WAITING);
        when(bookingRepository.findById(anyLong()))
            .thenReturn(Optional.of(bookingApr));
        when(bookingRepository.save(any(Booking.class)))
            .thenReturn(bookingApr);

        try {
            bookingService.updateBookingApproved(2L, 1L, true);
            fail("ValidationException expected");
        } catch (ValidationException e) {
            assertTrue(e.getMessage().contains("The user does not have the right to confirm the booking"));
        }
    }

    @Test
    void shouldExceptionWhenGetBookingByFailUser() {
        NotFoundException exp = assertThrows(NotFoundException.class,
            () -> bookingService.getBooking(1L, 1L));
        assertEquals("User with id 1 not found",
            exp.getMessage());
    }

    @Test
    void shouldExceptionWhenGetBookingByFailBooking() {
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(booker));
        NotFoundException exp = assertThrows(NotFoundException.class,
            () -> bookingService.getBooking(1L, 1L));
        assertEquals("Booking with id 1 not found",
            exp.getMessage());
    }
}
