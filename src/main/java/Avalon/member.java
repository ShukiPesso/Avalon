package Avalon;

public class member {
    int id;
    String name;
    charecters charecter;

    public member(String Name, int Id, charecters Character)
    {
        name = Name;
        id = Id;
        charecter = Character;
    }

    public void setCharacter(charecters Character){
        charecter = Character;
    }
}
