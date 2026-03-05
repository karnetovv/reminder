package cos.corp.service.Impl;

import cos.corp.domain.entity.Reminder;
import cos.corp.domain.entity.User;
import cos.corp.domain.repository.ReminderRepository;
import cos.corp.domain.repository.UserRepository;
import cos.corp.service.ReminderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReminderServiceImpl implements ReminderService {

    @Autowired
    private ReminderRepository reminderRepo;

    @Autowired
    private UserRepository userRepo;


    @Override
    @Transactional
    public Reminder createReminder(Reminder reminder, Long userId) {
        if (reminder == null)
            throw new IllegalArgumentException("Reminder is null");

        User user = userRepo.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        Reminder saved = reminderRepo.save(reminder);

        return saved;
    }

    @Override
    @Transactional
    public Reminder updateReminder(Reminder reminder, Long userId) {
        if (reminder == null)
            throw new IllegalArgumentException("Reminder is null");
        

        return null;
    }

    @Override
    public Reminder deleteReminder(Long reminderId, Long userId) {
        return null;
    }
}
