package nl.sugcube.crystalquest.util;

/**
 * @author SugarCaney
 */
public class CrystalQuestException extends RuntimeException {

    public CrystalQuestException() {
        super();
    }

    public CrystalQuestException(String message) {
        super(message);
    }

    public CrystalQuestException(Throwable cause) {
        super(cause);
    }

    public CrystalQuestException(String message, Throwable cause) {
        super(message, cause);
    }
}
