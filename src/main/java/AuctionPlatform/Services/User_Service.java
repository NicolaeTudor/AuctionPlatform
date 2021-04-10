package AuctionPlatform.Services;

import AuctionPlatform.Models.Auction.Auction;
import AuctionPlatform.Models.AuctionCompany;
import AuctionPlatform.Models.User;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class User_Service {
    private final Map<UUID, User> _userList;

    private static volatile User_Service instance;

    private User_Service() {
        _userList = new HashMap<>();
    }

    public static User_Service getInstance() {
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
        User_Service result = instance;
        if (result != null) {
            return result;
        }
        synchronized(User_Service.class) {
            if (instance == null) {
                instance = new User_Service();
            }
            return instance;
        }
    }

    private User getUser(UUID publicKey) {
        return _userList.get(publicKey);
    }

    public UUID createUser(String name) {
        var user = new User(name);
        var publicKey = UUID.randomUUID();
        _userList.put(publicKey, user);
        return publicKey;
    }

    public void addBid(@NotNull UUID auctionKey, @NotNull UUID offerKey, @NotNull UUID userKey, int bidAmount) {
        var user = getUser(userKey);
        var _auctionService = Auction_Service.getInstance();
        var result = _auctionService.addBid(auctionKey, offerKey, userKey, bidAmount);
        if(result) {
            System.out.printf("User %s bid %d tokens\n", user.get_name(), bidAmount);
        }
    }

    public String getUserName(@NotNull UUID userKey) {
        var user = getUser(userKey);
        return user.get_name();
    }
}
