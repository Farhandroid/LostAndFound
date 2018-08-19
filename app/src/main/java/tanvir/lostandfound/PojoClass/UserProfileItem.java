package tanvir.lostandfound.PojoClass;

import java.io.Serializable;

public class UserProfileItem implements Serializable {
    String userName,itemType,itemPlaceName,itemAdress,itemDate,
            itemReward,itemTime,itemDetailedDescription,itemHowManyImage,itemPostDateAndTime,itemCatagory,message;

    public String getItemReward() {
        return itemReward;
    }

    public String getItemPlaceName() {
        return itemPlaceName;
    }

    public String getUserName() {
        return userName;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemAdress() {
        return itemAdress;
    }

    public String getItemDate() {
        return itemDate;
    }

    public String getItemTime() {
        return itemTime;
    }

    public String getItemDetailedDescription() {
        return itemDetailedDescription;
    }

    public String getItemHowManyImage() {
        return itemHowManyImage;
    }

    public String getItemPostDateAndTime() {
        return itemPostDateAndTime;
    }

    public String getItemCatagory() {
        return itemCatagory;
    }

    public String getMessage() {
        return message;
    }
}
