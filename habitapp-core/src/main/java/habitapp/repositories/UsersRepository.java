package habitapp.repositories;

import habitapp.entities.User;

public interface UsersRepository {
    public void addUser (User user);
    public void redactUser (User oldUser , User newUser );
    public void deleteUser (User user);
    public boolean hasUser (User user);
    public User getUser (String email);
    public int getUserId (User user);
}
