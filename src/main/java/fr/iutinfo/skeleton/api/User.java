package fr.iutinfo.skeleton.api;

import com.google.common.base.Charsets;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.security.Principal;
import java.security.SecureRandom;

public class User implements Principal {
    private String name;
    private String alias;
    private int id = 0;
    private String email;
    private String passwdHash;
    private String salt;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(int id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setPassword(String password) {
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putString(password + getSalt(), Charsets.UTF_8);
        passwdHash = hasher.hash().toString();
    }

    public String getPasswdHash() {
        return passwdHash;
    }

    public void setPasswdHash(String passwdHash) {
        this.passwdHash = passwdHash;
    }

    @Override
    public boolean equals(Object arg) {
        if (getClass() != arg.getClass())
            return false;
        User user = (User) arg;
        return name.equals(user.name) && alias.equals(user.alias) && email.equals(user.email) && passwdHash.equals(user.getPasswdHash()) && salt.equals((user.getSalt()));
    }

    @Override
    public String toString() {
        return id + ": " + alias + ", " + name + " <" + email + ">";
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSalt() {
        if (salt == null) {
            salt = generateSalt();
        }
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putLong(random.nextLong());
        return hasher.hash().toString();
    }
}