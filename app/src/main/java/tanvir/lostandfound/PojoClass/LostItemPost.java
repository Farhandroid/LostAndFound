package tanvir.lostandfound.PojoClass;

import java.io.Serializable;

public class LostItemPost implements Serializable {

    String UserName,WhatIsLost,LostItemPlaceName,LostItemAdress,DayOfLost,TimeOfLost,Reward, DetailedDescription,HowManyImage,PostDateAndTime,message;

    public String getMessage() {
        return message;
    }

    public LostItemPost(String userName, String whatIsLost, String lostItemPlaceName, String lostItemAdress, String dayOfLost, String timeOfLost, String reward, String detailedDescription, String howManyImage, String postDateAndTime, String message) {
        UserName = userName;
        WhatIsLost = whatIsLost;
        LostItemPlaceName = lostItemPlaceName;
        LostItemAdress = lostItemAdress;
        DayOfLost = dayOfLost;
        TimeOfLost = timeOfLost;
        Reward = reward;
        DetailedDescription = detailedDescription;
        HowManyImage = howManyImage;
        PostDateAndTime = postDateAndTime;
        this.message = message;
    }



    public LostItemPost() {
    }


    public String getUserName() {
        return UserName;
    }

    public String getWhatIsLost() {
        return WhatIsLost;
    }

    public String getLostItemPlaceName() {
        return LostItemPlaceName;
    }

    public String getLostItemAdress() {
        return LostItemAdress;
    }

    public String getDayOfLost() {
        return DayOfLost;
    }

    public String getTimeOfLost() {
        return TimeOfLost;
    }

    public String getReward() {
        return Reward;
    }


    public String getDetailedDescription() {
        return DetailedDescription;
    }

    public String getHowManyImage() {
        return HowManyImage;
    }

    public String getPostDateAndTime() {
        return PostDateAndTime;
    }
}
