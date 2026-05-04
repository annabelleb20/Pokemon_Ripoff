public class User {
    private final int Id;
    private final String Name;
    private final String Pass;
    private final boolean isAdmin;

    public User(int Id, String Name, String Pass, boolean isAdmin){
        this.Id = Id;
        this.Name = Name;
        this.Pass = Pass;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getPass() {
        return Pass;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
