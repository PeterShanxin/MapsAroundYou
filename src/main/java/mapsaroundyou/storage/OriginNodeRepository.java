package mapsaroundyou.storage;

import mapsaroundyou.model.OriginNode;

import java.util.List;
import java.util.Optional;

/**
 * Read-only access to listing origin nodes used for commute matrix keys.
 */
public interface OriginNodeRepository {
    /**
     * Returns every origin node.
     *
     * @return immutable or copied collection
     */
    List<OriginNode> findAll();

    /**
     * Looks up an origin node by id.
     *
     * @param originNodeId candidate id
     * @return the node when present
     */
    Optional<OriginNode> findById(String originNodeId);
}
