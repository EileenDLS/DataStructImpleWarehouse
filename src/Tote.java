import java.util.LinkedList;
import java.util.Queue;

public class Tote {
    String UPC;
    Queue<Product> productList = new LinkedList<>();
    public void setUPC(String UPC) {
        this.UPC = UPC;
    }
    public String getUPC() {
        return UPC;
    }

}
