import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ProductData {
	
	
	public static void updateStock(Map<String,Integer> items ,LinkedHashMap<String,ProductModel> productMap)
	{
		 try
	    {
		Connection con=ConnectionDetail.getConnection();
		PreparedStatement pstmt = con.prepareStatement("update product set quantity= ? WHERE productName =?");
		
		 for (Map.Entry<String,Integer> item : items.entrySet())
		 {
			    ProductModel productModelObject = productMap.get(item.getKey());
			    int stock = productModelObject.getQuantity();
 			    pstmt.setInt(1, stock);
			    pstmt.setString(2, item.getKey());
				int i = pstmt.executeUpdate();
		 }
			}
		 catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}

		
	}
	public static void saveOrderDetails(Map<String,Integer> items,LinkedHashMap<String,ProductModel> productMap, int userid)
	{
		float grandTotal=0.0f;
		Map<Integer,InventoryModel> inventorys = new HashMap<Integer,InventoryModel>();
		 for (Map.Entry<String,Integer> item : items.entrySet())
		 {
			 InventoryModel inventoryModel = new InventoryModel();
			 ProductModel pmodel = productMap.get(item.getKey());
			 inventoryModel.setProductId(pmodel.getProductId());
			 inventoryModel.setProductName(item.getKey());
			 inventoryModel.setNoOfItems(item.getValue());
			 inventoryModel.setPrice(pmodel.getPrice());
			 float subTotal = pmodel.getPrice()*item.getValue();
			 inventoryModel.setSubTotal(subTotal);
    		 grandTotal=grandTotal+subTotal;
    		 
    		 
    		 inventorys.put(pmodel.getProductId(), inventoryModel);
//    		 System.out.println("quantity"+item.getValue());
//    		 System.out.println("price"+pmodel.getPrice());
//			 System.out.println("subTotal"+subTotal);
//			 System.out.println("grandTotal"+grandTotal);
		
		 }
		Connection con=ConnectionDetail.getConnection();
		
		try {
			PreparedStatement stmt =con.prepareStatement("insert into documentorder(userid,date,totalAmount)values(?,?,?)");
			Date date = Calendar.getInstance().getTime();
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	        String today = formatter.format(date);
			stmt.setInt(1,userid);
	    	stmt.setString(2,today);
	 		stmt.setFloat(3,grandTotal);
	 		
	 		int j = stmt.executeUpdate();
			
			System.out.println("Record is inserted."+ j);
	 		//con.close();
			stmt.close();
			
			
			PreparedStatement ostmt = con.prepareStatement("SELECT orderid FROM documentorder WHERE userid = ? and date =? and totalAmount=?");
			ostmt.setInt(1, userid);
			ostmt.setString(2, today);
			ostmt.setFloat(3, grandTotal);

			ResultSet rs = ostmt.executeQuery();
			rs.next();
			int orderid =rs.getInt(1);
			ostmt.close();
			
			 for (Map.Entry<Integer,InventoryModel> inv : inventorys.entrySet())
			 {
				    InventoryModel invmodel = inv.getValue();
					PreparedStatement pstmt =con.prepareStatement("insert into inventory(orderid,productid,noOfItems,subTotal,price\r\n" + 
							")values(?,?,?,?,?)");
					pstmt.setInt(1,orderid);
					pstmt.setInt(2,inv.getKey());
					pstmt.setInt(3,invmodel.getNoOfItems());
			    	pstmt.setFloat(4, invmodel.getSubTotal());
			 		pstmt.setFloat(5,invmodel.getPrice());
			 		//System.out.println("order id"+orderid);
			 		int k = pstmt.executeUpdate();
			 		pstmt.close();

			 		
			 }

			
			 con.close();
		 					
			
			
			 invoice(inventorys);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	public static void invoice(Map<Integer,InventoryModel> inventorys) {
	    float grandTotal=0.0f;
		System.out.println(".....................................Invoice Detail...........................................");
	    System.out.println("===============================================================================================");
		System.out.println(".......Product Name .......Quantity....Price....Amount   ..................");
		
		for(Map.Entry<Integer,InventoryModel> inv:inventorys.entrySet())
        {	
			System.out.println("...........................................................................................");
			InventoryModel invmodel = inv.getValue();
			grandTotal=grandTotal+invmodel.getSubTotal();

    		System.out.println("\t"+invmodel.getProductName()+"\t"+invmodel.getNoOfItems()+"\t"+invmodel.getPrice()+"\t"+invmodel.getSubTotal());
		}
		 System.out.println("===============================================================================================");
		 System.out.println(" Grand Total  :"+grandTotal);
		 
		 System.out.println(" Thank You  ");
		 
	}
	
	
	
	
	
	public static void ConfirmOrder(Map<String,Integer> items,LinkedHashMap<String,ProductModel> productMap, int userid)
	{
		System.out.println(" Do you want to confirm your order?");
		Scanner sc= new Scanner(System.in); 
		char confirm  =  Character.toUpperCase(sc.next().charAt(0));
		
		if(confirm=='Y')
		{
			updateStock(items,productMap);
			saveOrderDetails(items,productMap,userid);
		}
	}
	
	public static void addCart(LinkedHashMap<String,ProductModel> productMap, int userid)
	{
		
		
		Map<String,Integer> items = new HashMap<String,Integer>();
		 char a ;
		 
		 try {
		
		do
		{
		    System.out.println(" Enter the item do you want to purchase");
		    Scanner sc= new Scanner(System.in);
	    	String itemName = sc.nextLine();
	    	if(productMap.containsKey(itemName))
     		{
	         	System.out.println(" Enter the item quantity do you want to purchase");
		        int quantity = sc.nextInt();
		        ProductModel pobject = productMap.get(itemName);
		        int stock =pobject.getQuantity();
		        if(quantity<=stock)
		        {
	     		if(items.containsKey(itemName))
		    	{
	     	    stock=stock-quantity;
	     		    
		   		quantity=items.get(itemName)+quantity;
		   		//items.put(itemName,quantity);
			     //  stock=stock-quantity;		   		
		    	}
			 else
			   {
		      
		       stock=stock-quantity;
		    
		    	}
	     		 items.put(itemName,quantity);
	     	   pobject.setQuantity(stock);
		       productMap.put(itemName, pobject);
		      }
	           else
	        	{
		      System.out.println(itemName+ " item is out of stock");
	        	}
		        
     		}
	    	else
	    	{
	    		 System.out.println(itemName+ " item is not present in big basket");
	    	}
		     System.out.println(" Do u want to purchase more product press y for purchasing another item");
		     sc= new Scanner(System.in);
             a=Character.toUpperCase(sc.next().charAt(0));
     
		}
		while((a)=='Y');
		ConfirmOrder(items,productMap,userid);
			
		 }catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
		
	}
	
	public static void showProduct(int userid)
	{
		Connection con=ConnectionDetail.getConnection();
		
		Statement stmt;
	//
		LinkedHashMap<String,ProductModel> productMap = new LinkedHashMap<String,ProductModel>();
		
		try {
			stmt = con.createStatement();
	
		ResultSet rs = stmt.executeQuery("select * from product order by productname");
		
		while(rs.next()) {
			ProductModel pmodel= new ProductModel();
			pmodel.setProductId(rs.getInt(1));
			pmodel.setProductName(rs.getString(2));
			pmodel.setDescription(rs.getString(3));
			pmodel.setPrice(rs.getFloat(4));
			pmodel.setQuantity(rs.getInt(5));
			productMap.put(rs.getString(2), pmodel);
		}
		 
		
		con.close();
		
//		System.out.println("==================================.Product List=====================================");
//		System.out.println(" product name            product description            price              quantity");
//		 for (Map.Entry<String,ProductModel> entry : productMap.entrySet())  
//		 {
//			 ProductModel pmodel= entry.getValue();
//			 System.out.println( pmodel.getProductName()+"                " + pmodel.getDescription() +"             "+pmodel.getPrice()+"          "+pmodel.getQuantity());
//			 System.out.println("......................................................................");
//
//		 }
		 
		String tabFormat = "\t\t";
			System.out.println("==================================.Product List=====================================");
			System.out.println(" product name            product description            price              quantity");
			 for (Map.Entry<String,ProductModel> entry : productMap.entrySet())  
			 {
				 ProductModel pmodel= entry.getValue();
				 System.out.println( pmodel.getProductName() + tabFormat + pmodel.getDescription() + tabFormat + pmodel.getPrice() + tabFormat + pmodel.getQuantity());
				 System.out.println("......................................................................");

			 }
		 
		 
		 
		 
		 
		 addCart(productMap,userid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
