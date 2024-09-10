package CurrencyExchange.Users;

class NormalUser extends User {
    @Override
    public boolean isAdmin() { return false; }
}