package AuctionPlatform.Models.Offer;

import org.javatuples.Pair;

import java.util.*;

public class OfferSecondPrice extends Offer {
    public OfferSecondPrice(UUID itemKey, int startingBid) {
        super(itemKey, startingBid);
    }

    // In case of tie the winner will be random between the highest bidders
    @Override
    public Pair<UUID, Integer> stopBidding() {
        var entryList = new ArrayList<>(_bids.entrySet());
        entryList.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        var payingPrice = _startingBid;
        var highestBid = _startingBid;
        UUID winner = null;

        for (var entry : entryList) {
            if (entry.getValue() > highestBid) {
                payingPrice = highestBid;
                highestBid = entry.getValue();
                winner = entry.getKey();
            }
        }

        _winnerKey = winner;
        _soldValue = payingPrice;

        return finalBid();
    }
}
