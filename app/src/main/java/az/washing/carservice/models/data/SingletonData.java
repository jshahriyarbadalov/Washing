package az.washing.carservice.models.data;

public class SingletonData {

    private static SingletonData singletonData;

    private String time;
    private  boolean isCheck;

    public static SingletonData getInstance() {

        if (singletonData == null) {
            synchronized (SingletonData.class) {
                if (singletonData == null) {
                    singletonData = new SingletonData();
                }
            }
        }
        return singletonData;
    }

    public  String getTime() {
        return time;
    }

    public  void setTime(String time) {
        this.time = time;
    }

    public  boolean isIsCheck() {
        return isCheck;
    }

    public  void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }


}
