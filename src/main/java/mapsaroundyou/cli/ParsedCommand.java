package mapsaroundyou.cli;

/**
 * Parsed CLI argv intent produced by {@link CliCommandParser}.
 *
 * @param commandType high-level mode (help, interactive, or structured search)
 * @param searchArguments populated only for {@link CommandType#SEARCH}
 */
record ParsedCommand(CommandType commandType, SearchCommandArguments searchArguments) {
    enum CommandType {
        HELP,
        INTERACTIVE,
        SEARCH
    }
}
