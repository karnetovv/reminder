package cos.corp.domain.specification;

import cos.corp.domain.entity.Reminder;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class ReminderSpecification {

    private ReminderSpecification() {
    }

    public static Specification<Reminder> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Reminder> titleContains(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Reminder> descriptionContains(String description) {
        return (root, query, cb) ->
                description == null || description.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<Reminder> hasDate(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            return cb.between(root.get("remind"), startOfDay, endOfDay);
        };
    }
}
