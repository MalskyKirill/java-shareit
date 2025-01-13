package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createNewRequest(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST-запрос к эндпоинту: '/requests' на добавление requests");
        return requestClient.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET-запрос к эндпоинту: '/requests' на получение requests");
        return requestClient.getAllRequestsByOwner(userId);
    }

    @GetMapping("/all") // нет тестов в постмане
    public ResponseEntity<Object> getAllRequests() {
        return null;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {
        log.info("GET-запрос к эндпоинту: '/requests/{requestId}' на получение requests");
        return requestClient.getRequestById(userId, requestId);
    }
}
