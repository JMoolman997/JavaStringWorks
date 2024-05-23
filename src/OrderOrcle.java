package src;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
class OrderOracle {

    public static void main(String[] args) {
        boolean gameOver = false;
        Scanner myScanner = new Scanner(System.in);
        while (!gameOver) {
            System.out.println("Generate/Sort/Search/Exit:");

            String selectedMode = myScanner.nextLine();

            if (selectedMode.equals("Generate")) {
                System.out.println("_Generate selected_");
                generateRandomNumbers(myScanner);
            } else if (selectedMode.equals("Sort")) {
                System.out.println("_Sort selected_");
                sortIntFromTextFile(myScanner);
            } else if (selectedMode.equals("Search")) {
                System.out.println("_Search selected_");
            } else if (selectedMode.equals("Exit")) {
                myScanner.close();
                System.exit(0);
            } else {
                System.out.println("Error - Invalid operation selected");
            }
        }
    }

    public static void generateRandomNumbers(Scanner scanner) {
        System.out.println("Enter number of elements to generate:");

        if (scanner.hasNextInt()) {
            int arrayLength = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (arrayLength <= 0) {
                throw new IllegalArgumentException("Length of the array must be greater than 0.");
            }

            Comparable[] randomArray = new Comparable[arrayLength];
            Random random = new Random();

            for (int i = 0; i < arrayLength; i++) {
                // Assuming Comparable type is Integer, adjust as needed
                randomArray[i] = random.nextInt();
            }

            System.out.println("Enter filename:");
            String filename = scanner.nextLine().trim();

            String outputPath = "resources/" + filename;

            writeArrIntegersToFile(randomArray, outputPath);

            System.out.println("Random numbers generated and saved to file successfully.");
        } else {
            System.out.println("Invalid input. Please enter a valid integer.");
        }
    }


    public static void sortIntFromTextFile(Scanner scanner) {
        System.out.println("Enter filename to sort:");
        String filename = scanner.nextLine().trim();

        Comparable[] arrUnsortedIntegers = readIntTextFile("resources/" + filename);

        
        String outputPath = "resources/" + filename;

        writeArrIntegersToFile(arrUnsortedIntegers, outputPath);
    }

    private static void writeArrIntegersToFile(Comparable[] arrIntegers, String filePath) {
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("File created successfully: " + filePath);
                } else {
                    System.out.println("Error creating the file: " + filePath);
                    return;
                }
            }

            try (BufferedWriter myWriter = new BufferedWriter(new FileWriter(file))) {
                for (Comparable sortedInt : arrIntegers) {
                    myWriter.write(String.valueOf(sortedInt));
                    myWriter.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing to file");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Error checking file existence");
            e.printStackTrace();
        }
    }

    public static Comparable[] readIntTextFile(String filename) {
        ArrayList<Comparable<Integer>> arrNumbers = new ArrayList<>();

        try (Scanner fileReader = new Scanner(new File(filename))) {
            while (fileReader.hasNextInt()) {
                Integer number = fileReader.nextInt();
                arrNumbers.add(number);  // Integer implements Comparable<Integer>
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error - File not found: " + filename);
            e.printStackTrace();
        }

        return arrNumbers.toArray(new Comparable[0]);
    }

    public static void searchIntTextFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter filename to search:");
        String filename = scanner.nextLine().trim();
        scanner.close();
    }

    private static int[] convertArrayListToArray(ArrayList<Integer> arrayList) {
        int[] result = new int[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            result[i] = arrayList.get(i);
        }
        return result;
    }
}
