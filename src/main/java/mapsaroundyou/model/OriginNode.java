package mapsaroundyou.model;

/**
 * Origin node taken from the repository-tracked rental area dataset.
 *
 * @param originNodeId matrix row identifier shared with listings
 * @param postalCode postal code for the origin
 * @param region broad geographic region label
 * @param areaName finer-grained area label
 */
public record OriginNode(String originNodeId, String postalCode, String region, String areaName) {
}
