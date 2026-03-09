package cos.corp.service.impl;

import cos.corp.domain.entity.Reminder;
import cos.corp.domain.entity.User;
import cos.corp.domain.repository.ReminderRepository;
import cos.corp.domain.repository.UserRepository;
import cos.corp.domain.specification.ReminderSpecification;
import cos.corp.dto.ReminderListRespDto;
import cos.corp.dto.ReminderRespDto;
import cos.corp.exception.NotFoundException;
import cos.corp.mapper.ReminderMapper;
import cos.corp.scheduler.ReminderJobScheduler;
import cos.corp.service.ReminderService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReminderServiceImpl implements ReminderService {


    private final ReminderRepository reminderRepo;
    private final UserRepository userRepo;
    private final ReminderJobScheduler scheduleReminder;
    private final ReminderMapper reminderMapper;

    public ReminderServiceImpl(ReminderRepository reminderRepo, UserRepository userRepo, ReminderJobScheduler scheduleReminder, ReminderMapper reminderMapper) {
        this.reminderRepo = reminderRepo;
        this.userRepo = userRepo;
        this.scheduleReminder = scheduleReminder;
        this.reminderMapper = reminderMapper;
    }

    @Override
    @Transactional
    public Reminder createReminder(Reminder reminder, Long userId) {
        validateReminder(reminder);

        User user = userRepo.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        reminder.setUser(user);
        Reminder saved = reminderRepo.save(reminder);
        scheduleReminder.scheduleReminder(saved);
        return saved;
    }

    @Override
    @Transactional
    public Reminder updateReminder(Reminder reminder, Long userId) {
        if (reminder == null || reminder.getId() == null) {
            throw new IllegalArgumentException("Reminder id must not be null");
        }

        Reminder existingReminder = reminderRepo.findByIdAndUserId(reminder.getId(), userId)
                .orElseThrow(() -> new NotFoundException("Reminder not found"));

        if (reminder.getTitle() != null && !reminder.getTitle().isBlank()) {
            existingReminder.setTitle(reminder.getTitle());
        }

        if (reminder.getDescription() != null) {
            existingReminder.setDescription(reminder.getDescription());
        }

        if (reminder.getRemind() != null) {
            if (reminder.getRemind().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Remind datetime must be in the future");
            }
            existingReminder.setRemind(reminder.getRemind());
        }

        Reminder updatedReminder = reminderRepo.save(existingReminder);

        scheduleReminder.rescheduleReminder(updatedReminder);

        return updatedReminder;
    }

    @Override
    public Reminder deleteReminder(Long reminderId, Long userId) {
        Reminder existingReminder = reminderRepo.findByIdAndUserId(reminderId, userId)
                .orElseThrow(() -> new NotFoundException("Reminder not found"));

        scheduleReminder.deleteReminderJob(existingReminder);

        reminderRepo.delete(existingReminder);

        return existingReminder;
    }

    @Override
    @Transactional
    public ReminderListRespDto listReminders(Long userId,
                                             Pageable pageable,
                                             String title,
                                             String description,
                                             LocalDate date,
                                             LocalTime time) {

        Specification<Reminder> specification = Specification
                .where(ReminderSpecification.hasUserId(userId))
                .and(ReminderSpecification.titleContains(title))
                .and(ReminderSpecification.descriptionContains(description))
                .and(ReminderSpecification.hasDate(date));

        Page<Reminder> reminderPage = reminderRepo.findAll(specification, pageable);

        List<Reminder> reminders = reminderPage.getContent();

        if (time != null) {
            reminders = reminders.stream()
                    .filter(reminder -> reminder.getRemind().toLocalTime().withSecond(0).withNano(0)
                            .equals(time.withSecond(0).withNano(0)))
                    .toList();
        }

        List<ReminderRespDto> items = reminders.stream()
                .map(reminderMapper::toDto)
                .toList();

        return new ReminderListRespDto(
                reminderPage.getTotalElements(),
                reminderPage.getNumber(),
                reminderPage.getSize(),
                items
        );
    }

    private void validateReminder(Reminder reminder) {
        if (reminder == null) {
            throw new IllegalArgumentException("Reminder must not be null");
        }

        if (reminder.getTitle() == null || reminder.getTitle().isBlank()) {
            throw new IllegalArgumentException("Reminder title must not be blank");
        }

        if (reminder.getRemind() == null) {
            throw new IllegalArgumentException("Reminder datetime must not be null");
        }

        if (reminder.getRemind().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Remind datetime must be in the future");
        }
    }
}
