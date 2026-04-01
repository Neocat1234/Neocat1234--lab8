public class TooSmallText extends Exception {
    public TooSmallText(String message) {
        super(message);
    }

    
    public String toString() {
        return getMessage();
    }
}
