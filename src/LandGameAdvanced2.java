public class LandGameAdvanced2 {
    final static int length = 6;
    final static int width = 2;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    static boolean[][] player1choices = new boolean[length][width];
    static boolean[][] player2choices = new boolean[length][width];
    static boolean[][] player1Land = new boolean[length][width];
    static boolean[][] player2Land = new boolean[length][width];
    static boolean[][] containsTurret = new boolean[length][width];
    static boolean[][] containsSecret = new boolean[length][width];
    static boolean player2dead;
    static boolean player1dead;
    static int[][] board = new int[length][width];
    static int[][] boardSecrets = new int[length][width];
    static int[][] boardFoodFacility = new int[length][width];
//    static int[][] boardTrainingCamp = new int[length][width];
//    static int[][] boardSecretFacility = new int[length][width];
//    static int[][] boardNukeFacility = new int[length][width];
    static int[][] boardTurret = new int[length][width];
    static char[][] ABCboard = new char[length][width];
    static int[][] intboard = new int[length][width];
    public static void main(String[] args) {
        System.out.println("Would you like to see the instructions? 1: Yes \nAny Number: No");
        if(getInt.getInt() == 1){
            printInstructions();
        }
        initializationOfABCboard();
        initializationOfIntBoard();
        printBoard();

        player1firstchoice();
        printBoard();
        pressEnterToContinue();

        player2firstchoice();
        printBoard();
        pressEnterToContinue();

        showNearbyTowns();
        printBoard();
        pressEnterToContinue();

        //StartGame
        boolean player1win = playGame();

        //win or not
        if(player1win == true){
            System.out.println("Player 1, you win!");
        }else{
            System.out.println("Player 2, you win!");
        }
    }

    static void printInstructions(){
        System.out.println("These are the instructions:");
        System.out.println("This game is a player versus player game");
        System.out.println("The key in winning is killing the other player");
        System.out.println("You can do this by attacking, but keep in mind you will have to defend too");
        System.out.println("There are three ways of attacking, invading adjacent lands, deploying men by air, or dropping a nuke.");
        System.out.println("You can also construct buildings that can boost your men count or take down invaders");
        System.out.println("If you anticipate what the opponent wants to do or just simply want to boost your men count, you can create a secret");
        System.out.println("At the end of your turn, you roll a dice to determine how many men you get");
        System.out.println("Staying boosts your men count.");
    }
    static void initializationOfABCboard(){
        char ch = 'A';
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                ABCboard[j][i] = ch;
                ch++;
            }
        }
    }
    static void initializationOfIntBoard(){
        int count = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                intboard[i][j] = count++;
            }
        }
    }
    static void showNearbyTowns(){
        System.out.println("These are the towns nearby:");
        Die d = new Die(8);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(board[i][j] == 0){
                    board[i][j] = d.roll();
                }
            }
        }
    }
    static void player1firstchoice(){
        System.out.println("Player 1, choose a box to start at by entering in a letter");
        String boxStartAt = getLetter.getLetter(length*width);
        DicePair p = new DicePair();
        System.out.println("Press enter to roll");
        PressToContinue.pressToContinue();
        int total = p.roll();
        System.out.println("You rolled a total of " + total);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                if(ABCboard[j][i] == boxStartAt.charAt(0)){
                    System.out.println("You took over box "+boxStartAt+" with an attack value of "+total);
                    board[j][i] = total;
                    player1Land[j][i] = true;
                    addChoicesForPlayer1(j, i);
                    break;
                }
            }
        }
    }
    static void player2firstchoice()
    {
        System.out.println("Player 2, choose a box to start at by entering in a letter");
        System.out.println("Please don't choose the same box as player 1");
        String boxStartAt = getLetter.getLetter(length*width);
        DicePair p = new DicePair();
        System.out.println("Press enter to roll");
        PressToContinue.pressToContinue();
        int total = p.roll()+3;
        System.out.println("You rolled a total of " + total);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                if(ABCboard[j][i] == boxStartAt.charAt(0)){
                    System.out.println("You took over box "+boxStartAt+" with an attack value of "+total);
                    board[j][i] = total;
                    player2Land[j][i] = true;
                    addChoicesForPlayer2(j, i);
                    break;
                }
            }
        }
    }

    static boolean playGame(){
        //------------------------------------------------------------------------------------------
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(boardSecrets[i][j] == 4){
                    System.out.println("A secret was triggered!");
                    System.out.println("A reinforcement of 10 troops have come");
                    board[i][j]+=10;
                    containsSecret[i][j] = false;
                    boardSecrets[i][j] = 0;
                }
            }
        }
        //------------------------------------------------------------------------------------------
        organizeChoices(true);
        System.out.println("Player1: The options of what you can do are:");
        playerChoice(true);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(player1Land[i][j] == true){
                    player2Land[i][j] = false;
                }
            }
        }
        if(player2dead){
            return true;
        }
        //------------------------------------------------------------------------------------------
        organizeChoices(false);
        System.out.println("Player2: The options of what you can do are:");
        playerChoice(false);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(player2Land[i][j] == true){
                    player1Land[i][j] = false;
                }
            }
        }
        if(player1dead){
            return true;
        }
        //------------------------------------------------------------------------------------------
        if(playGame() == true){
            return true;
        }else{
            return false;
        }
    }
    static void playerChoice(boolean isPlayer1){
        System.out.println("1. Attack \n2. Create Secret \n3. Build \n4. Stay \n5. Surrender ");
        int choice1 = getInt.getInt();
        switch (choice1){
            case 1:
                printBoard();
                playerAttack(isPlayer1);
                pressEnterToRollAndEndTurn(isPlayer1);
                break;
            case 2:
                printBoard();
                playerCreateSecret(isPlayer1);
                pressEnterToRollAndEndTurn(isPlayer1);
                break;
            case 3:
                printBoard();/*
                System.out.println("Would you like to \n1. Build \n2. Upgrade");
                int choice3 = getInt.getInt();
                if(choice3 == 1){*/
                    playerBuild(isPlayer1);
                /*}else{
//                    playerUpgrade(true);
                }*/
                pressEnterToRollAndEndTurn(isPlayer1);
                break;
            case 4:
                printBoard();
                if(isPlayer1) {
                    for (int i = 0; i < length; i++) {
                        for (int j = 0; j < width; j++) {
                            if (player1Land[i][j] == true) {
                                board[i][j]+=5;
                            }
                        }
                    }
                }else{
                    for (int i = 0; i < length; i++) {
                        for (int j = 0; j < width; j++) {
                            if (player2Land[i][j] == true) {
                                board[i][j]+=5;
                            }
                        }
                    }
                }
                pressEnterToRollAndEndTurn(isPlayer1);
                break;
            case 5:
                if(isPlayer1){
                    player1dead = true;
                }else{
                    player2dead = true;
                }
                break;
            default:
                playerChoice(isPlayer1);
        }
    }
    static void playerAttack(boolean isPlayer1){
        System.out.println("Which method will you like to use to attack: \n1. Invasion by Land \n2. Deploy soldiers from air \n3. Drop nuclear bomb");
        int choice1 = getInt.getInt();
        switch(choice1){
            case 1:
                System.out.println("Which land would you like to attack?");
                String boxToAttack = checkAndValidateLand(true,true,isPlayer1);
                invasionbyLand(boxToAttack,isPlayer1);
                break;
            case 2:
                invasionbyAir(isPlayer1);
                break;
            case 3:
                dropNuclearBomb(isPlayer1);
                break;
            default:
                System.out.println("Please enter in a number from 1-3");
                playerAttack(isPlayer1);
                break;
        }
    }
        static void invasionbyLand(String boxToAttack, boolean isPlayer1){
        int[] attackChoice = getIntegerCoordinates(boxToAttack);
        int[] attackFromLand = getAttackFromLand(attackChoice, isPlayer1);
        String boxToAttackFrom;
        if (attackFromLand == null) {
            System.out.println("Please select a land to attack from");
            boxToAttackFrom = checkAndValidateLand(false, true,isPlayer1);
        } else {
            boxToAttackFrom = getLetterCoordinates(attackFromLand);
        }
        int[] attackFrom = getIntegerCoordinates(boxToAttackFrom);
        if(boardSecrets[attackChoice[0]][attackChoice[1]] == 3) {
            System.out.println("You triggered a secret!");
            System.out.println("A reinforcement of 10 came to protect the land!");
            board[attackChoice[0]][attackChoice[1]]+=10;
            boardSecrets[attackChoice[0]][attackChoice[1]] = 0;
            containsSecret[attackChoice[0]][attackChoice[1]] = false;
        }
        if(boardTurret[attackChoice[0]][attackChoice[1]] == 1){
            int troopsDied = (int) (board[attackFrom[0]][attackFrom[1]]*0.3);
            System.out.println("The turret took out "+troopsDied+" of your troops");
            board[attackFrom[0]][attackFrom[1]]-=troopsDied;
        }
        if(isPlayer1) {
            if (board[attackFrom[0]][attackFrom[1]] >= board[attackChoice[0]][attackChoice[1]]) {
                System.out.println("You took over their land!");
                player1Land[attackChoice[0]][attackChoice[1]] = true;
                player2Land[attackChoice[0]][attackChoice[1]] = false;
                addChoicesForPlayer1(attackChoice[0], attackChoice[1]);
                organizeChoices(false);
                board[attackFrom[0]][attackFrom[1]] -= board[attackChoice[0]][attackChoice[1]];
                printBoard();
                checkIfPlayerAlive(false);
            } else {
                System.out.println("You lost the battle!");
                board[attackChoice[0]][attackChoice[1]] -= board[attackFrom[0]][attackFrom[1]];
                board[attackFrom[0]][attackFrom[1]] = 0;
            }
        }else{
            if (board[attackFrom[0]][attackFrom[1]] >= board[attackChoice[0]][attackChoice[1]]) {
                System.out.println("You took over their land!");
                player2Land[attackChoice[0]][attackChoice[1]] = true;
                player1Land[attackChoice[0]][attackChoice[1]] = false;
                addChoicesForPlayer2(attackChoice[0], attackChoice[1]);
                organizeChoices(true);
                board[attackFrom[0]][attackFrom[1]] -= board[attackChoice[0]][attackChoice[1]];
                checkIfPlayerAlive(true);
            } else {
                System.out.println("You lost the battle!");
                board[attackChoice[0]][attackChoice[1]] -= board[attackFrom[0]][attackFrom[1]];
                board[attackFrom[0]][attackFrom[1]] = 0;
            }
        }
    }
            static int[] getAttackFromLand(int[] coordinate, boolean isPlayer1){
        int x = coordinate[0];
        int y = coordinate[1];
        int[] newCoordinate = new int[2];
        int count = 0;
        if(isPlayer1) {
            if (x + 1 < length) {
                if (player1Land[x + 1][y] == true) {
                    newCoordinate[0] = x + 1;
                    newCoordinate[1] = y;
                    count++;
                }
            }
            if (x - 1 >= 0) {
                if (player1Land[x - 1][y] == true) {
                    newCoordinate[0] = x-1;
                    newCoordinate[1] = y;
                    count++;
                }
            }
            if (y + 1 < width) {
                if (player1Land[x][y + 1] == true) {
                    newCoordinate[0] = x;
                    newCoordinate[1] = y+1;
                    count++;
                }
            }
            if (y - 1 >= 0) {
                if (player1Land[x][y - 1] == true) {
                    newCoordinate[0] = x;
                    newCoordinate[1] = y-1;
                    count++;
                }
            }
        }else{
            if (x + 1 < length) {
                if (player2Land[x + 1][y] == true) {
                    newCoordinate[0] = x + 1;
                    newCoordinate[1] = y;
                    return newCoordinate;
                }
            }
            if (x - 1 >= 0) {
                if (player2Land[x - 1][y] == true) {
                    newCoordinate[0] = x-1;
                    newCoordinate[1] = y;
                    return newCoordinate;
                }
            }
            if (y + 1 < width) {
                if (player2Land[x][y + 1] == true) {
                    newCoordinate[0] = x;
                    newCoordinate[1] = y+1;
                    return newCoordinate;
                }
            }
            if (y - 1 >= 0) {
                if (player2Land[x][y - 1] == true) {
                    newCoordinate[0] = x;
                    newCoordinate[1] = y-1;
                    return newCoordinate;
                }
            }
        }
        if(count == 1){
            return newCoordinate;
        }else{
            return null;
        }
    }
        static void invasionbyAir(boolean isPlayer1){
        System.out.println("Which land would you like to deploy your troops from?");
        String boxToAttackFrom = checkAndValidateLand(false,false,isPlayer1);
        System.out.println("Where would you like to deploy your troops?");
        String boxToAttack = checkAndValidateLand(true,false,isPlayer1);
        System.out.println("How many troops will you deploy?");
        int numtroops = getInt.getInt();
        int[] coordinatesFrom = getIntegerCoordinates(boxToAttackFrom);
        int[] coordinatesAttack = getIntegerCoordinates(boxToAttack);
        if(board[coordinatesFrom[0]][coordinatesFrom[1]]-numtroops>=0){
            board[coordinatesFrom[0]][coordinatesFrom[1]]-=numtroops;
        }else{
            System.out.println("Please enter in a number that is smaller than your total number of troops");
            invasionbyAir(isPlayer1);
            return;
        }
        int troopsDiedLanding = (int) (Math.random()*(numtroops/2));
        System.out.println("The number of troops that died while landing is "+troopsDiedLanding);
        numtroops-=troopsDiedLanding;
        if(boardSecrets[coordinatesAttack[0]][coordinatesAttack[1]]==2){
            System.out.println("A secret was triggered!");
            System.out.println("Reinforcements of 10 came to protect the land");
            board[coordinatesAttack[0]][coordinatesAttack[1]]+=10;
            boardSecrets[coordinatesAttack[0]][coordinatesAttack[1]] = 0;
            containsSecret[coordinatesAttack[0]][coordinatesAttack[1]] = false;
        }
        if(boardTurret[coordinatesFrom[0]][coordinatesFrom[1]] == 1){
            int troopsDied = (int) (board[coordinatesFrom[0]][coordinatesFrom[1]]*0.3);
            System.out.println("The turret took out "+troopsDied+" of your troops left");
            board[coordinatesFrom[0]][coordinatesFrom[1]]-=troopsDied;
        }
        if(numtroops>board[coordinatesAttack[0]][coordinatesAttack[1]]){
            System.out.println("You won!");
            if(isPlayer1){
                player2Land[coordinatesAttack[0]][coordinatesAttack[1]] = false;
                player1Land[coordinatesAttack[0]][coordinatesAttack[1]] = true;
                addChoicesForPlayer1(coordinatesAttack[0],coordinatesAttack[1]);
            }else{
                player2Land[coordinatesAttack[0]][coordinatesAttack[1]] = true;
                player1Land[coordinatesAttack[0]][coordinatesAttack[1]] = false;
                addChoicesForPlayer2(coordinatesAttack[0],coordinatesAttack[1]);
            }
        }else{
            System.out.println("You lost the battle!");
            board[coordinatesAttack[0]][coordinatesAttack[1]]-=numtroops;
            board[coordinatesFrom[0]][coordinatesFrom[1]] = 0;
        }
        if(isPlayer1){
            checkIfPlayerAlive(false);
        }else{
            checkIfPlayerAlive(true);
        }
    }
        static void dropNuclearBomb(boolean isPlayer1){
        System.out.println("The number of troops that you will have to sacrifice to make the bomb is going to be: 15");
        System.out.println("Which land would you like to create the nuclear bomb?");
        String boxToAttackFrom = checkAndValidateLand(false,false,isPlayer1);
        int[] boxAttackFrom = getIntegerCoordinates(boxToAttackFrom);
        if(board[boxAttackFrom[0]][boxAttackFrom[1]]>=15){
            board[boxAttackFrom[0]][boxAttackFrom[1]]-=15;
        }else{
            System.out.println("Please choose a land that has more than 15 troops");
            System.out.println("If you wish to not create a nuclear bomb, press '1' to go back");
            int choice = getInt.getInt();
            if(choice == 1){
                playerAttack(isPlayer1);
                return;
            }else {
                return;
            }
        }
        System.out.println("Where would you like to drop it?");
        String boxToAttack = checkAndValidateLand(true,false,isPlayer1);
        int[] boxAttack = getIntegerCoordinates(boxToAttack);
        if(boardSecrets[boxAttack[0]][boxAttack[1]] != 1){
            board[boxAttack[0]][boxAttack[1]] = -1;
            player2Land[boxAttack[0]][boxAttack[1]] = false;
            player1Land[boxAttack[0]][boxAttack[1]] = false;
            System.out.println("You have successfully detonated the whole land.");
            if(isPlayer1){
                checkIfPlayerAlive(false);
            }else{
                checkIfPlayerAlive(true);
            }
        }else{
            System.out.println("You triggered a secret!");
            System.out.println("Your nuclear bomb was shot down and killed 0 people");
            boardSecrets[boxAttack[0]][boxAttack[1]] = 0;
            containsSecret[boxAttack[0]][boxAttack[1]] = false;
        }
    }
    static void playerCreateSecret(boolean isPlayer1){
        System.out.println("One land can only contain 1 secret");
        System.out.println("Here are the choices for creating a secret: \n1.Antinuke \n2.AntiAir \n3.AntiLandInvasion \n4. BuffNextTurn");
        int choice = getInt.getInt();
        switch (choice) {
            case 1:
                System.out.println("The secret will be triggered when you land is bombed by a nuke.");
                System.out.println("The nuke will do no damage to your land");
                System.out.println("Be warned however that if the enemy takes over the land (such as means of air or land), they will get the buff instead if the secret is not triggered");
                System.out.println("The amount of troops you will sacrifice to create this secret will be 5");
                System.out.println("Are you sure to continue? Press 1 to continue and any number to go back");
                if(getInt.getInt() == 1){
                    System.out.println("Please select a land that you want to have the secret in");
                    String s = checkAndValidateLand(false,false,isPlayer1);
                    int[] choiceLand = getIntegerCoordinates(s);
                    if(containsSecret[choiceLand[0]][choiceLand[1]]){
                        playerCreateSecret(isPlayer1);
                    }else{
                        if(board[choiceLand[0]][choiceLand[1]] >= 5){
                            board[choiceLand[0]][choiceLand[1]]-=5;
                            containsSecret[choiceLand[0]][choiceLand[1]] = true;
                            boardSecrets[choiceLand[0]][choiceLand[1]] = 1;
                        }else{
                            System.out.println("Please select a land that has more than 5 troops");
                            playerCreateSecret(isPlayer1);
                            return;
                        }
                    }
                }else{
                    playerCreateSecret(isPlayer1);
                }
                break;
            case 2:
                System.out.println("The secret will be triggered when your land is attacked from air");
                System.out.println("When the secret is triggered, you will get an extra 10 troops to fight off the enemy");
                System.out.println("Be warned however that if the enemy takes over the land (such as means of land), they will get the buff instead if the secret is not triggered");
                System.out.println("The amount of troops you will sacrifice to create this secret will be 5");
                System.out.println("Are you sure to continue? Press 1 to continue and any number to go back");
                if(getInt.getInt() == 1){
                    System.out.println("Please select a land that you want to have the secret in");
                    String s = checkAndValidateLand(false,false,isPlayer1);
                    int[] choiceLand = getIntegerCoordinates(s);
                    if(containsSecret[choiceLand[0]][choiceLand[1]]){
                        playerCreateSecret(isPlayer1);
                    }else{
                        if(board[choiceLand[0]][choiceLand[1]] >= 5){
                            board[choiceLand[0]][choiceLand[1]]-=5;
                        }else{
                            System.out.println("Please select a land that has more than 5 troops");
                            playerCreateSecret(isPlayer1);
                            return;
                        }
                        containsSecret[choiceLand[0]][choiceLand[1]] = true;
                        boardSecrets[choiceLand[0]][choiceLand[1]] = 2;
                    }
                }else{
                    playerCreateSecret(isPlayer1);
                }
                break;
            case 3:
                System.out.println("The secret will be triggered when your land is attacked by an invasion");
                System.out.println("When you get attacked, it will add 10 troops to your army");
                System.out.println("Be warned however that if the enemy takes over the land (such as means of air), they will get the buff instead if the secret is not triggered");
                System.out.println("The amount of troops you will sacrifice to create this secret will be 5");
                System.out.println("Are you sure to continue? Press 1 to continue and any number to go back");
                if(getInt.getInt() == 1){
                    System.out.println("Please select a land that you want to have the secret in");
                    String s = checkAndValidateLand(false,false,isPlayer1);
                    int[] choiceLand = getIntegerCoordinates(s);
                    if(containsSecret[choiceLand[0]][choiceLand[1]]){
                        playerCreateSecret(isPlayer1);
                    }else{
                        if(board[choiceLand[0]][choiceLand[1]] >= 5){
                            board[choiceLand[0]][choiceLand[1]]-=5;
                            containsSecret[choiceLand[0]][choiceLand[1]] = true;
                            boardSecrets[choiceLand[0]][choiceLand[1]] = 3;
                        }else{
                            System.out.println("Please select a land that has more than 5 troops");
                            playerCreateSecret(isPlayer1);
                            return;
                        }
                    }
                }else{
                    playerCreateSecret(isPlayer1);
                }
                break;
            case 4:
                System.out.println("The secret will be triggered when player1's next turn starts");
                System.out.println("It will add 10 troops to your army");
                System.out.println("Be warned however that if the enemy takes over the land, they will get the buff instead");
                System.out.println("The amount of troops you will sacrifice to create this secret will be 5");
                System.out.println("Are you sure to continue? Press 1 to continue and any number to go back");
                if(getInt.getInt() == 1){
                    System.out.println("Please select a land that you want to have the secret in");
                    String s = checkAndValidateLand(false,false,isPlayer1);
                    int[] choiceLand = getIntegerCoordinates(s);
                    if(containsSecret[choiceLand[0]][choiceLand[1]]){
                        playerCreateSecret(isPlayer1);
                    }else{
                        if(board[choiceLand[0]][choiceLand[1]] >= 5){
                            board[choiceLand[0]][choiceLand[1]]-=5;
                            containsSecret[choiceLand[0]][choiceLand[1]] = true;
                            boardSecrets[choiceLand[0]][choiceLand[1]] = 4;
                        }else{
                            System.out.println("Please select a land that has more than 5 troops");
                            playerCreateSecret(isPlayer1);
                            return;
                        }
                    }
                }else{
                    playerCreateSecret(isPlayer1);
                }
                break;
            default:
                System.out.println("Please enter in a number between 1 and 4");
                playerCreateSecret(isPlayer1);
                break;
        }

        }
    static void playerBuild(boolean isPlayer1){
        System.out.println("What would you like to build? \n1. Food facility\n2. Turret");
//        System.out.println("3. Secret facility\n4. Nuclear bomb facility\n5. Training Camp");
        int choice = getInt.getInt();
        switch(choice) {
            case 1:
                System.out.println("A food facility boosts the number of troops added to your army");
                System.out.println("You will get to roll 2 dices at the end of your turn");
                System.out.println("Be warned however, that the facility can be taken over during an invasion.");
                System.out.println("The troops you will sacrifice to build a food facility is 8");
                System.out.println("Where would you like to build the food facility?");
                String chosenLand = checkAndValidateLand(false,false,isPlayer1);
                int[] chosenLandCoordinates = getIntegerCoordinates(chosenLand);
                if(board[chosenLandCoordinates[0]][chosenLandCoordinates[1]]>=8) {
                    board[chosenLandCoordinates[0]][chosenLandCoordinates[1]] -= 8;
                    boardFoodFacility[chosenLandCoordinates[0]][chosenLandCoordinates[1]] = 1;
                }else{
                    System.out.println("Please select a land with more than 8 troops");
                    playerBuild(isPlayer1);
                }
                break;
            case 2:
                System.out.println("A turret can take on troops invading the land.");
                System.out.println("It can kill 30% of invaders either from air or from land");
                System.out.println("The troops you will sacrifice to build a turret is 5");
                System.out.println("Where would you like to build the turret?");
                String chosenLand2 = checkAndValidateLand(false,false,isPlayer1);
                int[] chosenLand2Coordinates = getIntegerCoordinates(chosenLand2);
                board[chosenLand2Coordinates[0]][chosenLand2Coordinates[1]]-=5;
                containsTurret[chosenLand2Coordinates[0]][chosenLand2Coordinates[1]] = true;
                boardTurret[chosenLand2Coordinates[0]][chosenLand2Coordinates[1]] = 1;
                break;
//            case 3:
//                System.out.println("A secret facility provides you with secrets at the start of your turn");
//                System.out.println("A level 1 secret facility provides you with a secret every 2 turns");
//                System.out.println("A level 2 secret facility provides you with a secret every turn");
//                System.out.println("Be warned however, that the facility can be taken over during an invasion.");
//                System.out.println("The troops you will sacrifice to build a secret facility is 6");
//                System.out.println("Where would you like to build the secret facility?");
//                String chosenLand3 = checkAndValidateLand(false,false,isPlayer1);
//                int[] chosenLand3Coordinates = getIntegerCoordinates(chosenLand3);
//                board[chosenLand3Coordinates[0]][chosenLand3Coordinates[1]]-=6;
//                boardSecretFacility[chosenLand3Coordinates[0]][chosenLand3Coordinates[1]] = 1;
//                break;
//            case 4:
//                System.out.println("A nuclear bomb facility provides you with nuclear bombs at the start of your turn");
//                System.out.println("A level 1 nuclear bomb facility provides you with a nuclear bomb every 2 turns");
//                System.out.println("A level 2 nuclear bomb facility provides you with a nuclear bomb every turn");
//                System.out.println("Be warned however, that the facility can be taken over during an invasion.");
//                System.out.println("The troops you will sacrifice to build a nuclear facility is 18");
//                System.out.println("Where would you like to build the nuclear facility?");
//                String chosenLand4 = checkAndValidateLand(false,false,isPlayer1);
//                int[] chosenLand4Coordinates = getIntegerCoordinates(chosenLand4);
//                board[chosenLand4Coordinates[0]][chosenLand4Coordinates[1]]-=18;
//                boardNukeFacility[chosenLand4Coordinates[0]][chosenLand4Coordinates[1]] = 1;
//                break;
//            case 5:
//                System.out.println("A training camp boosts the soldiers durability and confidence");
//                System.out.println("A level 1 training camp will make the death rate of an air deploy decrease to 0");
//                System.out.println("A level 2 training camp will make your troops able to take on 4/3 of other troops with ease");
//                System.out.println("A level 3 training camp will make your troops able to take on 2 times of other troops with ease");
//                System.out.println("Be warned however, that the camp can be taken over during an invasion.");
//                System.out.println("The troops you will sacrifice to build a training camp is 5");
//                System.out.println("Where would you like to build the training camp?");
//                String chosenLand5 = checkAndValidateLand(false,false,isPlayer1);
//                int[] chosenLand5Coordinates = getIntegerCoordinates(chosenLand5);
//                board[chosenLand5Coordinates[0]][chosenLand5Coordinates[1]]-=5;
//                boardTrainingCamp[chosenLand5Coordinates[0]][chosenLand5Coordinates[1]] = 1;
//                break;
            default:
                System.out.println("Please enter in a number between 1 and 2");
                playerBuild(isPlayer1);
        }
    }
    static void printBoard() {
        for (int i = 0; i < length; i++) {
            System.out.print(" ------");
        }
        System.out.println();
        for (int j = 0; j < width; j++) {
            System.out.print("|");
            for (int i = 0; i < length; i++) {
                System.out.print(ABCboard[i][j] + "     |");
            }
            System.out.println();
            System.out.print("| ");
            for (int i = 0; i < length; i++) {
                if(board[i][j] == -1){
                    System.out.print("  @  | ");
                } else if (player1Land[i][j] == true) {
                    if (boardTurret[i][j] == 1) {
                        if (board[i][j] > 9) {
                            System.out.print(ANSI_BLUE + board[i][j] + "x-" + ANSI_RESET + " | ");
                        } else {
                            System.out.print(" " + ANSI_BLUE + board[i][j] + "x-" + ANSI_RESET + " | ");
                        }
                    } else if (boardFoodFacility[i][j] == 1) {
                        if (board[i][j] > 9) {
                            System.out.print("" + ANSI_BLUE + board[i][j] + "[]" + ANSI_RESET + " | ");
                        }else{
                            System.out.print(" " + ANSI_BLUE + board[i][j] + "[]" + ANSI_RESET + " | ");
                        }
                    } else if (board[i][j] > 9) {
                        System.out.print(" " + ANSI_BLUE + board[i][j] + ANSI_RESET + "  | ");
                    } else {
                        System.out.print("  " + ANSI_BLUE + board[i][j] + ANSI_RESET + "  | ");
                    }
                } else if (player2Land[i][j] == true) {
                    if (boardTurret[i][j] == 1) {
                        if (board[i][j] > 9) {
                            System.out.print(ANSI_RED + board[i][j]+"x-"+ ANSI_RESET + " | ");
                        } else {
                            System.out.print(" " + ANSI_RED + board[i][j]+"x-" + ANSI_RESET + " | ");
                        }
                    } else if (boardFoodFacility[i][j] == 1) {
                        if (board[i][j] > 9) {
                            System.out.print("" + ANSI_RED + board[i][j] + "[]" + ANSI_RESET + " | ");
                        }else{
                            System.out.print(" " + ANSI_RED + board[i][j] + "[]" + ANSI_RESET + " | ");
                        }
                    } else if (board[i][j] > 9) {
                        System.out.print(" " + ANSI_RED + board[i][j] + ANSI_RESET + "  | ");
                    } else {
                        System.out.print("  " + ANSI_RED + board[i][j] + ANSI_RESET + "  | ");
                    }
                } else {
                    if (board[i][j] > 9) {
                        System.out.print(" " + board[i][j] + "  | ");
                    } else {
                        System.out.print("  " + board[i][j] + "  | ");
                    }
                }
            }
            System.out.println();
            System.out.print("|");
            for (int i = 0; i < length; i++) {
                if(containsSecret[i][j]){
                    System.out.print("     ?|");
                }else {
                    System.out.print("      |");
                }
            }
            System.out.println();
            for (int i = 0; i < length; i++) {
                System.out.print(" ------");
            }
            System.out.println();
        }
    }
    static void addChoicesForPlayer1(int j, int i){
        if(j+1 < length){
            player1choices[j+1][i] = true;
        }
        if(j-1 >= 0){
            player1choices[j-1][i] = true;
        }
        if(i+1 < width){
            player1choices[j][i+1] = true;
        }
        if(i-1 >= 0){
            player1choices[j][i-1] = true;
        }
    }
    static void addChoicesForPlayer2(int j, int i){
        if(j+1 < length){
            player2choices[j+1][i] = true;
        }
        if(j-1 >= 0){
            player2choices[j-1][i] = true;
        }
        if(i+1 < width){
            player2choices[j][i+1] = true;
        }
        if(i-1 >= 0){
            player2choices[j][i-1] = true;
        }
    }
    static String checkAndValidateLand(boolean attack, boolean useland, boolean isPlayer1){
        System.out.println("Please enter in a letter");
        String choice = getLetter.getLetter(length*width);
        int[] coordinateChoice = getIntegerCoordinates(choice);
        int x = coordinateChoice[0];
        int y = coordinateChoice[1];
        if(board[x][y] == -1){
            System.out.println("Please attack a land that has not been detonated");
            return checkAndValidateLand(attack,useland,isPlayer1);
        }
        if(attack == true) {
            if(useland == true) {
                if (isPlayer1) {
                    if (player1Land[x][y] == true) {
                        System.out.println("Please enter a letter that's not your land");
                        return checkAndValidateLand(attack, useland, isPlayer1);
                    }
                    if (player1choices[x][y] == true) {
                        return getLetterCoordinates(coordinateChoice);
                    }
                } else {
                    if (player2Land[x][y] == true) {
                        System.out.println("Please enter a letter that's not your land");
                        return checkAndValidateLand(attack, useland, isPlayer1);
                    }
                    if (player2choices[x][y] == true) {
                        return getLetterCoordinates(coordinateChoice);
                    }
                }
            }else{
                if (isPlayer1) {
                    if (player1Land[x][y] == true) {
                        System.out.println("Please enter a letter that's not your land");
                        return checkAndValidateLand(attack, useland, isPlayer1);
                    }else{
                        return getLetterCoordinates(coordinateChoice);
                    }
                } else {
                    if (player2Land[x][y] == true) {
                        System.out.println("Please enter a letter that's not your land");
                        return checkAndValidateLand(attack, useland, isPlayer1);
                    }else{
                        return getLetterCoordinates(coordinateChoice);
                    }
                }
            }
        }else {
            if (isPlayer1) {
                if (player1Land[x][y] == true) {
                    return getLetterCoordinates(coordinateChoice);
                } else {
                    System.out.println("Please enter a letter that's your land");
                    return checkAndValidateLand(attack,useland,isPlayer1);
                }
            } else {
                if (player2Land[x][y] == true) {
                    return getLetterCoordinates(coordinateChoice);
                } else {
                    System.out.println("Please enter a letter that's your land");
                    return checkAndValidateLand(attack,useland,isPlayer1);
                }
            }
        }
        return null;
    }
    static int[] getIntegerCoordinates(String letter){
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(letter.charAt(0) == ABCboard[i][j]){
                    int[] coordinate = new int[2];
                    coordinate[0] = i;
                    coordinate[1] = j;
                    return coordinate;
                }
            }
        }
        return null;
    }
    static String getLetterCoordinates(int[] coordinates){
        int l = coordinates[0];
        int w = coordinates[1];
        char a = ABCboard[l][w];
        return a+"";
    }
    static void organizeChoices(boolean isPlayer1){
        if(isPlayer1 == true){
            player1choices = new boolean[length][width];
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if(player1Land[i][j] == true){
                        addChoicesForPlayer1(i,j);
                    }
                }
            }
        }else{
            player2choices = new boolean[length][width];
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if(player2Land[i][j] == true){
                        addChoicesForPlayer2(i,j);
                    }
                }
            }
        }
    }
    static void checkIfPlayerAlive(boolean isPlayer1){
        boolean playerAlive = false;
        if(isPlayer1){
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if(player1Land[i][j]==true){
                        playerAlive = true;
                    }
                }
            }
            if(playerAlive == false){
                player1dead = true;
            }
        }else{
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if(player2Land[i][j] == true){
                        playerAlive = true;
                    }
                }
            }
            if(playerAlive == false){
                player2dead = true;
            }
        }
    }
    static void pressEnterToRollAndEndTurn(boolean isPlayer1){
        Die d = new Die();
        System.out.println("Press enter to roll to end your turn.");
        PressToContinue.pressToContinue();
        if(isPlayer1) {
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if (player1Land[i][j] == true) {
                        if(boardFoodFacility[i][j] == 1) {
                            int newint = d.roll();
                            board[i][j]+=(newint*2);
                        }else{
                            board[i][j] += d.roll();
                        }
                    }
                }
            }
        }else{
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if (player2Land[i][j] == true) {
                        if(boardFoodFacility[i][j] == 1) {
                            int newint = d.roll();
                            board[i][j]+=(newint*2);
                        }else{
                            board[i][j] += d.roll();
                        }
                    }
                }
            }
        }
        printBoard();
    }
    static void pressEnterToContinue(){
        System.out.println("Press enter to continue");
        PressToContinue.pressToContinue();
    }
}
