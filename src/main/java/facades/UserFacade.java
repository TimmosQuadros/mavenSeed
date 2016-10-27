package facades;

import security.IUserFacade;
import entity.User;
import facades.exceptions.NonexistentEntityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import security.*;

public class UserFacade implements IUserFacade {

    /*When implementing your own database for this seed, you should NOT touch any of the classes in the security folder
    Make sure your new facade implements IUserFacade and keeps the name UserFacade, and that your Entity User class implements 
    IUser interface, then security should work "out of the box" with users and roles stored in your database */
    //private final Map<String, IUser> users = new HashMap<>();
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("seed");
    private UserJpaController ujpa = new UserJpaController(emf);
    private final Map<String, IUser> users = new HashMap<>();

    public UserFacade() {
        List<User> listOfUsers = ujpa.findUserEntities(); //Should use db to search for the user instead of retreiving the entire list every time but i'm in a hurry :).
        for (User user : listOfUsers) {
            users.put(user.getUserName(), user);
        }
    }

    @Override
    public IUser getUserByUserId(String id) {
        return users.get(id);
    }

    /*
  Return the Roles if users could be authenticated, otherwise null
     */
    @Override
    public List<String> authenticateUser(String userName, String password) {
        IUser user = users.get(userName);
        boolean validPassword = false;
        try {
            validPassword = PasswordStorage.verifyPassword(password, user.getPassword());
        } catch (PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException ex) {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return user != null && validPassword ? user.getRolesAsStrings() : null;
    }

    //Create
    public void createUser(User user) {
        ujpa.create(user);
    }
    //Retrieve

    public List<User> findUserEntities() {
        return ujpa.findUserEntities();
    }

    public User findUser(Integer id) {
        return ujpa.findUser(id);
    }
    //Update

    public void editUser(User user) throws NonexistentEntityException, Exception {
        ujpa.edit(user);
    }
    //Delete

    public void destroyUser(Integer id) throws NonexistentEntityException {
        ujpa.destroy(id);
    }

}
