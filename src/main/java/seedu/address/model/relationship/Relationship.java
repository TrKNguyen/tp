package seedu.address.model.relationship;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Relationship between two persons in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Relationship {
    public static final String MESSAGE_CONSTRAINTS =
            "Relationship names can take any values, should not be blank, "
                    + "and must start with a non-whitespace character";
    public static final String VALIDATION_REGEX = "\\S.*"; // Must start with non-whitespace

    private final String firstUserId;
    private final String secondUserId;
    private final String forwardName;
    private final String reverseName;
    private final Set<Tag> tags;

    /**
     * Constructs a {@code Relationship}.
     * A relationship is a connection between two users in the address book. It has a name and can have tags.
     *
     * @param firstUserId The user ID of the first user in the relationship.
     * @param secondUserId The user ID of the second user in the relationship.
     * @param forwardName The name of the relationship from user1 to user2.
     * @param reverseName The name of the relationship from user2 to user1.
     * @param tags The tags associated with the relationship.
     */
    public Relationship(String firstUserId,
                        String secondUserId,
                        String forwardName,
                        String reverseName,
                        Set<Tag> tags) {
        // null checks
        requireNonNull(firstUserId, "First user ID cannot be null");
        requireNonNull(secondUserId, "Second user ID cannot be null");
        requireNonNull(forwardName, "Forward relationship name cannot be null");
        requireNonNull(reverseName, "Reverse relationship name cannot be null");

        checkArgument(isValidRelationshipName(forwardName), MESSAGE_CONSTRAINTS);
        checkArgument(isValidRelationshipName(reverseName), MESSAGE_CONSTRAINTS);

        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.forwardName = forwardName;
        this.reverseName = reverseName;
        this.tags = new HashSet<>(tags);
    }

    /**
     * Validates the given relationship name.
     * A valid relationship name is any non-empty string.
     *
     * @param test The relationship name to validate.
     * @return True if the relationship name is valid, False otherwise.
     */
    public static boolean isValidRelationshipName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public String getFirstUserId() {
        return firstUserId;
    }

    public String getSecondUserId() {
        return secondUserId;
    }

    public String getForwardName() {
        return forwardName;
    }

    public String getReverseName() {
        return reverseName;
    }

    public String getNameFromPerspective(String userId) {
        if (userId.equals(firstUserId)) {
            return forwardName;
        } else if (userId.equals(secondUserId)) {
            return reverseName;
        }
        throw new IllegalArgumentException("User ID does not match either end of the relationship");
    }

    /**
     * Returns an immutable copy of the relationship's tags.
     *
     * @return An immutable copy of the relationship's tags.
     */
    public Set<Tag> getTags() {
        return new HashSet<>(tags);
    }

    /**
     * Checks if this relationship involves the given user ID.
     *
     * @param userId The user ID to check.
     * @return True if this relationship involves the given user ID, False otherwise.
     */
    public boolean involvesUser(String userId) {
        return firstUserId.equals(userId) || secondUserId.equals(userId);
    }

    /**
     * Checks if this relationship involves the given user IDs and matches either the forward or reverse name.
     * The order of user IDs doesn't matter, but one of the relationship names must match exactly.
     *
     * @param userId1 The ID of the first user
     * @param userId2 The ID of the second user
     * @param relationshipName The name to check against both forward and reverse names
     * @return true if the relationship matches the given parameters
     */
    public boolean isSameRelationship(String userId1, String userId2, String relationshipName) {
        boolean usersMatch = (this.firstUserId.equals(userId1) && this.secondUserId.equals(userId2))
                || (this.firstUserId.equals(userId2) && this.secondUserId.equals(userId1));

        if (!usersMatch) {
            return false;
        }

        // Check if the provided name matches either the forward or reverse name
        if (this.firstUserId.equals(userId1)) {
            return this.forwardName.equals(relationshipName) || this.reverseName.equals(relationshipName);
        } else {
            return this.reverseName.equals(relationshipName) || this.forwardName.equals(relationshipName);
        }
    }

    /**
     * Checks if this relationship is the same as the given relationship.
     *
     * @param other The relationship to compare with.
     * @return True if this relationship is the same as the given relationship, False otherwise.
     */
    public boolean isSameRelationship(Relationship other) {
        if (other == this) {
            return true;
        }

        if (other == null) {
            return false;
        }

        // Check if relationships match in forward direction
        boolean matchesForwardDirection = firstUserId.equals(other.firstUserId)
                && secondUserId.equals(other.secondUserId)
                && forwardName.equals(other.forwardName)
                && reverseName.equals(other.reverseName);

        // Check if relationships match in reverse direction
        boolean matchesReverseDirection = firstUserId.equals(other.secondUserId)
                && secondUserId.equals(other.firstUserId)
                && forwardName.equals(other.reverseName)
                && reverseName.equals(other.forwardName);

        return matchesForwardDirection || matchesReverseDirection;
    }

    /**
     * Returns a new relationship with the given tag added to it.
     *
     * @param tag The tag to add.
     * @return A new relationship with the given tag added to it.
     */
    public Relationship withAddedTag(Tag tag) {
        Set<Tag> newTags = new HashSet<>(tags);
        newTags.add(tag);
        return new Relationship(firstUserId, secondUserId, forwardName, reverseName, newTags);
    }

    /**
     * Returns a new relationship with the given tag removed from it.
     *
     * @param tag The tag to remove.
     * @return A new relationship with the given tag removed from it.
     */
    public Relationship withRemovedTag(Tag tag) {
        Set<Tag> newTags = new HashSet<>(tags);
        newTags.remove(tag);
        return new Relationship(firstUserId, secondUserId, forwardName, reverseName, newTags);
    }

    /**
     * Checks if this relationship is the same as the given relationship. This method ignores the tags.
     *
     * @param other The relationship to compare with.
     * @return True if this relationship is the same as the given relationship, False otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Relationship otherRelationship)) {
            return false;
        }

        return isSameRelationship(otherRelationship) && tags.equals(otherRelationship.tags);
    }

    /**
     * Returns the hash code of this relationship.
     * The hash code is based on the user IDs of the two users in the relationship, the name of the relationship,
     * and the tags associated with the relationship.
     *
     * @return The hash code of this relationship.
     */
    @Override
    public int hashCode() {
        // Create a consistent hash regardless of direction
        String smallerId = firstUserId.compareTo(secondUserId) < 0 ? firstUserId : secondUserId;
        String largerId = firstUserId.compareTo(secondUserId) < 0 ? secondUserId : firstUserId;
        String smallerName = smallerId.equals(firstUserId) ? forwardName : reverseName;
        String largerName = smallerId.equals(firstUserId) ? reverseName : forwardName;

        return Objects.hash(smallerId + largerId, smallerName + largerName, tags);
    }

    /**
     * Returns the string representation of this relationship.
     * The string representation includes the name of the relationship, and the user IDs of the two users in the
     * relationship. If the relationship has tags, the string representation will also include the tags.
     *
     * @return The string representation of this relationship.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[Forward: ")
                .append(forwardName)
                .append(", Reverse: ")
                .append(reverseName)
                .append("] Between: ")
                .append(firstUserId)
                .append(" and ")
                .append(secondUserId);

        if (!tags.isEmpty()) {
            builder.append(" Tags: ");
            tags.forEach(builder::append);
        }
        return builder.toString();
    }
}
