package CurrencyExchange.Users;

import java.time.LocalDate;

class ExchangeRate {
    private double rate;
    private LocalDate date;

    public ExchangeRate(double rate, LocalDate date) {
        this.rate = rate;
        this.date = date;
    }

    public double getRate() { return rate; }
    public LocalDate getDate() { return date; }
}