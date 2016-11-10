package Lobby;

import Model.User;
import SpelLogica.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by michi on 6/11/2016.
 */
public interface ILobbyMethod extends Remote {
    void addUser(User user) throws RemoteException;
    List<String> getUserNames() throws RemoteException;
    Game canMakeGame(User user) throws RemoteException;
    List<Game> getRunningGames() throws RemoteException;
    void logOutUser(User thisUser) throws RemoteException;
}
