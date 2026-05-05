public class AppData {
    private static AppData instance;
    private User selectedUser;

    public static AppData getInstance(){
        if(instance == null){
            instance = new AppData();
        }
        return instance;
    }

    private AppData() {
        selectedUser = null;
    }

    public void setSelectedUser(User u) { this.selectedUser = u; }
    public User getSelectedUser() { return selectedUser; }
}
