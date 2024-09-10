import java.util.Scanner;

public class MainMenu{

    public static void main(String[] args) {
        welcomeMessage();
    }

    public static void welcomeMessage(){
        System.out.println("Welcome to the Library Management System");
        displayMenu();
    }

    public static void displayMenu(){
        System.out.println("Chose an option below by entering the number 1, 2, 3 or 4");
        System.out.println("1 - Add A book");
        System.out.println("2 - Remove A Book");
        System.out.println("3 - View All Books ");
        System.out.println("4 - Exit Program");
        selection();
    }

    public static void selection(){
        int optionSelected = 0;
        Scanner menuSelection = new Scanner(System.in);
        try {
            optionSelected = Integer.parseInt(menuSelection.nextLine());

            if (optionSelected>4){
                System.out.println("Not a valid option, try again");
                selection();
            }
        }
        catch(NumberFormatException nfe){
            System.out.println("Only Numbers are accepted. Please try again.");
            selection();
        }

        switch (optionSelected){

            case 1:
                 RDBMS.addABook();
                 break;
            case 2:
                RDBMS.removeABook();
                break;
            case 3:
                RDBMS.listAllBooks();
                break;
            case 4:
                System.out.println("Thank you for using the Library Management System");
        }
    }
}