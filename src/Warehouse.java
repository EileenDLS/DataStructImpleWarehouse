import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Warehouse implements IWarehouse {
    // warehouse: Map<ToteID, Tote>   ToteID: max=100
    HashMap<Integer, Tote> warehouse = new HashMap<>();
    // tag for warehouse: Map<UPC, toteIDList>
    HashMap<String, Stack<Integer>> tag = new HashMap<>();
    private static final int WAREHOUSE_CAPACITY = 100;
    private static final int TOTE_CAPACITY = 10;

    @Override
    public void LoadProducts(String filename) {
        System.out.println("Loading products...");
        try {
            // get data from file
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while (reader.ready()) {
                String line = reader.readLine();
                StringTokenizer input = new StringTokenizer(line, ",");
                // set UPC and description for a product object
                Product product = new Product();
                product.setUPC(input.nextToken());
                product.setDescription(input.nextToken());
                // add current product into the responding tote, and put the tote to warehouse
                for (int i = 0; i < WAREHOUSE_CAPACITY; i++) {
                    Tote curTote = warehouse.get(i);
                    // case1: in warehouse, current position no tote, new a tote
                    // case2: current tote is full, new another tote
                    if (curTote == null) {
                        Tote tote = new Tote();
                        warehouse.put(i, tote);
                        tote.setUPC(product.getUPC());
                        tote.productList.offer(product);
                        break;
                    } else if (curTote.getUPC().equals(product.getUPC()) && curTote.productList.size() < TOTE_CAPACITY) {
                        // current product UPC equals current Tote's UPC and this tote doesn't full --> put this product into it
                        curTote.productList.offer(product);
                        break;
                    }
                    // current tote not full but current product doesn't belong it, check next tote
                }
            }
            for (Map.Entry<Integer, Tote> entry : warehouse.entrySet()) {
                System.out.printf("Used additional tote (%d) for UPC: %s %n", entry.getKey(), entry.getValue().getUPC());

                setTag(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loading complete.");
    }

    // set <UPC, toteIDList> to tag
    public void setTag(Map.Entry<Integer, Tote> entry){
        String curToteUPC = entry.getValue().getUPC();
        if (tag.get(curToteUPC) == null) {  // current toteList is null, new a toteList to current tag
            Stack<Integer> toteList = new Stack<>();
            toteList.push(entry.getKey());
            tag.put(curToteUPC, toteList);
        } else if (tag.containsKey(curToteUPC)) {  // if UPC is same, put current toteID into current totoList
            tag.get(curToteUPC).push(entry.getKey());
        }
    }

    @Override
    public void FulfillOrders(String filename) {
        System.out.println("\nFulfilling orders...");
        try {
            // get data from file
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while (reader.ready()) {
                String line = reader.readLine();
                StringTokenizer input = new StringTokenizer(line, ",");
                String orderID = input.nextToken();
                System.out.println("\nOrder fulfillment started: Order " + orderID);

                String output = "Order fulfilled--> Order " + orderID;
                while (input.hasMoreTokens()) {
                    String curUPC = input.nextToken().trim();  // get current UPC
                    Stack<Integer> totesList = tag.get(curUPC); // get responding toteIDList
                    // pick up product from tote randomly
                    Random random = new Random();
                    int index = random.nextInt(totesList.size() - 1);
                    int toteID = totesList.get(index);
                    System.out.printf("Retrieving product from tote (%d) for UPC: %s%n", toteID, curUPC);
                    warehouse.get(toteID).productList.poll();

                    output += ", " + curUPC;
                }
                System.out.println(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean MergeTotes() {
        System.out.println("\nMerging partially filled totes...");
        for (Map.Entry<Integer, Tote> entry : warehouse.entrySet()) {
            Tote curTote = entry.getValue();
            // calculate the number of missing products
            int lack = TOTE_CAPACITY - curTote.productList.size();
            helperMerge(lack, curTote);
        }
        System.out.println("Merge complete.");
        return true;
    }

    // marge helper function: choose the last tote of toteIDList to fill up others which is not full
    // check last one, if last one is empty, remove it from toteIDList
    public void helperMerge(int lack, Tote curTote) {
        while (lack != 0) { // current tote needs to be filled
            String curUPC = curTote.getUPC();
            int lastID = tag.get(curUPC).peek();
            Tote lastTote = warehouse.get(lastID);
            Product product = lastTote.productList.poll();
            curTote.productList.offer(product);

            if (lastTote.productList.size() == 0){
                tag.get(curUPC).pop();
            }

            lack--;
        }
    }

    @Override
    public void DisplayDetails() {
        System.out.println("\nWarehouse details:");
        // count the number of full tote and not full tote
        int full = 0, notFull = 0;
        for (Map.Entry<Integer, Tote> entry : warehouse.entrySet()) {
            Tote curTote = entry.getValue();
            if (curTote.productList.size() == TOTE_CAPACITY) { // tote is full
                full++;
            } else if (curTote.productList.size() != 0){ // tote isn't full but not empty
                notFull++;
            }
        }
        int remain = 100 - (full + notFull);
        System.out.printf("%d full totes, %d partially filled totes, and %d empty totes%n", full, notFull, remain);
        System.out.println("UPC             # Totes");
        System.out.println("--------------- -------");
        for (Map.Entry<String, Stack<Integer>> entry : tag.entrySet()) {
            System.out.println(entry.getKey() + "          " + entry.getValue().size());
        }
    }
}