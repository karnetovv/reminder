package cos.corp.controller;

import cos.corp.domain.entity.Reminder;
import cos.corp.domain.entity.User;
import cos.corp.dto.*;
import cos.corp.mapper.ReminderMapper;
import cos.corp.service.ReminderService;
import cos.corp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("api/v1/")
public class ReminderController {

    private final ReminderService reminderService;
    private final ReminderMapper reminderMapper;
    private final UserService userService;

    public ReminderController(ReminderService reminderService, ReminderMapper reminderMapper, UserService userService) {
        this.reminderService = reminderService;
        this.reminderMapper = reminderMapper;
        this.userService = userService;
    }

    @PostMapping("reminder/create")
    public ResponseEntity<ReminderRespDto> addReminder(@Valid @RequestBody ReminderCreateReqDto reminderDto,
                                                       Authentication authentication) {
        User currentUser = userService.getInternalUser(authentication);
        Reminder reminder = reminderMapper.toEntity(reminderDto);
        Reminder savedReminder = reminderService.createReminder(reminder,currentUser.getId());
        return ResponseEntity.ok(reminderMapper.toDto(savedReminder));
    }

    @PostMapping("reminder/update")
    public ResponseEntity<ReminderRespDto> updateReminder(@Valid @RequestBody ReminderUpdateReqDto reminderDto,
                                              Authentication authentication) {
        User currentUser = userService.getInternalUser(authentication);
        Reminder reminder = new Reminder();
        reminder.setId(reminderDto.id());
        reminderMapper.updateEntityFromDto(reminderDto,reminder);

        Reminder updatedReminder = reminderService.updateReminder(reminder, currentUser.getId());
        return ResponseEntity.ok(reminderMapper.toDto(updatedReminder));
    }

    @DeleteMapping("reminder/delete")
    public ResponseEntity<Void> deleteReminder(@Valid @RequestBody ReminderDeleteDto reminderDto,
                                                 Authentication authentication) {
        User currentUser = userService.getInternalUser(authentication);
        reminderService.deleteReminder(reminderDto.reminderId(), currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<ReminderListRespDto> listReminder(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size,
                                                            @RequestParam(defaultValue = "date") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String direction,
                                                            @RequestParam(required = false) String title,
                                                            @RequestParam(required = false) String description,
                                                            @RequestParam(required = false) LocalDate date,
                                                            @RequestParam(required = false) LocalTime time,
                                                            Authentication authentication) {
        User currentUser = userService.getInternalUser(authentication);

        Sort sort = buildSort(sortBy, direction);

        ReminderListRespDto response = reminderService.listReminders(
                currentUser.getId(),
                PageRequest.of(page, size, sort),
                title,
                description,
                date,
                time
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sort")
    public ResponseEntity<ReminderListRespDto> sortReminder(@RequestParam(name = "by") String by,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size,
                                                            @RequestParam(defaultValue = "asc") String direction,
                                                            Authentication authentication) {
        User currentUser = userService.getInternalUser(authentication);

        Sort sort = buildSort(by, direction);

        ReminderListRespDto response = reminderService.listReminders(
                currentUser.getId(),
                PageRequest.of(page, size, sort),
                null,
                null,
                null,
                null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<ReminderListRespDto> filtrReminder(@RequestParam(required = false) LocalDate date,
                                                             @RequestParam(required = false) LocalTime time,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size,
                                                             @RequestParam(defaultValue = "date") String sortBy,
                                                             @RequestParam(defaultValue = "asc") String direction,
                                                             Authentication authentication) {
        User currentUser = userService.getInternalUser(authentication);

        Sort sort = buildSort(sortBy, direction);

        ReminderListRespDto response = reminderService.listReminders(
                currentUser.getId(),
                PageRequest.of(page, size, sort),
                null,
                null,
                date,
                time
        );

        return ResponseEntity.ok(response);
    }

    private Sort buildSort(String by, String direction) {
        String property;

        switch (by == null ? "" : by.toLowerCase()) {
            case "name":
                property = "title";
                break;
            case "date":
            case "time":
                property = "remind";
                break;
            default:
                property = "remind";
        }

        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        return Sort.by(sortDirection, property);
    }

}


