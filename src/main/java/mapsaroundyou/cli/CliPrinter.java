package mapsaroundyou.cli;

import mapsaroundyou.model.CommuteEstimate;
import mapsaroundyou.model.DatasetMetadata;
import mapsaroundyou.model.Destination;
import mapsaroundyou.model.SearchResult;

import java.util.List;

/**
 * Handles terminal output formatting.
 */
public class CliPrinter {
    public void printBanner(DatasetMetadata datasetMetadata) {
        System.out.println("MapsAroundYou CLI");
        System.out.println("Offline smart rental search scaffold");
        System.out.println("Data accurate as of " + datasetMetadata.lastUpdated()
                + " | " + datasetMetadata.sourceDescription());
        System.out.println();
    }

    public void printHelp() {
        String jarPlaceholder = "<path-to-MapsAroundYou-<version>-all.jar>";

        System.out.println("Usage:");
        System.out.println("  java -cp " + jarPlaceholder + " mapsaroundyou.cli.MapsAroundYouApp");
        System.out.println(
                "  java -cp " + jarPlaceholder + " mapsaroundyou.cli.MapsAroundYouApp "
                        + "search --destination D01 --max-rent 2200 --max-commute 45 --max-transfers 1 "
                        + "--max-walk 10 --result-limit 5 "
                        + "--sort balanced --require-aircon --exclude-walk-dominant"
        );
        System.out.println("  (Replace the placeholder with the path to the runnable fat JAR.)");
        System.out.println();
        System.out.println("Commands:");
        System.out.println(
                "  search --destination <ID> --max-rent <SGD> --max-commute <minutes> "
                        + "[--max-transfers <count>] "
                        + "[--max-walk <minutes>] [--result-limit <count>] "
                        + "[--sort <commute|rent|balanced>] [--require-aircon] "
                        + "[--exclude-walk-dominant]"
        );
        System.out.println("  help");
        System.out.println();
        System.out.println("Examples:");
        System.out.println(
                "  java -cp " + jarPlaceholder + " mapsaroundyou.cli.MapsAroundYouApp "
                        + "search --destination D05 --max-rent 1800 --max-commute 35 "
                        + "--max-transfers 1 --max-walk 10 --result-limit 10 --sort commute"
        );
        System.out.println("  (Use a fully qualified path if the JAR is not in the current directory.)");
        System.out.println();
    }

    public void printDestinations(List<Destination> destinations) {
        System.out.println("Supported destinations:");
        for (Destination destination : destinations) {
            System.out.printf("  %-4s %-55s (%s)%n",
                    destination.destinationId(),
                    destination.name(),
                    destination.category());
        }
        System.out.println();
    }

    public void printInteractiveInstructions() {
        System.out.println("Interactive mode");
        System.out.println("Type 'exit' at the destination prompt to quit.");
        System.out.println("Press Enter to keep the value shown in brackets.");
        System.out.println("You can leave max transfers high to avoid filtering by interchange count.");
        System.out.println();
    }

    public void printResults(List<SearchResult> results) {
        System.out.println("Top matches:");
        int rank = 1;
        for (SearchResult result : results) {
            CommuteEstimate commute = result.commute();
            System.out.printf(
                    "%d. %s [%s]%n",
                    rank++,
                    result.listing().title(),
                    result.listing().listingId()
            );
            System.out.printf(
                    "   Rent: SGD %d | Commute: %d min (%d transit / %d walk) | Aircon: %s%n",
                    result.listing().monthlyRent(),
                    commute.totalMinutes(),
                    commute.transitMinutes(),
                    commute.walkMinutes(),
                    result.listing().hasAircon() ? "Yes" : "No"
            );
            System.out.printf(
                    "   Address: %s | Type: %s | Score: %.2f%n",
                    result.listing().address(),
                    result.listing().roomType(),
                    result.score()
            );
        }
        System.out.println();
    }

    public void printNoResults(String message) {
        System.out.println(message);
        System.out.println();
    }

    public void printError(String message) {
        System.err.println("Error: " + message);
    }

    public void printGoodbye() {
        System.out.println("Exiting MapsAroundYou CLI.");
    }
}
