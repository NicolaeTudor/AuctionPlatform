package AuctionPlatform.Models.Auction;

import AuctionPlatform.Models.Offer.Offer;
import AuctionPlatform.Models.Offer.OfferEnglish;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;

import org.javatuples.Triplet;

public abstract class Auction {
    protected final UUID _companyKey;
    protected LocalDateTime _startTime;
    protected LocalDateTime _endTime;
    protected String _description;
    protected AuctionStatus _status;
    protected Map<UUID, Offer> _offerList;

    public Auction(UUID companyKey, String description) {
        _description = description;
        _companyKey = companyKey;
        _status = AuctionStatus.Unpublished;
        _offerList = new HashMap<>();
    }

    public Boolean publishAuction(LocalDateTime startTime, LocalDateTime endTime) {
        if(startTime.isBefore(LocalDateTime.now())) {
            System.out.println("Auction start time must be after current time");
            return false;
        }
        else if(endTime.isBefore(startTime)) {
            System.out.println("Auction can't end before it starts");
            return false;
        }

        _startTime = startTime;
        _endTime = endTime;
        _status = AuctionStatus.Published;

        return true;
    }

    public void startAuction() {
        _status = AuctionStatus.InProgress;
    }

    // offerKey, userKey, payAmount
    public List<Triplet<UUID, UUID, Integer>> endAuction() {
        _status = AuctionStatus.Ended;
        List<Triplet<UUID, UUID, Integer>> list = new ArrayList<>();
        for (var offerEntry: _offerList.entrySet()) {
            var offer = offerEntry.getValue();
            var finalBid = offer.stopBidding();
            list.add(new Triplet<UUID, UUID, Integer>(offerEntry.getKey(), finalBid.getValue0(), finalBid.getValue1()));
        }

        return list;
    }

    public Boolean validateCompany(@NotNull UUID companyKey) {
        return _companyKey == companyKey;
    }

    public abstract UUID addOffer(@NotNull UUID itemKey, int startingBid);

    protected Boolean canAddBid() {
        if(_status != AuctionStatus.InProgress) {
            System.out.println("You can only bid on ongoing auctions!");
            return false;
        }
        return true;
    }

    public Boolean addBid(@NotNull UUID offerKey, @NotNull UUID userKey, int bidAmount) {
        if(!canAddBid()) {
            return false;
        }
        var offer = _offerList.get(offerKey);

        return offer.addBid(userKey, bidAmount);
    }

    public UUID getItemKey(UUID offerKey) {
        var offer = _offerList.get(offerKey);
        return offer.getItemKey();
    }
}
