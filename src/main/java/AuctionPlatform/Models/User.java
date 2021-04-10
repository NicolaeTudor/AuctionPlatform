package AuctionPlatform.Models;

import java.util.UUID;

public class User {
    private final String _name;

    public User(String name) {
        _name = name;
    }

    public String get_name() {
        return _name;
    }
}
