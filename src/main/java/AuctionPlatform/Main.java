package AuctionPlatform;

import AuctionPlatform.Models.Auction.AuctionType;
import AuctionPlatform.Services.AuctionCompany_Service;
import AuctionPlatform.Services.Auction_Service;
import AuctionPlatform.Services.User_Service;

import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) {
        var _auctionService = Auction_Service.getInstance();
        var _companyService = AuctionCompany_Service.getInstance();
        var _userService = User_Service.getInstance();

        var auctionCompanyName = "Keystone Auctions LLC";
        var companyKey = _companyService.CreateAuctionCompany(auctionCompanyName);

        var itemDescription = "Hepplewhite-style Inlaid Mahogany Breakfront";
        var itemKey = _companyService.AddItem(companyKey, itemDescription);

        var auctionDescription = "Fine Furniture, Art, Coins, Jewelery";
        var auctionKey = _companyService.AddAuction(companyKey, AuctionType.EnglishAuction, auctionDescription);

        var startingBid = 200;
        var offerKey = _companyService.AddOffer(companyKey, itemKey, auctionKey, startingBid);

	    var startTime  = LocalDateTime.now().plusMinutes(1);
        var endTime  = LocalDateTime.now().plusMinutes(6);

        _companyService.PublishAuction(companyKey, auctionKey, startTime, endTime);

        var userName1 = "Gary Atkins";
        var userName2 = "Montell Odling";
        var userKey1 = _userService.createUser(userName1);
        var userKey2 = _userService.createUser(userName2);

        _userService.addBid(auctionKey, offerKey, userKey1, 200);

        _auctionService.startAuction(auctionKey);

        _userService.addBid(auctionKey, offerKey, userKey1, 200);
        _userService.addBid(auctionKey, offerKey, userKey1, 250);
        _userService.addBid(auctionKey, offerKey, userKey1, 500);
        _userService.addBid(auctionKey, offerKey, userKey2, 300);
        _userService.addBid(auctionKey, offerKey, userKey1, 350);

        _companyService.endAuction(companyKey, auctionKey);
    }
}
