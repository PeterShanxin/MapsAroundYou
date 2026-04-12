package mapsaroundyou.common;

import mapsaroundyou.model.TransportMode;

/**
 * Shared application defaults for the Week 8 scaffold.
 */
public final class AppConfig {
    /** Classpath location of the supported destinations CSV. */
    public static final String DESTINATIONS_RESOURCE = "commute_data/Dst_List.csv";
    /** Classpath location of the rental origin node CSV. */
    public static final String ORIGIN_NODES_RESOURCE = "commute_data/Rental_List.csv";
    /** Classpath location of the public-transit travel-time matrix CSV. */
    public static final String TRAVEL_TIMES_RESOURCE = "commute_data/transit_matrix.csv";
    /** Classpath location of the curated listings CSV. */
    public static final String LISTINGS_RESOURCE = "commute_data/listings.csv";
    /** Classpath location of dataset provenance metadata. */
    public static final String DATASET_METADATA_RESOURCE = "commute_data/dataset-metadata.properties";

    /** Maximum number of ranked listings returned to callers. */
    public static final int DEFAULT_RESULT_LIMIT = 10;
    /** Ratio of walk minutes to total minutes that marks a walk-dominant commute. */
    public static final double DEFAULT_WALK_DOMINANT_THRESHOLD = 0.6d;
    /** Default transport mode used when callers do not override preferences. */
    public static final TransportMode DEFAULT_TRANSPORT_MODE = TransportMode.PUBLIC_TRANSPORT;

    private AppConfig() {
    }
}
