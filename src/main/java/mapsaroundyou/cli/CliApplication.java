package mapsaroundyou.cli;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import mapsaroundyou.common.InvalidInputException;
import mapsaroundyou.common.MapsAroundYouException;
import mapsaroundyou.common.NoResultsException;
import mapsaroundyou.logic.SearchLogic;
import mapsaroundyou.model.Destination;
import mapsaroundyou.model.SortMode;
import mapsaroundyou.model.TransportMode;
import mapsaroundyou.model.UserPreferences;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Orchestrates CLI execution for interactive and flag-driven modes.
 */
public final class CliApplication {
    private static final Logger LOGGER = Logger.getLogger(CliApplication.class.getName());

    private final SearchLogic searchLogic;
    private final CliCommandParser commandParser;
    private final CliPrinter cliPrinter;

    /**
     * Creates a CLI runner with injected collaborators (used by tests and {@link MapsAroundYouApp}).
     *
     * @param searchLogic domain workflow
     * @param commandParser argv parser
     * @param cliPrinter stdout/stderr writer
     */
    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification = "CliApplication composes shared services and does not expose mutable collaborator state."
    )
    public CliApplication(SearchLogic searchLogic, CliCommandParser commandParser, CliPrinter cliPrinter) {
        this.searchLogic = searchLogic;
        this.commandParser = commandParser;
        this.cliPrinter = cliPrinter;
    }

    /**
     * Parses arguments, dispatches to help/interactive/search flows, and returns a process exit
     * code.
     *
     * @param args raw CLI arguments
     * @return {@code 0} on success, {@code 1} on user error or unexpected failure
     */
    public int run(String[] args) {
        try {
            cliPrinter.printBanner(searchLogic.getDatasetMetadata());
            ParsedCommand parsedCommand = commandParser.parse(args);
            return switch (parsedCommand.commandType()) {
            case HELP -> runHelp();
            case INTERACTIVE -> runInteractive();
            case SEARCH -> runSearch(parsedCommand.searchArguments());
            };
        } catch (NoResultsException exception) {
            cliPrinter.printNoResults(exception.getMessage());
            return 0;
        } catch (NoSuchElementException exception) {
            cliPrinter.printError("Interactive mode ended before all inputs were provided.");
            cliPrinter.printHelp();
            return 1;
        } catch (MapsAroundYouException exception) {
            cliPrinter.printError(exception.getMessage());
            cliPrinter.printHelp();
            return 1;
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unexpected error in CLI", exception);
            cliPrinter.printError("Unexpected error: " + exception.getMessage());
            cliPrinter.printHelp();
            return 1;
        }
    }

    /**
     * Prints usage information and returns success.
     */
    private int runHelp() {
        cliPrinter.printHelp();
        return 0;
    }

    /**
     * Prompts for destination and filters in a loop until the user exits.
     *
     * @return process exit code ({@code 0} on orderly exit)
     */
    private int runInteractive() {
        List<Destination> destinations = searchLogic.getSupportedDestinations();
        cliPrinter.printDestinations(destinations);
        cliPrinter.printInteractiveInstructions();

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                UserPreferences currentPreferences = searchLogic.getCurrentPreferences();
                String destinationId = promptWithDefault(scanner, "Destination ID", currentPreferences.destinationId());
                if (shouldExit(destinationId)) {
                    cliPrinter.printGoodbye();
                    return 0;
                }

                try {
                    UserPreferences preferences = new UserPreferences(
                            destinationId,
                            promptIntegerWithDefault(scanner, "Max rent (SGD)", currentPreferences.maxRent(), 0),
                            promptIntegerWithDefault(
                                    scanner,
                                    "Max commute (minutes)",
                                    currentPreferences.maxCommuteMinutes(),
                                    1
                            ),
                            promptIntegerWithDefault(
                                    scanner,
                                    "Max transfers",
                                    currentPreferences.maxTransfers(),
                                    0
                            ),
                            promptIntegerWithDefault(
                                    scanner,
                                    "Max walking time (minutes)",
                                    currentPreferences.maxWalkMinutes(),
                                    0
                            ),
                            promptYesNoWithDefault(
                                    scanner,
                                    "Require aircon?",
                                    currentPreferences.requireAircon()
                            ),
                            TransportMode.PUBLIC_TRANSPORT,
                            promptIntegerWithDefault(
                                    scanner,
                                    "Result limit",
                                    currentPreferences.resultLimit(),
                                    1
                            ),
                            promptSortModeWithDefault(scanner, currentPreferences.sortMode()),
                            promptYesNoWithDefault(
                                    scanner,
                                    "Exclude walk-dominant routes?",
                                    currentPreferences.excludeWalkDominantRoutes()
                            )
                    );
                    runSearch(preferences);
                } catch (NoResultsException exception) {
                    cliPrinter.printNoResults(exception.getMessage());
                } catch (MapsAroundYouException exception) {
                    cliPrinter.printError(exception.getMessage());
                } catch (Exception exception) {
                    LOGGER.log(Level.SEVERE, "Unexpected error in interactive search", exception);
                    cliPrinter.printError("Unexpected error: " + exception.getMessage());
                }

                String nextAction = prompt(scanner, "Press Enter to search again or type exit");
                if (shouldExit(nextAction)) {
                    cliPrinter.printGoodbye();
                    return 0;
                }
                System.out.println();
            }
        }
    }

    private int runSearch(SearchCommandArguments arguments) {
        UserPreferences currentPreferences = searchLogic.getCurrentPreferences();
        UserPreferences preferences = new UserPreferences(
                arguments.destinationId(),
                arguments.maxRent(),
                arguments.maxCommuteMinutes(),
                arguments.maxTransfers() == null
                        ? currentPreferences.maxTransfers()
                        : arguments.maxTransfers(),
                arguments.maxWalkMinutes() == null
                        ? currentPreferences.maxWalkMinutes()
                        : arguments.maxWalkMinutes(),
                arguments.requireAircon(),
                TransportMode.PUBLIC_TRANSPORT,
                arguments.resultLimit() == null ? currentPreferences.resultLimit() : arguments.resultLimit(),
                arguments.sortMode() == null ? currentPreferences.sortMode() : arguments.sortMode(),
                arguments.excludeWalkDominantRoutes()
        );
        return runSearch(preferences);
    }

    private int runSearch(UserPreferences preferences) {
        searchLogic.updatePreferences(preferences);
        cliPrinter.printResults(searchLogic.generateShortlist());
        return 0;
    }

    private static String prompt(Scanner scanner, String label) {
        System.out.print(label + ": ");
        return scanner.nextLine().trim();
    }

    private static int parseInteger(String rawValue, String label) {
        try {
            return Integer.parseInt(rawValue);
        } catch (NumberFormatException exception) {
            throw new InvalidInputException(label + " must be a valid integer.", exception);
        }
    }

    private static String promptWithDefault(Scanner scanner, String label, String defaultValue) {
        String promptLabel = defaultValue == null || defaultValue.isBlank()
                ? label
                : label + " [" + defaultValue + "]";
        String rawValue = prompt(scanner, promptLabel);
        if (rawValue.isBlank()) {
            if (defaultValue == null || defaultValue.isBlank()) {
                throw new InvalidInputException(label + " is required.");
            }
            return defaultValue;
        }
        return rawValue;
    }

    private static int promptIntegerWithDefault(Scanner scanner, String label, int defaultValue, int minimumValue) {
        int normalizedDefaultValue = Math.max(defaultValue, minimumValue);
        int value = parseInteger(
                promptWithDefault(scanner, label, Integer.toString(normalizedDefaultValue)),
                label
        );
        if (value < minimumValue) {
            throw new InvalidInputException(label + " must be at least " + minimumValue + ".");
        }
        return value;
    }

    private static boolean promptYesNoWithDefault(Scanner scanner, String label, boolean defaultValue) {
        String defaultToken = defaultValue ? "y" : "n";
        return parseYesNo(promptWithDefault(scanner, label + " [y/n]", defaultToken), label);
    }

    private static SortMode promptSortModeWithDefault(Scanner scanner, SortMode defaultValue) {
        try {
            return SortMode.fromCliValue(promptWithDefault(
                    scanner,
                    "Sort mode (commute/rent/balanced)",
                    defaultValue.cliValue()
            ));
        } catch (IllegalArgumentException exception) {
            throw new InvalidInputException(exception.getMessage());
        }
    }

    private static boolean parseYesNo(String rawValue, String label) {
        if (rawValue.isBlank() || rawValue.equalsIgnoreCase("n") || rawValue.equalsIgnoreCase("no")) {
            return false;
        }
        if (rawValue.equalsIgnoreCase("y") || rawValue.equalsIgnoreCase("yes")) {
            return true;
        }
        throw new InvalidInputException("Please answer y/yes or n/no for " + label + ".");
    }

    private static boolean shouldExit(String rawValue) {
        return rawValue.equalsIgnoreCase("exit") || rawValue.equalsIgnoreCase("quit");
    }
}
