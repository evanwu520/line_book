package com.example.linebook.scheduler;

import com.example.linebook.entity.Loan;
import com.example.linebook.repository.LoanRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class NotificationScheduler {


    @Autowired
    LoanRepository loanRepository;


    @Value("${tip_before_days}")
    private int tipBeforeDays;

//    @Scheduled(cron = "0 0 1 * * ?") // Run every day at 1 AM
    @Scheduled(cron = "0 * * * * ?")
    public void sendDueDateNotifications() {
        log.info("sendDueDateNotifications");
        LocalDate notificationDate = LocalDate.now().plusDays(tipBeforeDays);
        List<Loan> upcomingDueLoans = loanRepository.findByDueDateBeforeAndReturnDateIsNull(notificationDate);

        for (Loan loan : upcomingDueLoans) {
            log.info("Notification: The book '{}' is due on {}", loan.getBookCopy().getBook().getTitle(), loan.getDueDate());
        }
    }
}
