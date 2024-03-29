package Registreer;

import Database.IDatabaseMethod;
import Model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class RegistreerMethod extends UnicastRemoteObject implements IRegistreerMethod {
    private IDatabaseMethod database;

    public RegistreerMethod(IDatabaseMethod database) throws RemoteException {
        this.database = database;
    }

    public User getUser(String userName, User user) throws RemoteException {
        user.setNaam(userName);
        User gebruiker = database.getInfo(user);
        String UID = database.getToken(user);
        gebruiker.setToken(UID);

        return gebruiker;
    }

    public boolean checkCredentials(String name, String pas) throws RemoteException{
        boolean check = database.checkCredentials(name,pas);
        return check;
    }

    @Override
    public boolean createAccount(String name, String pas) throws RemoteException {
        boolean check = database.createAccount(name,pas);
        return check;
    }

    @Override
    public boolean createSalt(byte[] salt, String name) throws RemoteException {
        return database.createSalt(salt,name);
    }

    @Override
    public byte[] getSalt(String name) throws RemoteException {
        return database.getSalt(name);
    }
}
