package AuctionPlatform.Models.Auction;

import AuctionPlatform.Models.Offer.Offer;
import AuctionPlatform.Models.Offer.OfferEnglish;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EnglishAuction extends Auction {

    public EnglishAuction(UUID companyKey, String description) {
        super(companyKey, description);
    }

    @Override
    public UUID addOffer(@NotNull UUID itemKey, int startingBid)  {
        var offer = new OfferEnglish(itemKey, startingBid);
        var publicKey = UUID.randomUUID();
        _offerList.put(publicKey, offer);

        return publicKey;
    }
}
