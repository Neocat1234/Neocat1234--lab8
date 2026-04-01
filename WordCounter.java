import java.util.regex.*;
import java.io.*;
import java.util.Scanner;

public class WordCounter {



    //counts all words in the text, throws TooSmallText if fewer than 5 words, and InvalidStopwordException if the stopword is not found

    public static int processText(StringBuffer text, String stopword)
            throws InvalidStopwordException, TooSmallText {
        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");
        Matcher regexMatcher = regex.matcher(text);
        int count = 0;
        int totalCount = 0;
        boolean stopwordFound = false;
        boolean countingDone = false;

        while (regexMatcher.find()) { //counts using regex
            totalCount++;
            if (!countingDone) {
                count++;
                String word = regexMatcher.group();
                if (stopword != null && word.equals(stopword)) {
                    stopwordFound = true;
                    countingDone = true;
                }
            }
        }

        if (totalCount < 5) { //checks if text is too small
            throw new TooSmallText("Only found " + totalCount + " words.");
        }

        if (stopword != null && !stopwordFound) { //checks if stopword is missing
            throw new InvalidStopwordException("Couldn't find stopword: " + stopword);
        }

        return count;
    }
//converts file into a string buffer and if file is empty, throws EmptyFileException with message containing the file path
    public static StringBuffer processFile(String path) throws EmptyFileException {
        String filePath = path;

        while (true) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (sb.length() > 0) sb.append("\n");
                    sb.append(line);
                }

                if (sb.length() == 0) {
                    throw new EmptyFileException(filePath + " was empty");
                }

                return sb;

            } catch (EmptyFileException e) {
                throw e;
            } catch (IOException e) {
                System.out.print("error with our file path.");
                filePath = new Scanner(System.in).nextLine();
            }
        }
    }

    public static void main(String[] args) {
//take an option from the user
        Scanner inputScanner = new Scanner(System.in);
        int choice = 0;
        while (choice != 1 && choice != 2) {
            System.out.println("Coption 1 = file, option 2 = text. Choose your fate:");
            try {
                choice = Integer.parseInt(inputScanner.nextLine().trim());
                if (choice != 1 && choice != 2) {
                    System.out.println("1 or 2");
                }
            } catch (NumberFormatException e) {
                System.out.println("1 or 2");
            }
        }

        String stopword = (args.length >= 2) ? args[1] : null;

        //load content
        StringBuffer content = new StringBuffer();

        if (choice == 1) {
            try {
                content = processFile(args[0]);
            } catch (EmptyFileException e) {
                System.out.println(e);          
                content = new StringBuffer();   //will cause TooSmallText below
            }
        } else {
            content = new StringBuffer(args[0]);
        }

        //count words, handle exceptions
        try {
            int count = processText(content, stopword);
            System.out.println("Found " + count + " words.");
        } catch (TooSmallText e) {
            System.out.println(e);
        } catch (InvalidStopwordException e) {
            System.out.println(e);
            System.out.print(" enter a new stopword");
            String newStopword = inputScanner.nextLine();
            try {
                int count = processText(content, newStopword);
                System.out.println("Found " + count + " words.");
            } catch (InvalidStopwordException e2) {
                System.out.println(newStopword + " error stop word was not found in the text.");
            } catch (TooSmallText e2) {
                System.out.println(e2);
            }
        }
    }
}
