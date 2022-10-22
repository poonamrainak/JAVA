import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class AdminData {

	public static void userWisePurchaseHistory()
	{
		LinkedHashMap<Integer,OrderModel>  orders = new LinkedHashMap<Integer,OrderModel>();
		Scanner sc = new Scanner(System.in);
		System.out.println(" Enter username  of which u want to see history");
		String username=sc.nextLine();
		Connection con=ConnectionDetail.getConnection();
		int	userid =0;
		try
		{
		
		PreparedStatement pstmt = con.prepareStatement("SELECT * FROM user WHERE username = ?");
	    pstmt.setString(1, username);
	    ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
		userid = rs.getInt(1);

	//	System.out.println("user id"+i);
		}
		
		//con.close();
		pstmt.close(); 
		
		
//		select a.orderid ,a.date,a.totalAmount,b.productid,c.productName,b.noOfItems,b.price,b.subTotal from documentorder a
//		join inventory b on a.orderid=b.orderid 
//		join product c on c.productid=b.productid
//		where a.userid=2 order by a.orderid; 
		
		
		PreparedStatement ostmt = con.prepareStatement("select a.orderid ,a.date,a.totalAmount,b.productid,c.productName,b.noOfItems,b.price,b.subTotal from documentorder a\r\n" + 
				"join inventory b on a.orderid=b.orderid \r\n" + 
				"join product c on c.productid=b.productid\r\n" + 
				"where a.userid=? order by a.orderid; ");
		ostmt.setInt(1, userid);
	    
	    ResultSet rs1 = ostmt.executeQuery();
	    int  previousorderid=0;
	    LinkedList<InventoryModel> itemModel=null;
	    OrderModel orderModel=null;
		
		while(rs1.next()) {
			
			//System.out.println("userid"+userid);
		
					
			if(previousorderid!=rs1.getInt(1))
			{
			//	System.out.println("id order"+rs1.getInt(1));
				if(previousorderid!=0)
				{
					orderModel.setItemModel(itemModel);
					orders.put(orderModel.getOrderNo(), orderModel);
				////	System.out.println("orderid"+orderModel.getOrderNo());
					
				}
				orderModel = new OrderModel();
				orderModel.setOrderNo(rs1.getInt(1));
				orderModel.setOrderDate(rs1.getString(2));
				orderModel.setGrandTotal(rs1.getFloat(3));

				previousorderid=orderModel.getOrderNo();
			    itemModel = new  LinkedList<InventoryModel>();
			}
			
			
			if(previousorderid==orderModel.getOrderNo()) {
			
				InventoryModel invModel = new InventoryModel();
				invModel.setProductId(rs1.getInt(4));
				invModel.setProductName(rs1.getString(5));
				invModel.setNoOfItems(rs1.getInt(6));
				invModel.setPrice(rs1.getFloat(7));
				invModel.setSubTotal(rs1.getFloat(8));
				itemModel.add(invModel);
			}
			//orderModel.setItemModel(itemModel);
		}
		if(previousorderid!=0)
		{
			orderModel.setItemModel(itemModel);
			orders.put(orderModel.getOrderNo(), orderModel);
			//System.out.println("orderid"+orderModel.getOrderNo());
			
		}
		
		Set<Map.Entry<Integer, OrderModel>> ordersSet = orders.entrySet();
		
		System.out.println("==========================User History====================================");

		for(Map.Entry<Integer,OrderModel> or : ordersSet)
		{
			System.out.println(" Order No :"+or.getKey());
			
			//System.out.println(" ....................................................................");
			//System.out.println(" ....................................................................");
			System.out.println("*************************************************************************");
			OrderModel orderMod = or.getValue();
			 LinkedList<InventoryModel> itemMod=orderMod.getItemModel();
			 int srNo=1;
			for(InventoryModel imodel :itemMod)
			{
				System.out.println("\t"+srNo+"    "+imodel.getProductName()+"  "+imodel.getNoOfItems()+"  "+imodel.getPrice()+" "+imodel.getSubTotal());
				System.out.println(" ....................................................................");
				srNo=srNo+1;
			}
			System.out.println("=========================================================================");
			System.out.println("  Grand Total :"+orderMod.getGrandTotal());
			System.out.println("**************************************************************************");
			
		}
		
		
		
	//	System.out.println("user order size"+orders.size());
		
		}
		catch (Exception e) {
			// TODO: handle exceptione.
			e.printStackTrace();
		}

		
		
		
	}
	public static void userInfo()
	{
		List<UserModel> users = new ArrayList<UserModel>();
		Connection con=ConnectionDetail.getConnection();
		try {
			Statement stmt= con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from user order by username");
			while(rs.next()) {
				UserModel userModel = new UserModel();
				userModel.setUsername(rs.getString(3));
				userModel.setCity(rs.getString(5));
				userModel.setMobileNo(rs.getLong(6));
				users.add(userModel);
			}
			System.out.println("================================Registered Users =====================================");
			System.out.println("  UserName                    city              ");
			for(UserModel uModel:users)
			{
				System.out.println("\t"+uModel.getUsername()+"\t"+uModel.getCity());
			}
			con.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void checkStock()
	{
		
		Connection con=ConnectionDetail.getConnection();
		 char choice;
		
		do
		{
		System.out.println(" Please enter product id for stock");		
		Scanner sc = new Scanner(System.in);
		int productId = sc.nextInt();
		
		PreparedStatement ostmt;
		try {
			
		ostmt = con.prepareStatement("SELECT quantity,productName FROM product WHERE productid = ? ");
		ostmt.setInt(1, productId);
		ResultSet rs = ostmt.executeQuery();
		rs.next();
		int stock =rs.getInt(1);
		String productName =rs.getString(2);
		ostmt.close();
		
		if(stock>0)
		{
			System.out.println(" Remaining stock for "+productName+" is "+stock);
		}else
		{
			System.out.println(productName+" is out of stock ");
		}
		
		} 
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   System.out.println(" Do u see stock of other product.");
		   System.out.println(" Press Y to see stock of product.");
		   System.out.println(" Press N to exit");
		    sc= new Scanner(System.in);
		    choice=Character.toUpperCase(sc.next().charAt(0));

		
		}while((choice)!='N');
		
		
	}
}

