package mapsaroundyou.cli;

/**
 * Normalized search flags parsed from {@code search ...} argv tokens.
 *
 * @param destinationId destination identifier from the supported list
 * @param maxRent inclusive rent ceiling in SGD
 * @param maxCommuteMinutes inclusive commute upper bound in minutes
 * @param requireAircon whether air-conditioning is mandatory
 */
record SearchCommandArguments(
        String destinationId,
        int maxRent,
        int maxCommuteMinutes,
        boolean requireAircon
) {
}
