import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static String[][] morningHall;
    static String[][] afternoonHall;
    static String[][] nightHall;
    static Scanner scanner = new Scanner(System.in);
    static String[] bookingHistory = new String[20];
    static String hallOption;

    public static void main(String[] args) {
        displayMenu();
        boolean isTrue = true;

        do {
            System.out.println(" [[ Application Menu ]]");
            System.out.println("<A> Booking");
            System.out.println("<B> Hall");
            System.out.println("<C> Showtime");
            System.out.println("<D> Reboot Showtime");
            System.out.println("<E> History");
            System.out.println("<F> Exit");

            String option = readValidOption();
            switch (option) {
                case "a" -> booking();
                case "b" -> displayAllHall();
                case "c" -> showTime();
                case "d" -> rebootShowTime();
                case "e" -> history(bookingHistory, hallOption);
                case "f" -> {
                    isTrue = false;
                    System.exit(0);
                }
                default -> System.out.println("Sorry, Invalid Option!!!");
            }
        } while (isTrue);

        scanner.close();
    }

    private static void displayMenu() {
        System.out.print("-+".repeat(20) + "\n");
        System.out.println("CSTAD HALL BOOKING SYSTEM");
        System.out.print("-+".repeat(20) + "\n");
        System.out.print("> Config total rows in hall : ");
        int storeRowData = readPositiveIntegerInput();
        System.out.print("> Config total seats per row in hall : ");
        int storeSeatData = readPositiveIntegerInput();
        initializeHalls(storeRowData, storeSeatData);
    }

    private static void initializeHalls(int storeRowData, int storeSeatData) {
        morningHall = new String[storeRowData][storeSeatData];
        afternoonHall = new String[storeRowData][storeSeatData];
        nightHall = new String[storeRowData][storeSeatData];

        for (int i = 0; i < storeRowData; i++) {
            for (int j = 0; j < storeSeatData; j++) {
                char c = (char) ('A' + i);
                morningHall[i][j] = "|" + c + "-" + (j + 1) + "::AV|";
                afternoonHall[i][j] = "|" + c + "-" + (j + 1) + "::AV|";
                nightHall[i][j] = "|" + c + "-" + (j + 1) + "::AV|";
            }
        }
    }

    private static void booking() {
        String option;
        System.out.println("+-".repeat(25));
        System.out.println("# START BOOKING PROCESS ");
        System.out.println("+-".repeat(25));
        System.out.print(" >> PLEASE SELECT SHOW TIME ( A | B | C ) : ");

        do {
            option = scanner.nextLine().toLowerCase();
            hallOption = option.toUpperCase();
            Pattern pattern2 = Pattern.compile("[a-c]");
            if (pattern2.matcher(option).matches()) {
                showTime();
                switch (option) {
                    case "a" -> bookForHall(morningHall, hallOption);
                    case "b" -> bookForHall(afternoonHall, hallOption);
                    case "c" -> bookForHall(nightHall, hallOption);
                }
            }
        } while (!option.equals("e"));
    }

    private static void bookForHall(String[][] hall, String hallOption) {
        displayHall(hall);
        System.out.println("# INSTRUCTION \n# SINGLE : C-1 \n# MULTIPLE ( SEPARATE BY COMMA ) : C-1 , C-2");
        System.out.print(">> PLEASE SELECT AVAILABLE SEAT : ");

        String row = scanner.nextLine().toUpperCase().replaceAll("\\s", "");
        String[] booking = row.split(",");
        System.out.print(">> PLEASE ENTER STUDENT ID : ");
        String stuId = scanner.nextLine();

        for (String eachBooking : booking) {
            Pattern pattern = Pattern.compile("^[A-Za-z]-(?!0)\\d+$");

            if (pattern.matcher(eachBooking).matches()) {
                String[] parts = eachBooking.split("-");

                if (parts.length == 2) {
                    int r = parts[0].charAt(0) - 'A';
                    int c = Integer.parseInt(parts[1]) - 1;

                    if (isValidSeat(r, c, hall)) {
                        char l = (char) ('A' + r);
                        hall[r][c] = "|" + l + "-" + (c + 1) + "::BO|";
                        System.out.println("+-".repeat(25));
                        System.out.println("Seat " + "[" + eachBooking + "]" + " successfully booked!");
                        String[] bookingDetails = {eachBooking, hall[r][c], stuId};
                        history(bookingDetails, hallOption);

                    } else {
                        System.out.println("+-".repeat(25));
                        System.out.println("Seat " + "[" + eachBooking + "]" + " is not available.");
                    }
                    System.out.println("+-".repeat(25));
                    System.out.print("Enter [e] to exit : ");
                }
            } else {
                System.out.println(" >> ERROR !! INVALID FORMAT !");
            }
        }
    }

    private static void displayAllHall() {
        System.out.println("+-".repeat(25));
        System.out.println("#HALL A  - Morning");
        System.out.println("+-".repeat(25));
        displayHall(morningHall);
        System.out.println("+-".repeat(25));
        System.out.println("#HALL B  - Afternoon");
        System.out.println("+-".repeat(25));
        displayHall(afternoonHall);
        System.out.println("+-".repeat(25));
        System.out.println("#HALL C  - Night");
        System.out.println("+-".repeat(25));
        displayHall(nightHall);
    }

    private static void displayHall(String[][] hall) {
        for (String[] row : hall) {
            for (String seat : row) {
                System.out.print(seat + "\t");
            }
            System.out.println();
        }
    }

    private static void showTime() {
        System.out.print("-+".repeat(25) + "\n");
        String[] showTime = {"A) Morning (10:00AM - 12:30PM)", "B) Afternoon (3:00PM - 5:30PM)", "C) Night (7:00PM - 9:30PM)"};
        for (String i : showTime) {
            System.out.println("# " + i);
        }
        System.out.print("-+".repeat(25) + "\n");
    }

    private static void rebootShowTime() {
        System.out.println("+-".repeat(25));
        System.out.println("Start Rebooting The Hall...");
        resetHalls();
        System.out.println("Rebooted Successfully!!");
        System.out.println("+-".repeat(25));
    }

    private static void resetHalls() {
        initializeHalls(morningHall.length, morningHall[0].length);
    }

    // Modify the history method to call displayBookingHistory
    private static void history(String[] bookingDetails, String hallOption) {
        System.out.println("-+".repeat(25));
        System.out.println("Booking History:");
        System.out.println("-+".repeat(25));
        int count = 0;

        for (String bookingEntry : bookingHistory) {
            if (bookingEntry == null) {
                LocalDateTime showTime = LocalDateTime.now();
                System.out.println("#NO: " + (count + 1));
                System.out.println("#SEATS: [" + Arrays.toString(bookingDetails) + "]");
                System.out.println("#HALL               #STU.ID         #CREATED AT");
                System.out.println("HALL " + hallOption + "               " + bookingDetails[2] + "       " + showTime);

                // Only store the history, don't display it here

                recordHistory(bookingDetails);
                count++;

                // Display booking history immediately after recording
                displayBookingHistory();
                break;
            }
        }

        if (count == 0) {
            System.out.println("-+".repeat(25));
            System.out.println("No booking history available.");
            System.out.println("-+".repeat(25));
        }
    }

    private static void recordHistory(String[] entry) {
        for (int i = bookingHistory.length - 1; i > 0; i--) {
            bookingHistory[i] = bookingHistory[i - 1];
        }
        bookingHistory[0] = String.format(
                String.valueOf(1), Arrays.toString(entry), entry[2], entry[1], LocalDateTime.now());
    }


    // Add a method to display booking history
    private static void displayBookingHistory() {
        for (String bookingEntry : bookingHistory) {
            if (bookingEntry != null) {
                System.out.println(bookingEntry);
            }
        }
    }


    private static String readValidOption() {
        String optionSelect = "^[a-fA-F]$";
        Pattern patternOption = Pattern.compile(optionSelect);
        String option;

        do {
            System.out.print("> Please select menu no: ");
            option = scanner.next().toLowerCase();

            if (!isValidInput(option, patternOption)) {
                System.out.println("Invalid input. Please enter a valid option.");
            }
        } while (!isValidInput(option, patternOption));

        return option;
    }

    private static int readPositiveIntegerInput() {
        Pattern pattern = Pattern.compile("^[1-9]\\d*$");
        int input;

        do {
            while (!scanner.hasNextInt()) {
                System.err.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
            input = scanner.nextInt();

            if (input <= 0) {
                System.err.println("Invalid input. Please enter a positive integer.");
            }
        } while (input <= 0);

        return input;
    }

    private static boolean isValidInput(String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private static boolean isValidSeat(int row, int col, String[][] hall) {
        return row >= 0 && row < hall.length && col >= 0 && col < hall[0].length && hall[row][col].endsWith("AV|");
    }
}
