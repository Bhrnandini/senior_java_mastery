package chapter1;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Order {
    private int orderId;
    private String status;

    public Order(int orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId && Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, status);
    }
}

public class HashingHashMapDemo {

    public static void main(String[] args) {
        Map<Order, String> warehouseMap = new HashMap<>();

        Order o1 = new Order(500, "PENDING");
        warehouseMap.put(o1, "Aisle 4");

// Later in the workflow, the order ships:
        o1.setStatus("SHIPPED");

// Now your code runs these three checks:
        System.out.println("Check 1: " + warehouseMap.get(o1));
        System.out.println("Check 2: " + warehouseMap.get(new Order(500, "PENDING")));
        System.out.println("Check 3: " + warehouseMap.size());
    }
}


/*
Reviewing Your Answers
1. Check 1 (warehouseMap.get(o1)) -> null (You nailed it!)
When o1 was first inserted, its hash was computed from (500, "PENDING")—let's say Bucket #12.

After o1.setStatus("SHIPPED"), get(o1) computes a brand-new hash from (500, "SHIPPED")—let's say Bucket #88.

Java looks in Bucket #88, finds it completely empty, and returns null.

2. Check 2 (warehouseMap.get(new Order(500, "PENDING"))) -> null (The Hidden Trap!)
Here is what happens step-by-step when you pass new Order(500, "PENDING"):

Step 1 (hashCode): Java hashes (500, "PENDING") and gets Bucket #12. It jumps straight to Bucket #12, and yes, o1 is sitting right there!

Step 2 (equals): Now Java has to confirm the key in Bucket #12 actually matches what you asked for. It runs:

Java
existingKey.equals(searchKey)
Remember, existingKey is the exact same o1 object reference in heap memory, and its status field was mutated to "SHIPPED"!
So Java compares o1 (500, "SHIPPED") against new Order(500, "PENDING").
"SHIPPED".equals("PENDING") is false!

Because equals() returns false, HashMap assumes this was just a hash collision with an unrelated object and returns null!

The result: Neither o1 nor new Order(500, "PENDING") can ever retrieve "Aisle 4" again. You can't even call warehouseMap.remove(o1)—that entry is now a permanent memory leak inside the map until the map itself is garbage-collected or cleared.

3. Check 3 (warehouseMap.size()) -> 1 (You nailed it!)
Even though the entry is completely unreachable by .get() or .remove(), the Node is still physically sitting inside Bucket #12, so .size() still reports 1.
 */
