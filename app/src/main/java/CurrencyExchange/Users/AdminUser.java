package CurrencyExchange.Users;

class AdminUser extends User {
    @Override
    public boolean isAdmin() { return true; }
}