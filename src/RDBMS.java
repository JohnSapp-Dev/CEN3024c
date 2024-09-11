import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class RDBMS {

    public static void addABook(){
        try {
            Scanner SQLLogin = new Scanner(System.in);
            System.out.println("Enter the username of your database");
            String SQLUser = SQLLogin.nextLine();
            System.out.println("Enter the password for your database");
            String SQLPassword = SQLLogin.nextLine();
            System.out.println("Enter the name of your database");
            String SQLDatabase = SQLLogin.nextLine();
            System.out.println("Enter the name of your table");
            String SQLTable = SQLLogin.nextLine();
            Connection SQLServer;

            String SQLUrl = "jdbc:mysql://localhost:3306/" + SQLDatabase; // sql server url

            Scanner filePath = new Scanner(System.in); // accepts input
            String filePathName;
            System.out.println("Enter the Absolute path to your .txt file you want to add");
            filePathName = filePath.nextLine();

            Class.forName("com.mysql.cj.jdbc.Driver");
            SQLServer = DriverManager.getConnection(SQLUrl,SQLUser,SQLPassword);

            String SQLCommand = "INSERT INTO "+ SQLTable +" (Book_id, Book_Title, Book_Author)" +
                    " VALUES (?,?,?)"; //template for the preparedStatement object
            File SQLFile = new File(filePathName); //opens in .txt file
            Scanner SQLFileValues = new Scanner(SQLFile); //reads in .txt file
            PreparedStatement SQLStatement = SQLServer.prepareStatement(SQLCommand);

            String line;
            String[] linesplit;
            SQLFileValues.nextLine(); // skips title line

            /* This while loop reads in the file line by line using the \n (newline)
             *  to denote the next entry*/
            while (SQLFileValues.hasNext()){

                line = SQLFileValues.next();
                linesplit = line.split(",");

                for (String x : linesplit){
                    System.out.println(x);
                }
                SQLStatement.setInt(1,Integer.parseInt(linesplit[0]));
                SQLStatement.setString(2,linesplit[1]);
                SQLStatement.setString(3,linesplit[2]);
                SQLStatement.executeUpdate();

            }
            SQLServer.close();
            MainMenu.displayMenu();

        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
            System.out.print("File not found.\n");
            MainMenu.displayMenu();
        }catch (ClassNotFoundException e) {
            System.out.print("Error, database driver not found\n");
            MainMenu.displayMenu();
           // throw new RuntimeException(e);
        }catch (SQLException e){
            System.out.print("Error, SQL database not found\n");
            MainMenu.displayMenu();
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("File format error. All spaces should be replaced with underscores '_'");
            MainMenu.displayMenu();
        }
    }

    public static void removeABook(){
        int bookID = 0;
        String bookTitle = " ";
        String bookAuthor=" ";

        try{
            Scanner SQLLogin = new Scanner(System.in);
            System.out.println("Enter the username of your database");
            String SQLUser = SQLLogin.nextLine();
            System.out.println("Enter the password for your database");
            String SQLPassword = SQLLogin.nextLine();
            System.out.println("Enter the name of your database");
            String SQLDatabase = SQLLogin.nextLine();
            System.out.println("Enter the name of your table");
            String SQLTable = SQLLogin.nextLine();

            Connection SQLServer;
            PreparedStatement SQLStatementRetrieve;
            PreparedStatement SQLStatementCount;

            String SQLUrl = "jdbc:mysql://localhost:3306/"+SQLDatabase; // sql server url

            System.out.println("Enter the Book ID to be removed");
            Scanner userInput = new Scanner(System.in); // accepts input
            String removeEntry = userInput.nextLine();



            ResultSet rowInfo; // gets row info
            ResultSet CountBD; // gets count before the delete action
            ResultSet CountAD; // gets count after the delete action
            int countBD = 0;
            int countAD = 0;
            Class.forName("com.mysql.cj.jdbc.Driver");
            SQLServer = DriverManager.getConnection(SQLUrl,SQLUser,SQLPassword);
            String SQLCommandDelete = "DELETE FROM lms WHERE Book_id = ?"; //template for the preparedStatement object
            String SQLCommandRetrieve = "SELECT Book_id,Book_title,Book_author FROM "+SQLTable+" WHERE Book_id = ?";

            String SQLSize = "Select COUNT(*) FROM "+SQLTable;

            // counts all rows in the database before the delete query
            SQLStatementCount = SQLServer.prepareStatement(SQLSize);
            CountBD =  SQLStatementCount.executeQuery();

            while (CountBD.next()){
                countBD = CountBD.getInt("COUNT(*)");
            }

            // gets row information
            SQLStatementRetrieve = SQLServer.prepareStatement(SQLCommandRetrieve);
            SQLStatementRetrieve.setInt(1,Integer.parseInt(removeEntry));
            rowInfo = SQLStatementRetrieve.executeQuery();

            // deletes row
            PreparedStatement SQLStatementDelete = SQLServer.prepareStatement(SQLCommandDelete);
            SQLStatementDelete.setInt(1,Integer.parseInt(removeEntry));
            SQLStatementDelete.executeUpdate();

            // counts all rows in the database After the delete query
            SQLStatementCount = SQLServer.prepareStatement(SQLSize);
            CountAD =  SQLStatementCount.executeQuery();

            while (CountAD.next()) {
                countAD = CountAD.getInt("COUNT(*)");
            }

            int difference = countBD - countAD;

            if (difference>0){
                while(rowInfo.next()){
                    bookID = rowInfo.getInt("Book_id");
                    bookTitle = rowInfo.getString("Book_Title");
                    bookAuthor = rowInfo.getString("Book_Author");

                }
                System.out.println("Removing " + bookID+ " "+bookTitle+" "+bookAuthor);
                System.out.println("The book requested has been removed");

                MainMenu.displayMenu();

            }
            else{
                System.out.println("The book requested is not in the database");
                System.out.println("Returning to main menu");
                MainMenu.displayMenu();
            }

            SQLServer.close();

        }catch (ClassNotFoundException e) {
            System.out.print("Error, database driver not found\n");
            MainMenu.displayMenu();
            // throw new RuntimeException(e);
        }catch (SQLException e){
            System.out.print("Error, SQL database not found\n");
            MainMenu.displayMenu();
        }

    }

    public static void listAllBooks(){
        try{
            Scanner SQLLogin = new Scanner(System.in);
            System.out.println("Enter the username of your database");
            String SQLUser = SQLLogin.nextLine();
            System.out.println("Enter the password for your database");
            String SQLPassword = SQLLogin.nextLine();
            System.out.println("Enter the name of your database");
            String SQLDatabase = SQLLogin.nextLine();
            System.out.println("Enter the name of your table");
            String SQLTable = SQLLogin.nextLine();

            Connection SQLServer;

            String SQLUrl = "jdbc:mysql://localhost:3306/"+SQLDatabase; // sql server url
            ResultSet SQLRowValues;
            PreparedStatement SQLStatementRow;

            int bookID;
            String bookTitle;
            String bookAuthor;

            Class.forName("com.mysql.cj.jdbc.Driver");
            SQLServer = DriverManager.getConnection(SQLUrl,SQLUser,SQLPassword);
            String SQLSelectAll = "SELECT * FROM " +SQLTable;

            SQLStatementRow = SQLServer.prepareStatement(SQLSelectAll);
            SQLRowValues = SQLStatementRow.executeQuery();

            // This while loop reads in the SQL rows
            while (SQLRowValues.next()){

                bookID = SQLRowValues.getInt("Book_id");
                bookTitle = SQLRowValues.getString("Book_Title");
                bookAuthor = SQLRowValues.getString("Book_Author");

                System.out.println("book ID: "+bookID );
                System.out.println("Title:   " + bookTitle );
                System.out.println("Author:  "+ bookAuthor +"\n");
            }
            SQLServer.close();
            MainMenu.displayMenu();

        }catch (ClassNotFoundException e) {
            System.out.print("Error, database driver not found\n");
            MainMenu.displayMenu();
            // throw new RuntimeException(e);
        }catch (SQLException e){
            System.out.print("Error, SQL database not found\n");
            MainMenu.displayMenu();
        }
    }
}