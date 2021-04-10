package AuctionPlatform.Models.Offer;

import org.javatuples.Pair;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;

public class OfferEnglish extends Offer{
    public OfferEnglish(UUID itemKey, int startingBid) {
        super(itemKey, startingBid);
    }

    @Override
    public Boolean addBid(UUID userKey, int bidAmount) {
        var highestBid = getHighestBid();
        if(userKey == highestBid.getKey()) {
            System.out.println("You can't outbid yourself!");
            return false;
        }
        else if(bidAmount <= highestBid.getValue()) {
            System.out.println("You must bid a higher value then the current one!");
            return false;
        }

        return super.addBid(userKey, bidAmount);
    }

    @Override
    public Pair<UUID, Integer> stopBidding() {
        var highestBid = getHighestBid();
        _winnerKey = highestBid.getKey();
        _soldValue = highestBid.getValue();

        return finalBid();
    }

    private Map.Entry<UUID, Integer> getHighestBid() {
        var maxEntry = _bids.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        return maxEntry.orElseGet(() -> new AbstractMap.SimpleEntry<>(null, _startingBid));
    }
}
