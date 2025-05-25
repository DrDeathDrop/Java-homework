public class Player {
    double money;
    int position;
    boolean hasSteppedOnStart = false;

    public Player(double initialMoney) {
        this.money = initialMoney;
        this.position = 0;

    }

    double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void addMoney(double amount) {
        this.money += amount;
    }

    public boolean spendMoney(double amount) {
        if (amount > money) {
            return false;
        }
        this.money -= amount;
        return true;
    }
    public boolean hasSteppedOnStart() {
        return hasSteppedOnStart;
    }

    public void setHasSteppedOnStart(boolean value) {
        this.hasSteppedOnStart = value;
    }



    @Override
    public String toString() {
        return "Player has $" + money;
    }
}
