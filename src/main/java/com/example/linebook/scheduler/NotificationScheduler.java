package com.example.linebook.scheduler;

import com.example.linebook.entity.Loan;
import com.example.linebook.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    LoanRepository loanRepository;



    @Value("${tip_before_days}")
    private int tipBeforeDays;

    @Scheduled(cron = "0 0 1 * * ?") // Run every day at 1 AM
    public void sendDueDateNotifications() {
        LocalDate notificationDate = LocalDate.now().plusDays(tipBeforeDays);
        List<Loan> upcomingDueLoans = loanRepository.findByDueDateBeforeAndReturnDateIsNull(notificationDate);

        for (Loan loan : upcomingDueLoans) {
            System.out.println("Notification: The book '" + loan.getBookCopy().getBook().getTitle() + "' is due on " + loan.getDueDate());
        }
    }
}
