package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddEventCommand object.
 */
public class AddEventCommandParser implements Parser<AddEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddEventCommand
     * and returns an AddEventCommand object for execution.
     *
     * @param userInput full user input string.
     * @return an AddEventCommand object containing the parsed event.
     * @throws ParseException if the user input does not conform the expected format.
     */
    @Override
    public AddEventCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_EVENT_NAME,
                PREFIX_DATE, PREFIX_LOCATION, PREFIX_DESCRIPTION, PREFIX_TAG, PREFIX_CONTACT);

        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_EVENT_NAME, PREFIX_DATE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
        }

        String eventName = ParserUtil.parseEventName(argMultimap.getValue(PREFIX_EVENT_NAME).get());
        LocalDateTime date = DateParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        String location = argMultimap.getValue(PREFIX_LOCATION).orElse("");
        String description = argMultimap.getValue(PREFIX_DESCRIPTION).orElse("");
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        // Parse contacts; they are optional so this list might be empty.
        List<Person> contactsToAdd = ParserUtil.parseContacts(argMultimap.getAllValues(PREFIX_CONTACT));

        // Create an event with an empty contacts list.
        Event event = new Event(eventName, date, location, description, tagList, new UniquePersonList());
        return new AddEventCommand(event, contactsToAdd);
    }
}
