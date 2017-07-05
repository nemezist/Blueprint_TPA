package edu.bluejack16_2.blueprint;

/**
 * Created by BAGAS on 05-Jul-17.
 */

public class UserExploreModel {
    String userID,userEmail,userDisplayName,photoURL;

    UserExploreModel(){}

    public UserExploreModel(String userID, String userEmail, String userDisplayName, String photoURL) {
        this.userID = userID;
        this.userEmail = userEmail;
        this.userDisplayName = userDisplayName;
        this.photoURL = photoURL;
    }
}
