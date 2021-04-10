package AuctionPlatform.Services;

import AuctionPlatform.Models.Auction.Auction;
import AuctionPlatform.Models.Auction.AuctionType;
import AuctionPlatform.Models.Auction.EnglishAuction;
import AuctionPlatform.Models.Auction.SecondPriceAuction;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;

public final class Auction_Service {
    private final Map<UUID, Auction> _auctionList;
    private final Set<UUID> _publishedAuctionList;
    private final Set<UUID> _ongoingAuctionList;
    // The field must be declared volatile so that double check lock would work
    // correctly.
    private static volatile Auction_Service instance;

    private Auction_Service() {
        _auctionList = new HashMap<>();
        _publishedAuctionList = new HashSet<>();
        _ongoingAuctionList = new HashSet<>();
    }

    public static Auction_Service getInstance() {
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
        Auction_Service result = instance;
        if (result != null) {
            return result;
        }

        synchronized(Auction_Service.class) {
            if (instance == null) {
                instance = new Auction_Service();
            }
            return instance;
        }
    }

    private Auction getAuction(UUID publicKey) {
        return _auctionList.get(publicKey);
    }

    UUID createAuction(@NotNull UUID companyKey, @NotNull AuctionType type, String description) {
        Auction auction;
        switch (type) {
            case EnglishAuction -> auction = new EnglishAuction(companyKey, description);
            case SecondPriceAuction -> auction = new SecondPriceAuction(companyKey, description);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        var publicKey = UUID.randomUUID();
        _auctionList.put(publicKey, auction);

        return publicKey;
    }

    UUID addOffer(@NotNull UUID auctionKey, @NotNull UUID companyKey, @NotNull UUID itemKey, int startingBid) {
        var auction = getAuction(auctionKey);
        if(!auction.validateCompany(companyKey)) {
            System.out.println("Company must own the auction to add offers");
            return null;
        }

        return auction.addOffer(itemKey, startingBid);
    }

    Boolean publishAuction(@NotNull UUID auctionKey, @NotNull UUID companyKey,
                           LocalDateTime startTime, LocalDateTime endTime) {
        var auction = getAuction(auctionKey);
        if(!auction.validateCompany(companyKey)) {
            System.out.println("Company must own the auction to publish it");
            return null;
        }

        var result = auction.publishAuction(startTime, endTime);
        if(result) {
            _publishedAuctionList.add(auctionKey);
        }

        return result;
    }

    //TODO: I plan on having a job scheduler that will start/ end the auctions at the specified times
    public void startAuction(@NotNull UUID auctionKey) {
        var auction = getAuction(auctionKey);
        auction.startAuction();
        _ongoingAuctionList.add(auctionKey);
    }

    List<Triplet<UUID, UUID, Integer>> endAuction(@NotNull UUID auctionKey) {
        var auction = getAuction(auctionKey);
        var auctionResult = auction.endAuction();
        _ongoingAuctionList.remove(auctionKey);

        return auctionResult;
    }

    Boolean addBid(@NotNull UUID auctionKey, @NotNull UUID offerKey, @NotNull UUID userKey, int bidAmount) {
        var auction = getAuction(auctionKey);
        return auction.addBid(offerKey, userKey, bidAmount);
    }

    public UUID getItemKey(UUID auctionKey, UUID offerKey) {
        var auction = getAuction(auctionKey);
        return auction.getItemKey(offerKey);
    }
}
