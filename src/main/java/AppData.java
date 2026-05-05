import java.util.ArrayList;
import java.util.List;

public class AppData {
    private static AppData instance;
    private User selectedUser;
    private List<User> duel = new ArrayList<>();

    public static AppData getInstance(){
        if(instance == null){
            instance = new AppData();
        }
        return instance;
    }

    private AppData() {
        selectedUser = null;
        duel = null;
    }

    public void setSelectedUser(User u) { this.selectedUser = u; }
    public User getSelectedUser() { return selectedUser; }

    public void setDuel(List<User> u) {this.duel = u;}
    public List<User> getDuel() {return duel;}
}
