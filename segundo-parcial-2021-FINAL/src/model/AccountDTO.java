package model;

public class AccountDTO {

    private int dni;
    private String surname;
    private int accountNumber;
    private String accountType;

    private float startingCapital;
    private int days;
    private float endingCapital;

    public float getStartingCapital() {
        return startingCapital;
    }

    public void setStartingCapital(float startingCapital) {
        this.startingCapital = startingCapital;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public float getEndingCapital() {
        return endingCapital;
    }

    public void setEndingCapital(float endingCapital) {
        this.endingCapital = endingCapital;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "accountDTO{" + "dni=" + dni + ", surname=" + surname + ", accountNumber=" + accountNumber +
                ", accountType=" + accountType + ", startingCapital=" + startingCapital +
                ", days=" + days +", endingCapital=" + endingCapital +  '}';
    }
}
