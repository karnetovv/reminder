package cos.corp.controller;

import cos.corp.dto.ReminderCreateReqDto;
import cos.corp.dto.ReminderDeleteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
public class ReminderController {

    @PostMapping("reminder/create")
    public ResponseEntity<String> addReminder(@RequestBody ReminderCreateReqDto reminder) {
        return ResponseEntity.ok("Successfully added reminder");
    }

    @DeleteMapping("reminder/delete")
    public ResponseEntity<String> deleteReminder(@RequestBody ReminderDeleteDto reminder) {
        return ResponseEntity.ok("Successfully deleted reminder");
    }

    @GetMapping("sort")
    public ResponseEntity<String> sortReminder(@RequestParam String sort) {
        return ResponseEntity.ok("Successfully sorted reminder");
    }

    @GetMapping("filtr")
    public ResponseEntity<String> filtrReminder(@RequestParam String filter) {
        return ResponseEntity.ok("Successfully filtred reminder");
    }

    @GetMapping("list")
    public ResponseEntity<String> listReminder() {
        return ResponseEntity.ok("Successfully listed reminders");
    }

}


