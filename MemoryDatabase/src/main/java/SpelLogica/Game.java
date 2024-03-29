package SpelLogica;

import Model.User;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Created by michi on 6/11/2016.
 */
public class Game implements Serializable {
    private LinkedHashMap<User, Integer> punten; //voordeel t.o.v. gewone hashmap: volgorde van toevoegen wordt behouden
    private List<Integer> imageIDs, userIDs, reedsGevondenImages;
    private String name, thema;
    private int aantalSpelers, maxAantalSpelers, gameId, buzzyUserID = -1;
    private int[][] veld;//ontdekte combinaties zijn negatief
    private boolean uitgespeeld = false, vorigeWasFout = false;
    private List<Move> moves;
    private int serverId;

    public Game(int gameID) {
        this.gameId = gameID;
        punten = new LinkedHashMap<User, Integer>();
        userIDs = new ArrayList<Integer>();
        reedsGevondenImages = new ArrayList<Integer>();
        moves = new ArrayList<Move>();
        moves.add(new Move());
    }

    public void addUser(User u) {
        if (aantalSpelers < maxAantalSpelers || aantalSpelers == 0) {
            punten.put(u, 0);
            userIDs.add(u.getId());
            aantalSpelers++;
            if (buzzyUserID == -1) {
                buzzyUserID = u.getId(); //deze speler mag beginnen
            }
        }
    }

    public boolean isVorigeWasFout() {
        return vorigeWasFout;
    }

    public void setVorigeWasFout(boolean vorigeWasFout) {
        this.vorigeWasFout = vorigeWasFout;
    }

    public boolean isUitgespeeld() {
        return uitgespeeld;
    }

    public void setUitgespeeld(boolean uitgespeeld) {
        this.uitgespeeld = uitgespeeld;
    }

    public int getBuzzyUserID() {
        return buzzyUserID;
    }

    public int[][] getVeld() {
        return veld;
    }

    public List<Integer> getReedsGevondenImages() {
        return reedsGevondenImages;
    }

    public String getThema() {
        return thema;
    }

    public int getGameId() {
        return gameId;
    }

    public Move getMove(int index) throws InterruptedException {
        while(moves.size()<=index){
            //wachten
            Thread.sleep(20);
        }
        return moves.get(index);
    }

    public int getGrootteVeld() {
        return veld[0].length;
    }

    public void setAantalSpelers(int aantalSpelers) {
        this.aantalSpelers = aantalSpelers;
    }

    public void setGrootteVeld(int grootteVeld) {
        this.veld = new int[grootteVeld][grootteVeld];
    }

    public User getUserFromGame(Integer userID) {
        for (User user : punten.keySet()) {
            if (user.getId() == userID) {
                return user;
            }
        }
        return null;
    }

    public void setThema(String thema) {
        this.thema = thema;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAantalSpelers() {
        return aantalSpelers;
    }

    public int getMaxAantalSpelers() {
        return maxAantalSpelers;
    }

    public List<User> getGamers() {
        List spelers = new ArrayList<User>(punten.keySet());
        List indexen = new ArrayList<Integer>();
        for(int i = 0; i<spelers.size(); i++){
            User u = (User) spelers.get(i);
            if(!userIDs.contains(u.getId())){
                indexen.add(i);
            }
        }
        for(int i =0;i<indexen.size();i++){
            spelers.remove(spelers.get((Integer) indexen.get(i)));
            for (int j = 0;j<indexen.size();j++){
                Integer integer = new Integer((Integer) indexen.get(j));
                integer--;
                indexen.set(j,integer);
            }
        }
        return spelers;
    }

    public List<Integer> getImageIDs() {
        return imageIDs;
    }

    public void setMaxAantalSpelers(int maxAantalSpelers) {
        this.maxAantalSpelers = maxAantalSpelers;
    }

    public void maakVeld(List<Integer> mogelijkeIDs) {
        imageIDs = new ArrayList<Integer>();
        imageIDs.addAll(mogelijkeIDs);
        mogelijkeIDs.addAll(mogelijkeIDs); //iedere figuur zit twee keer in de lijst
        int id, index, lengte = veld[0].length;
        for (int i = 0; i < lengte; i++) {
            for (int j = 0; j < lengte; j++) {
                System.out.println("mogelijke ID's zijn "+mogelijkeIDs.size());
                index = new Random().nextInt(mogelijkeIDs.size());
                id = mogelijkeIDs.get(index);
                veld[i][j] = id;
                mogelijkeIDs.remove(index);
                System.out.print("  " + id);
            }
            System.out.println();
        }
    }

    public void removeUser(User user) {
        punten.remove(user);
        userIDs.remove(new Integer(user.getId()));
        aantalSpelers--;
    }

    public boolean doMove(Integer userID, Move move) {
        int idEersteKaart = veld[move.getCardX1()][move.getCardY1()];
        int idTweedeKaart = veld[move.getCardX2()][move.getCardY2()];
        User user = getUserFromGame(userID);
        if (idEersteKaart == idTweedeKaart) {
            punten.put(user, punten.get(user) + 1);
            System.out.println(" speler " + user.getNaam() + " heeft " + punten.get(user) + " punten.");
            reedsGevondenImages.add(idEersteKaart);
            return true;
        }
        return false;
    }

    public boolean voldoendeSpelers() {
        return aantalSpelers == maxAantalSpelers;
    }

    public void setNextBuzzyID() {
        boolean vorigeGepasseerd = false;
        System.out.println("vorige buzzy userID is " + buzzyUserID);

        for (int i : userIDs) {
            if (i == buzzyUserID) {
                vorigeGepasseerd = true;
            } else if (vorigeGepasseerd) {
                buzzyUserID = i;
                System.out.println("next buzzy userID is " + buzzyUserID);
                return;
            }
        }//indien de laatste in rij de vorigeBuzzyUser was beginnen we weer opnieuw:
        buzzyUserID = userIDs.get(0);

        System.out.println("next buzzy userID is " + buzzyUserID);
    }

    public int getPunten(int userId) {
        User user = getUserFromGame(userId);
        return punten.get(user);
    }

    public String getWinner() {
        Map.Entry<User, Integer> maxEntry = null;

        for (Map.Entry<User, Integer> entry : punten.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return "De winnaar is " + maxEntry.getKey().getNaam() + " met " + maxEntry.getValue() + " punten!";
    }

    public boolean addCardToMove(int col, int row, int index){
        return moves.get(index).addCardToMove(col, row);
    }

    public void resetMove(){
        moves.add(new Move());
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getServerId() {
        return serverId;
    }

    public List<Integer> getUserIDs() {
        return userIDs;
    }
}
