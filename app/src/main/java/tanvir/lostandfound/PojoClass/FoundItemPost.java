package tanvir.lostandfound.PojoClass;

import java.io.Serializable;

public class FoundItemPost implements Serializable {

    String UserName,WhatIsFound,
            FoundItemPlaceName,FoundItemAdress,DayOfFound,
            TimeOfFound,DetailedDescription,HowManyImage,PostDateAndTime,message;

    public FoundItemPost(String userName, String whatIsFound, String foundItemPlaceName, String foundItemAdress, String dayOfFound, String timeOfFound, String detailedDescription, String howManyImage, String postDateAndTime, String message) {
        UserName = userName;
        WhatIsFound = whatIsFound;
        FoundItemPlaceName = foundItemPlaceName;
        FoundItemAdress = foundItemAdress;
        DayOfFound = dayOfFound;
        TimeOfFound = timeOfFound;
        DetailedDescription = detailedDescription;
        HowManyImage = howManyImage;
        PostDateAndTime = postDateAndTime;
        this.message = message;
    }

    public FoundItemPost() {
    }

    public String getUserName() {
        return UserName;
    }

    public String getWhatIsFound() {
        return WhatIsFound;
    }

    public String getFoundItemPlaceName() {
        return FoundItemPlaceName;
    }

    public String getFoundItemAdress() {
        return FoundItemAdress;
    }

    public String getDayOfFound() {
        return DayOfFound;
    }

    public String getTimeOfFound() {
        return TimeOfFound;
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

    public String getMessage() {
        return message;
    }
}
