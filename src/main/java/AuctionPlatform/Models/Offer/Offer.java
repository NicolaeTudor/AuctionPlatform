package AuctionPlatform.Models.Offer;

import org.javatuples.Pair;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Offer {
    protected final UUID _itemKey;
    protected UUID _winnerKey;
    protected int _soldValue;
    protected final int _startingBid;
    protected final Map<UUID, Integer> _bids;

    public Offer(UUID itemKey, int startingBid) {
        _itemKey = itemKey;
        _startingBid = startingBid;
        _bids = new HashMap<>();
    }

    public Boolean addBid(UUID userKey, int bidAmount) {
        if(bidAmount < _startingBid) {
            return false;
        }

        _bids.put(userKey, bidAmount);
        return true;
    }

    protected Pair<UUID, Integer> finalBid() {
        return  new Pair<>(_winnerKey, _soldValue);
    }

    //Sets _winnerKey and _soldValue and return the pair
    public abstract Pair<UUID, Integer>  stopBidding();

    public UUID getItemKey() {
        return _itemKey;
    }
}
