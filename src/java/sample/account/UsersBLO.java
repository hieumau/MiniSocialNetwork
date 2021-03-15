package sample.account;

import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import sample.entity.Roles;
import sample.entity.Users;
import sample.jpa_controller.UsersJpaController;
import sample.utils.EmailUtils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Random;

public class UsersBLO {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("SocialNetworkMiniPU");

    public Users create (String userID, String password, String fullName){
        UsersJpaController usersJpaController = new UsersJpaController(emf);
        boolean isCreatNew = true;
        try {
            Users user = get(userID);

            if (user != null){
                if (!user.getStatus().equals("new")) isCreatNew = false;
                else {
                    int vetifyCode = getGenerateVerifyCode();
                    user.setFullName(fullName);
                    //encrypt sha256 password
                    user.setPassword(DigestUtils.sha256Hex(password));
                    set(user);
                    sendVerifyCodeToEmail(userID);
                    return user;
                }
            }

            if (isCreatNew){
                int vetifyCode = getGenerateVerifyCode();
                Users newUser = new Users(userID, fullName, DigestUtils.sha256Hex(password), "new", vetifyCode);
                newUser.setRoleId(new Roles(1));
                usersJpaController.create(newUser);
                sendVerifyCodeToEmail(userID);
                return newUser;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getGenerateVerifyCode(){
        // It will generate 6 digit random Number.
        // from 100000 to 999999
        return new Random().nextInt(899999) + 100000;
    }

    public boolean isExitsUserId(String userID){
        UsersJpaController usersJpaController = new UsersJpaController(emf);
        boolean isExits = false;
        try {
            Users user = usersJpaController.findUsers(userID);

            if (user != null){
                if (!user.getStatus().equals("new")) isExits = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExits;
    }

    public Users checkLogin(String userID, String password) {
        UsersJpaController usersJpaController = new UsersJpaController(emf);
        try {
            Users user = usersJpaController.findUsers(userID);
            if (user != null){
                if (user.getPassword().equals(DigestUtils.sha256Hex(password))){
                    user.setPassword("");
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isActiveUser(String userId) {
        UsersJpaController usersJpaController = new UsersJpaController(emf);
        boolean isActive = false;
        try {
            Users user = usersJpaController.findUsers(userId);

            if (user != null){
                if (user.getStatus().equals("active")) isActive = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isActive;
    }

    public Users get(String userId){
        UsersJpaController usersJpaController = new UsersJpaController(emf);
        return usersJpaController.findUsers(userId);
    }

    public void set(Users user){
        UsersJpaController usersJpaController = new UsersJpaController(emf);

        try {
            usersJpaController.edit(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resendNewVerifyCode(String userId){
        if (!isActiveUser(userId)){
            int verifyCode = getGenerateVerifyCode();
            Users user = get(userId);
            user.setVerifyCode(verifyCode);
            set(user);
            sendVerifyCodeToEmail(userId);
        }
    }

    public void sendVerifyCodeToEmail(String userId){
        EmailUtils.sendEmail(userId);
    }

    public Users verifyAccount(String userId, int verifyCode) {
        UsersJpaController usersJpaController = new UsersJpaController(emf);
        Users user = get(userId);
        if (isActiveUser(userId)){
            return user;
        } else {
            if (user.getVerifyCode() == verifyCode) {
                user.setStatus("active");
                try {
                    usersJpaController.edit(user);
                    return user;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
