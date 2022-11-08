package com.raisetech.mybatisdemo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/names")
public class NameController {
    private final NameService nameService;

    public NameController(NameService nameService) {
        this.nameService = nameService;
    }

    @GetMapping("/")
    public List<NameResponse> getNames() {
        return nameService.findAll().stream().map(NameResponse::new).toList();
    }

    @GetMapping("/{id}")
    public List<NameResponse> getId(@PathVariable("id") int id) throws Exception {
        return nameService.findById(id).stream().map(NameResponse::new).toList();
    }

    @GetMapping
    public List<NameResponse> idResponse(@RequestParam("id") int id) throws Exception {
        return nameService.findById(id).stream().map(NameResponse::new).toList();
    }

    @GetMapping("/residence")
    public List<NameResponse> getResidence(@RequestParam("residence") String residence) {
        return nameService.findByResidence(residence).stream().map(NameResponse::new).toList();
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFound(
            ResourceNotFoundException e, HttpServletRequest request) {
        Map<String, String> body = Map.of(
                "timestamp", ZonedDateTime.now().toString(),
                "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "message", e.getMessage(),
                "path", request.getRequestURI());
        return new ResponseEntity(body, HttpStatus.NOT_FOUND);
    }
}
