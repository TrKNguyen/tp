package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.predicate.AddressContainsKeywordsAsSubstringPredicate;

/**
 * Finds and lists all persons in address book whose address contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindAddressCommand extends Command {

    public static final String COMMAND_WORD = "findAddress";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose address contains any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " street avenue park";

    private final AddressContainsKeywordsAsSubstringPredicate predicate;

    public FindAddressCommand(AddressContainsKeywordsAsSubstringPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FindAddressCommand otherFindAddressCommand)) {
            return false;
        }

        return predicate.equals(otherFindAddressCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("predicate", predicate).toString();
    }
}
