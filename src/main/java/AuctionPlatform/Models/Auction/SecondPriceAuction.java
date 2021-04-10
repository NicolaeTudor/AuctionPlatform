package AuctionPlatform.Models.Auction;

import AuctionPlatform.Models.Offer.OfferEnglish;
import AuctionPlatform.Models.Offer.OfferSecondPrice;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/*  Vickrey's technique, called a second-price auction, works like a sealed-bid auction,
    in that all bids are sealed and sent to an auctioneer. Like a sealed bid auction, the highest bidder wins.
    But the price the winner pays is the price that the second highest bidder has bid.
    For example, suppose that we bid 100 tokens and the second highest bid is 10 tokens.
    Then we will win the bid, but we will only have to pay 10 tokens to secure the good.
    This auction runs in constant time, and maximizes consumer surplus,
    but it is still highly centralized and does not protect the privacy of the bids. */
public class SecondPriceAuction extends Auction {
    public SecondPriceAuction(UUID companyKey, String description) {
        super(companyKey, description);
    }

    @Override
    public UUID addOffer(@NotNull UUID itemKey, int startingBid) {
        var offer = new OfferSecondPrice(itemKey, startingBid);
        var publicKey = UUID.randomUUID();
        _offerList.put(publicKey, offer);

        return publicKey;
    }
}
