package Adaboost;

public class ConsoleLogger implements Logger {

    public void log(String message) {
        System.out.println(message);
    }

    public void logImportant(String message) {
        System.out.println(message);
    }
}
