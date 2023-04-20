# DataStructImpleWarehouse
Building a data structure to store and retrieve products from an automated warehouse. 
Defining and implementing data structures to emulate a warehouse that contains many totes that contain many products. 
I assume that each tote can hold up to 10 products (and no more) and the warehouse contains 100 totes (and no more).

There is a data file of products that need to load and store into the warehouse. 
Each instance of the same product UPC should be stored in the same tote – unless the tote becomes full and then another tote should be used. 
Each tote only store products of the same UPC. 
Reading the file and place each product appropriately into this warehouse data structure.

There is another file where each line represents an order containing a list of products. 
Needing to retrieve the products for each order from the warehouse and print out the appropriate message to the console. 
If a product exists in multiple totes, please select a tote at random to retrieve a product.

Over time, it is possible to have multiple totes that are half-full with the same products. Therefore, it is important to have a merge process that combines all similar products into the least number of totes thereby freeing up space for other products to be stored in the warehouse. Hence, we need to implement a merge process to handle this.

Think about what classes you want to implement and how each class will store its data. This includes thinking about the performance for storing and retrieving the data. The overall data structure should be able to handle thousands of products. 
