package com.conferences.services;

import com.conferences.dao.DaoFactory;
import com.conferences.dao.UserDao;
import com.conferences.exceptions.DaoException;
import com.conferences.model.ResetPasswordTokenInfo;
import com.conferences.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;
import static org.mindrot.jbcrypt.BCrypt.*;

/**
 * The {@code UserService} class defines methods that provide access to {@code UserDao}
 * to view layer by checking the input data
 *
 * @author Dmytro Martyshchuk
 */

public class UserService {
    private final UserDao userDao;

    /**
     * Initializes {@code UserService} object for designed database management system
     *
     * @param database a name of a database management system
     */
    public UserService(String database) {
        DaoFactory daoFactory = DaoFactory.getDaoFactory(database);
        this.userDao = daoFactory.getUserDao();
    }

    /**
     * Allows to create a new User and insert to the database
     *
     * @param email     {@link User} object uniq email
     * @param firstName a User first name
     * @param lastName  a User last name
     * @param password  a User password
     * @return a {@code User} object with updated identifier
     * @throws DaoException if a database access error occurs
     */
    public User createUser(String email, String firstName, String lastName, String password, String locale) throws DaoException {
        if (!(isValidEmail(email) && isValidPassword(password))) {
            throw new IllegalArgumentException("email or password is invalid!");
        }
        final User user = new User();
        user.setLogin(email);
        user.setPassword(hashpw(password, gensalt()));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(User.Role.USER);
        user.setLocale(locale);
        return userDao.add(user);
    }

    /**
     * Allows to get {@link User} object from the database
     *
     * @param login {@code User} uniq email
     * @return {@link Optional} with {@link User} object if User was found, otherwise {@code Optional} empty
     * @throws DaoException if a database access error occurs
     */
    public Optional<User> getUser(String login) throws DaoException {
        return userDao.getUserByLogin(login);
    }

    public Optional<User> getUser(long id) throws DaoException {
        return userDao.findById(id);
    }

    /**
     * Returns a {@link  User} object if a User with specified login is exists
     * and specified password hash is equals or {@code null}.
     *
     * @param login    - element represent login from view layer
     * @param password - clear text password from view layer
     * @return {@code User} if user with specified parameters is exists or {@code null;}
     */
    public User authenticateUser(String login, String password) throws DaoException {
        if (!(nonNull(login) && nonNull(password))) {
            throw new IllegalArgumentException("Cannot authenticate User, because parameters is null");
        }
        return getUser(login).filter(u -> checkpw(password, u.getPassword())).orElse(null);
    }

    /**
     * Allows to get all speakers from the database
     *
     * @return {@link List} of {@link User} objects
     * @throws DaoException if a database access error occurs
     */
    public List<User> getAllSpeakers() throws DaoException {
        return userDao.findUsersByRole(User.Role.SPEAKER);
    }

    public String createPasswordResetToken(long id) throws DaoException, NoSuchAlgorithmException, InvalidKeySpecException {
        long tokenLifetime = 3600 * 1000;
        long expiry = System.currentTimeMillis() + tokenLifetime;
        String token = generatePasswordResetToken();
        ResetPasswordTokenInfo tokenInfo = new ResetPasswordTokenInfo();

        tokenInfo.setUserId(id);
        tokenInfo.setToken(hashSHA256(token));
        tokenInfo.setTokenExpiry(expiry);
        while (!userDao.createToken(tokenInfo)) {
            tokenInfo.setToken(hashSHA256(generatePasswordResetToken()));
            tokenInfo.setTokenExpiry(System.currentTimeMillis() + tokenLifetime);
        }
        return token;
    }

    public long resetPassword(String token, String password) throws DaoException, NoSuchAlgorithmException {
        if(!isValidPassword(password)) {
            return -1;
        }

        ResetPasswordTokenInfo tokenInfo = userDao.getTokenInfo(hashSHA256(token));

        if(tokenInfo == null) {
            return -1;
        }

        long expiryTime = tokenInfo.getTokenExpiry();

        if(expiryTime > System.currentTimeMillis()) {
           userDao.resetPassword(tokenInfo.getUserId(), hashpw(password, gensalt()));
           return tokenInfo.getUserId();
        }
        return -1;
    }

    public boolean setLocale(long userId, String locale) throws DaoException {
        return userDao.setLocale(userId, locale);
    }

    private String generatePasswordResetToken() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] random = new byte[64];
        byte[] salt = new byte[64];

        new SecureRandom().nextBytes(random);
        new SecureRandom().nextBytes(salt);
        int iterations = 1000;
        int keyLength = 64 * 6;

        KeySpec spec = new PBEKeySpec(bytesToString(random).toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] secret = secretKeyFactory.generateSecret(spec).getEncoded();

        String token = toHex(secret);
        token = token.replaceAll("[/+=]", "");

        return token;
    }

    private boolean isValidEmail(String email) {
        final String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        final String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private String bytesToString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String toHex(byte[] arr) {
        BigInteger bi = new BigInteger(1, arr);
        String hex = bi.toString(16);
        int paddingLength = (arr.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0);
        }
        return hex;
    }

    private String hashSHA256(String base) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return bytesToString(digest.digest(base.getBytes(StandardCharsets.UTF_8)));
    }
}
