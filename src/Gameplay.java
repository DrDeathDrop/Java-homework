import java.util.Random;
import java.util.Scanner;


public class Gameplay {

    static final int TRAPS = 7;
    static final int INVESTS = 3;
    static final int PARTIES = 4;
    static final int CHANCES = 3;
    static final int STEALS = 3;


    public static boolean isTrapActive = false;
    public static boolean isIrsSent = false;
    public static boolean isDivorceActive = false;
    public static boolean isPropagandaActive = false;
    public static boolean isMoraleBoostActive = false;
    public static boolean isGamblingBossActive = false;
    public static String selectedCompanyForInvestment = null;
    public static int invested = 0;

    public static Scanner inp = new Scanner(System.in);
    public static Random rng = new Random();
    public static Player player = new Player(1000);
    public static Bot bot = new Bot(1000);


    public static void main(String[] args) {

        int firstTurn = rng.nextInt(1, 3);
        boolean isPlayerFirst = firstTurn == 1;


        System.out.println("Let's see whether the player or the bot is first: " + isPlayerFirst);

        System.out.println("""
                   This is what the
                  gameboard looks like
                        GLHF <3
                |X| |X| |X| |X| |X| |X| |X|
                |X|                     |X|
                |X|                     |X|
                |X| |X| |X| |X| |X| |X| |S|
                
                """);

        simulateLoading("Starting game", 4, 500);
        while (player.getMoney() > 0 && bot.getMoney() > 0) {

            String[] gameBoard = initializeGameBoard();

            while (!player.hasSteppedOnStart() || !bot.hasSteppedOnStart()) {
                if (player.getMoney() <= 0 || bot.getMoney() <= 0) {
                    break;
                }

                double win = oneBoardCycle(gameBoard, 0);
                simulateLoading("You stepped on start calculating return on investment", 5, 500);
                System.out.println("Your investment has returned, you have won/lost: " + win);
                player.addMoney(win);

                if (player.hasSteppedOnStart && bot.hasSteppedOnStart) {
                    player.hasSteppedOnStart = false;
                    bot.hasSteppedOnStart = false;
                    break;
                }
            }
            System.out.println("Cycle finished.");
        }

        System.out.println("Game over.");

    }

    public static double oneBoardCycle(String[] gameBoard, double win) {
        for (int i = 0; i < gameBoard.length; i++) {

            if (player.hasSteppedOnStart || bot.hasSteppedOnStart) {
                continue;
            }

            if (i % 2 == 0) {
                botLogic();
            } else {
                if (i == gameBoard.length) {
                    stepOnStart(player);
                    break;
                } else {
                    switch (gameBoard[i].toLowerCase()) {
                        case "i": {
                            stepOnInvest(player);
                            win = returnOfInvestment(selectedCompanyForInvestment, invested );

                            System.out.println("Your money: " + player.getMoney() + "$");
                            break;
                        }
                        case "c": {
                            stepOnChance(player);
                            System.out.println("Your money: " + player.getMoney() + "$");
                            break;
                        }
                        case "t": {
                            stepOnTrap(player);
                            System.out.println("Your money: " + player.getMoney() + "$");
                            break;
                        }
                        case "p": {
                            stepOnParty(player);
                            System.out.println("Your money: " + player.getMoney() + "$");
                            break;
                        }
                        case "s": {
                            stepOnSteal(player, gameBoard[i]);
                            System.out.println("Your money: " + player.getMoney() + "$");
                            System.out.println("The bot's money: " + bot.getMoney() + "$");
                            break;
                        }
                        case "start": {
                            System.out.println("Your investment has returned, you have won/lost: " + win);
                            player.addMoney(win);
                            stepOnStart(player);
                            break;
                        }
                    }
                }

            }
        }
        return win;
    }

    public static String[] stepOnStart(Player player) {
        player.hasSteppedOnStart = true;
        System.out.println(" reached START! Regenerating the board...");
        returnOfInvestment(selectedCompanyForInvestment, invested);
        return initializeGameBoard();
    }

    public static void stepOnSteal(Player player, String square) {
        int plan = rng.nextInt(0, 4);
        if (plan == 1) {
            if (square.equalsIgnoreCase("c")) {
                player.addMoney(100);
            } else if (square.equalsIgnoreCase("i")) {
                player.addMoney(100);
            } else if (square.equalsIgnoreCase("s")) {
                player.addMoney(100);
            }
        }
    }

    public static void botLogic() {
        int botMove = rng.nextInt(1, 3);
        if (botMove == 1) {
            bot.spendMoney(rng.nextInt(1, 251));
        } else {
            bot.addMoney(rng.nextInt(1, 251));
        }
        System.out.println("The bot made its move");
    }

    public static String[] initializeGameBoard() {
        String[] gameBoard = new String[20];
        int index = 0;
        for (int i = 0; i < INVESTS; i++) gameBoard[index++] = "I";
        for (int i = 0; i < CHANCES; i++) gameBoard[index++] = "C";
        for (int i = 0; i < TRAPS; i++) gameBoard[index++] = "T";
        for (int i = 0; i < STEALS; i++) gameBoard[index++] = "S";
        for (int i = 0; i < PARTIES; i++) gameBoard[index++] = "P";
        randomizeBoard(gameBoard);
        gameBoard[0] = "start";

        return gameBoard;
    }

    //Този метод е приватизиран (най-мазно откраднат) от GitHub :)
    public static void simulateLoading(String message, int dots, int delayMs) {
        System.out.print(message);
        for (int i = 0; i < dots; i++) {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.print(".");
        }
        System.out.println();
    }

    public static void stepOnTrap(Player player) {
        if (!isTrapActive) {
            System.out.println("You stepped on a trap, choose what to do to your opponent :)");
            System.out.println("""
                    (1) Send the IRS to your opponent
                    (2) Divorce :(
                    (3) Propaganda (German tactics)
                    (4) Morale Boost
                    (5) G A M B L I N G boss
                    (N) Nah, I like having friends
                    """);

            String trapChoice = inp.nextLine();
            switch (trapChoice) {
                case "1": {
                    isIrsSent = true;
                    player.spendMoney(10);
                    break;
                }
                case "2": {
                    isDivorceActive = true;
                    player.spendMoney(20);
                    break;
                }
                case "3": {
                    isPropagandaActive = true;
                    player.spendMoney(100);
                    break;
                }
                case "4": {
                    isMoraleBoostActive = true;
                    player.spendMoney(50);
                    break;
                }
                case "5": {
                    isGamblingBossActive = true;
                    player.spendMoney(100);
                    break;
                }
                case "N": {
                    break;
                }
            }
        } else {
            System.out.println("Uh-oh, you stepped on a trap, later on, you'll find out what your punishment is :(");
        }
    }

    public static void stepOnChance(Player player) {
        int coinFlip = rng.nextInt(1, 2);
        if (coinFlip == 1) {
            pickBadFate(player);
        } else {
            pickGoodFate(player);
        }
    }

    public static void stepOnParty(Player player) {
        System.out.println("You party hard in EXE night club. You lost 25 bucks, but had an amazing time :)");
        player.spendMoney(25);
    }

    public static void stepOnInvest(Player player) {
        Companies[] companies = storeCompanies();
        int maxInvestments = 3;
        int investmentsDone = 0;

        while (investmentsDone < maxInvestments) {
            System.out.println("You stepped on invest, let's talk business, you can invest in the following companies:");

            for (int i = 0; i < companies.length; i++) {
                Companies companyList = companies[i];
                System.out.printf("(%d) %s | min : %d | risk/reward : %.1f%n",
                        i + 1, companyList.getName(),
                        companyList.getMinInvestment(),
                        companyList.getRiskReward());
            }

            System.out.println("(N) I don't really want to invest :/");
            String input = inp.nextLine().trim();

            if (input.equalsIgnoreCase("N")) {
                System.out.println("You chose not to invest right now.");
                break;
            }

            if (input.length() != 1 || input.charAt(0) < '1' || input.charAt(0) > ('0' + companies.length)) {
                System.out.println("Invalid choice, please try again.");
                continue;
            }
            int choice = input.charAt(0) - '0';
            Companies selectedCompany = companies[choice - 1];

            System.out.println("You have chosen " + selectedCompany.getName() + ". How much will you invest? (or press N to cancel)");
            String investInput = inp.nextLine().trim();
            investmentsDone++;

            if (investInput.equalsIgnoreCase("N")) {
                System.out.println("Investment cancelled, back to company selection.");
                continue;
            }

            int investmentAmount = Integer.parseInt(investInput);

            if (investmentAmount < selectedCompany.getMinInvestment()) {
                System.out.println("Amount is less than the minimum required, try again.");
                continue;
            }

            if (investmentAmount > player.getMoney()) {
                System.out.println("You don't have enough money, try again.");
                continue;
            }

            player.spendMoney(investmentAmount);
            System.out.println("You invested " + investmentAmount + " in " + selectedCompany.getName() + ".");
            selectedCompanyForInvestment = selectedCompany.getName();
            invested = investmentAmount;
            investmentsDone++;

            if (investmentsDone < maxInvestments) {
                System.out.println("You can invest " + (maxInvestments - investmentsDone) + " more time(s).");
            } else {
                System.out.println("You have reached the maximum number of investments for this turn.");
            }
            break;
        }
    }

    public static Companies[] storeCompanies() {
        Companies[] companies = {
                new Companies("Evel Co", 500, 0.2),
                new Companies("Bombs Away", 400, 0.5),
                new Companies("Clock Work Orange", 300, 1.5),
                new Companies("Marauders United", 200, 2.0),
                new Companies("Fatcat Inc.", 100, 2.5),
                new Companies("Macrosoft", 50, 5.0)
        };
        return companies;
    }

    public static void pickGoodFate(Player player) {
        int fate = rng.nextInt(1, 101);

        if (fate >= 1 && fate <= 39) {
            System.out.println("You adopted some orphans, people love you :D You won 50 bucks");
            player.addMoney(50);
        } else if (fate >= 40 && fate <= 65) {
            System.out.println("You rizzed up a hot chick, people view you as a legendary chad :D You won 100 bucks");
            player.addMoney(100);
        } else if (fate >= 66 && fate <= 79) {
            System.out.println("You dropped out and became a millionaire, good job Mr. Zuckerberg :D You won 150 bucks");
            player.addMoney(150);
        } else if (fate >= 80 && fate <= 94) {
            System.out.println("You exploited so teenagers for your own benefit, cruel, but efficient :D You won 200 bucks");
            player.addMoney(200);
        } else if (fate > 94) {
            System.out.println("You employed a dwarf, you are not RACEist, people love, congrats :D You won 250 bucks");
            player.addMoney(250);
        }
    }

    public static void pickBadFate(Player player) {

        int fate = rng.nextInt(1, 101);

        if (fate >= 1 && fate <= 39) {
            System.out.println("Oh no, you had too much to drink and snort, you wake up with your TV stolen :( You lost 50 bucks");
            player.spendMoney(50);
        } else if (fate >= 40 && fate <= 65) {
            System.out.println("You forgot to use protection, congratulations !!! You lost 100 bucks");
            player.spendMoney(100);
        } else if (fate >= 66 && fate <= 79) {
            System.out.println("Welp, your best employee was kidnapped :( You lost 150 bucks");
            player.spendMoney(150);
        } else if (fate >= 80 && fate <= 94) {
            System.out.println("You were fighting a monster. In the process, you broke your pinky finger :( You lost 200 bucks");
            player.spendMoney(200);
        } else if (fate > 94) {
            System.out.println("Your employees have went on a strike like the one in Sofia :(  You lost 250 bucks");
            player.spendMoney(250);
        }
    }

    public static void randomizeBoard(String[] board) {
        String temp = null;
        int rngIndex = 0;

        for (int i = 1; i < board.length; i++) {
            rngIndex = rng.nextInt(board.length);
            temp = board[rngIndex];
            board[rngIndex] = board[i];
            board[i] = temp;
        }
    }

    public static double returnOfInvestment(String name, int investedAmount) {
        int investmentOutcome = 0;
        double finalAmount = investedAmount;

        if (name == null){
            System.out.print("");
        } else {
            switch (name.toLowerCase()) {
                case "macrosoft":
                    investmentOutcome = rng.nextInt(-20, 31);
                    if (investmentOutcome > 0) {
                        finalAmount *= 5;
                    } else {
                        finalAmount *= 0.2;
                    }
                    break;
                case "fatcat inc.":
                    investmentOutcome = rng.nextInt(-25, 101);
                    if (investmentOutcome > 0) {
                        finalAmount *= 2.5;
                    } else {
                        finalAmount *= 0.4;
                    }
                    break;
                case "marauders united":
                    investmentOutcome = rng.nextInt(-18, 51);
                    if (investmentOutcome > 0) {
                        finalAmount *= 2;
                    } else {
                        finalAmount *= 0.5;
                    }
                    break;
                case "clock work orange":
                    investmentOutcome = rng.nextInt(-15, 36);
                    if (investmentOutcome > 0) {
                        finalAmount *= 1.5;
                    } else {
                        finalAmount *= 0.66;
                    }
                    break;
                case "bombs away":
                    investmentOutcome = rng.nextInt(-10, 50);
                    if (investmentOutcome > 0) {
                        finalAmount *= 0.5;
                    } else {
                        finalAmount *= 2.0;
                    }
                    break;
                case "evel co":
                    investmentOutcome = rng.nextInt(-5, 20);
                    if (investmentOutcome > 0) {
                        finalAmount *= 0.2;
                    } else {
                        finalAmount *= 5.0;
                    }
                    break;
                default:{
                    break;
                }
            }
        }
        return finalAmount;
    }
}
