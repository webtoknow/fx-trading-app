package utilities.logger;


import java.util.Arrays;

class TacitusLogger {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[1;32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    TacitusLogger() {
    }

    static void depthLogger(String message) {
        try {
            System.out.println(ANSI_WHITE + TacitusHelper.getDateAndTime() + message + ANSI_RESET);
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }

    static void depthErrorLogger(String message, Exception ex) {
        try {
            System.out.println(ANSI_RED + TacitusHelper.getDateAndTime() + message + ANSI_RESET);

            if (ex != null) {
                System.out.println(ANSI_RED + TacitusHelper.getDateAndTime() + ex.getMessage() + ANSI_RESET);
                System.out.println(ANSI_RED + TacitusHelper.getDateAndTime() + ex.getStackTrace() + ANSI_RESET);
            }
        } catch (Exception innerEx) {
            System.out.println("ERROR: " + innerEx.getMessage());
        }
    }

    static void depthSuccessLogger(String message) {
        try {
            System.out.println(ANSI_GREEN + TacitusHelper.getDateAndTime() + message + ANSI_RESET);
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
}
