package ua.goit.gojava.servicePackage;

/**
 * Created by admin on 23.03.2016.
 */
public interface Observable {

    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
