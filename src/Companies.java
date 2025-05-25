public class Companies {
    public String name;
    public int minInvestment;
    public double riskReward;

    public Companies(String name, int minInvestment, double riskReward) {
        this.name = name;
        this.minInvestment = minInvestment;
        this.riskReward = riskReward;
    }

    public String getName() {
        return name;
    }

    public int getMinInvestment() {
        return minInvestment;
    }

    public double getRiskReward() {
        return riskReward;
    }

}
