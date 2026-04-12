package mapsaroundyou.model;

/**
 * Supported destination for commute searches.
 *
 * @param destinationId stable identifier referenced by the travel matrix
 * @param name human-readable label
 * @param category coarse grouping such as campus or hospital
 * @param area optional neighborhood or region text (may be blank in seed data)
 * @param postalCode Singapore postal code string from the dataset
 */
public record Destination(
        String destinationId,
        String name,
        String category,
        String area,
        String postalCode
) {
}
