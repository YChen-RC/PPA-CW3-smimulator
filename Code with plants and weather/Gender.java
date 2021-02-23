
public enum Gender {
    MALE("1"), FEMALE("0"), UNKNOWN("2");
    
    private String genderString;
    
    Gender(String genderString)
    {
        this.genderString = genderString;
    }
}