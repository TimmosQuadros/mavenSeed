package test;

import entity.User;
import facades.UserFacade;
import security.PasswordStorage;

public class Populate
{
    public static void main(String[] args) throws PasswordStorage.CannotPerformOperationException
    {
        UserFacade uf = new UserFacade();
        User user = new User("Peter", PasswordStorage.createHash("test"));
        user.addRole("User");
        uf.createUser(user);
        User admin = new User("Anne", PasswordStorage.createHash("test"));
        admin.addRole("Admin");
        uf.createUser(admin);
    }
}