package AuctionPlatform.Models;

import java.util.*;

public class AuctionCompany {
    //TODO: make some functionalities of the service work only with the privateKey
    private final UUID _auctionCompanyId;
    String _name;
    Map<UUID, Item> _catalog;
    Set<UUID> _auctionList;

    public AuctionCompany(String name) {
        _auctionCompanyId = UUID.randomUUID();
        _name = name;
        _catalog = new HashMap<>();
        _auctionList = new HashSet<>();
    }

    public UUID addItem(String itemDescription) {
        var item = new Item(itemDescription);
        var publicKey = UUID.randomUUID();
        _catalog.put(publicKey, item);

        return publicKey;
    }

    public void addAuction(UUID auctionKey) {
        _auctionList.add(auctionKey);
    }

    public Boolean verifyItem(UUID itemKey) {
        var item = _catalog.get(itemKey);
        return !(item == null || item.isReserved());
    }

    public void reserveItem(UUID itemKey) {
        var item = _catalog.get(itemKey);
        if (item != null) {
            item.reserveItem();
        }
    }

    public String getItemDescription(UUID itemKey) {
        var item = _catalog.get(itemKey);
        return item.getDescription();
    }
}
