package api.service;

import api.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import api.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RewardService {

    @Autowired
    private TransactionRepository transactionRepository;

    public int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (amount - 100) * 2;
            amount = 100;
        }
        if (amount > 50) {
            points += (amount - 50);
        }
        return points;
    }

    public Map<String, Integer> getMonthlyPoints(String customerId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate);
        Map<String, Integer> monthlyPoints = new HashMap<>();

        for (Transaction transaction : transactions) {
            String month = transaction.getDate().getMonth().toString();
            int points = calculatePoints(transaction.getAmount());
            monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
        }

        return monthlyPoints;
    }

    public int getTotalPoints(String customerId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate);
        return transactions.stream().mapToInt(t -> calculatePoints(t.getAmount())).sum();
    }
}