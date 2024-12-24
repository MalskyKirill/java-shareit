package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResp;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public ItemRequestDtoResp createNewRequest(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST-запрос к эндпоинту: '/requests' на добавление requests");
        return requestService.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDtoResp> getAllRequestsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET-запрос к эндпоинту: '/requests' на получение requests");
        return requestService.getAllRequestsByOwner(userId);
    }

    @GetMapping("/all") // нет тестов в постмане
    public List<ItemRequestDtoResp> getAllRequests() {
        return null;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoResp getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {
        log.info("GET-запрос к эндпоинту: '/requests/{requestId}' на получение requests");
        return requestService.getRequestById(userId, requestId);
    }
}
