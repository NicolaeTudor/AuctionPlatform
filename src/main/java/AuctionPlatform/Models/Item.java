package AuctionPlatform.Models;

import java.util.UUID;

public class Item {
    private final String _description;
    private Boolean _isReserved;

    public Item(String Description) {
        _description = Description;
        _isReserved = false;
    }

    public Boolean isReserved() {
        return _isReserved;
    }

    public void reserveItem() {
        _isReserved = true;
    }

    public String getDescription() {
        return _description;
    }
}
