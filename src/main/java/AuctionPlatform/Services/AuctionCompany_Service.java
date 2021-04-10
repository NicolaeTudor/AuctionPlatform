package AuctionPlatform.Services;

import AuctionPlatform.Models.Auction.AuctionType;
import AuctionPlatform.Models.AuctionCompany;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AuctionCompany_Service {
    private final Map<UUID, AuctionCompany> _companyList;

    // The field must be declared volatile so that double check lock would work
    // correctly.
    private static volatile AuctionCompany_Service instance;

    private AuctionCompany_Service() {
        _companyList = new HashMap<>();
    }

    public static AuctionCompany_Service getInstance() {
        // The approach taken here is called double-checked locking (DCL). It
        // exists to prevent race condition between multiple threads that may
        // attempt to get singleton instance at the same time, creating separate
        // instances as a result.
        //
        // It may seem that having the `result` variable here is completely
        // pointless. There is, however, a very important caveat when
        // implementing double-checked locking in Java, which is solved by
        // introducing this local variable.
        //
        // You can read more info DCL issues in Java here:
        // https://refactoring.guru/java-dcl-issue
        AuctionCompany_Service result = instance;
        if (result != null) {
            return result;
        }
        synchronized(AuctionCompany_Service.class) {
            if (instance == null) {
                instance = new AuctionCompany_Service();
            }
            return instance;
        }
    }

    private AuctionCompany getAuctionCompany(UUID publicKey) {
        return _companyList.get(publicKey);
    }

    public UUID CreateAuctionCompany(String name) {
        var company = new AuctionCompany(name);
        var publicKey = UUID.randomUUID();
        _companyList.put(publicKey, company);

        return publicKey;
    }

    public UUID AddItem(@NotNull UUID companyKey, String itemDescription) {
        var company = getAuctionCompany(companyKey);
        return company.addItem(itemDescription);
    }

    public UUID AddAuction(@NotNull UUID companyKey, @NotNull AuctionType type, String description) {
        var _auctionService = Auction_Service.getInstance();
        var company = getAuctionCompany(companyKey);
        var auctionKey = _auctionService.createAuction(companyKey, type, description);
        company.addAuction(auctionKey);

        return auctionKey;
    }

    public UUID AddOffer(@NotNull UUID companyKey, @NotNull UUID itemKey, @NotNull UUID auctionKey, int startingBid) {
        var _auctionService = Auction_Service.getInstance();
        var company = getAuctionCompany(companyKey);
        if(!company.verifyItem(itemKey)) {
            return null;
        }

        var offerKey = _auctionService.addOffer(auctionKey, companyKey, itemKey, startingBid);
        if(offerKey != null) {
            company.reserveItem(itemKey);
        }

        return offerKey;
    }

    public void PublishAuction(@NotNull UUID companyKey, @NotNull UUID auctionKey,
                               LocalDateTime startTime, LocalDateTime endTime) {
        var _auctionService = Auction_Service.getInstance();
        _auctionService.publishAuction(auctionKey, companyKey, startTime, endTime);
    }

    public void endAuction(@NotNull UUID companyKey, @NotNull UUID auctionKey) {
        var _auctionService = Auction_Service.getInstance();
        var _userService = User_Service.getInstance();
        var company = getAuctionCompany(companyKey);
        var auctionResult = _auctionService.endAuction(auctionKey);
        for (var result: auctionResult) {
            UUID offerKey = result.getValue0();
            UUID userKey = result.getValue1();
            Integer amountToPay = result.getValue2();
            var userName = _userService.getUserName(userKey);
            var itemKey = _auctionService.getItemKey(auctionKey, offerKey);
            var itemDescription = company.getItemDescription(itemKey);
            System.out.printf("User %s won item \"%s\" at %d tokens\n", userName, itemDescription, amountToPay);
        }
    }
}
