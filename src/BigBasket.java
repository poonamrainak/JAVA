import java.util.Scanner;

public class BigBasket {
	
	public static void masterPageData()
	{
		System.out.println("*****************Welcome to Big Basket*****************");
		System.out.println(" Press 1 for Registration.");
		System.out.println(" Press 2 for Product Details.");
		System.out.println(" Press 3 for User Details.");
		System.out.println(" Press 4 for Product Stock.");
		System.out.println(" Press 5 for User History.");
		System.out.println();
		System.out.println(" Please enter your choice.");

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char ch;
		
	do
		{
		masterPageData();
		
		Scanner  sc = new Scanner(System.in);
		int choice = sc.nextInt();
		
//		if(choice==1)
//		{
//			UserData.getUserData();
//		
//		}
//		if(choice==2)
//		{
//		int userid=	UserData.checkRegisterUser();
//		if(userid>0)
//		{
//			ProductData.showProduct(userid);
//			
//		}else
//		{
//			System.out.println("Register please");
//		}
//			
//		
//		}
//		
//		if(choice==4)
//		{
//			AdminData.checkStock();
//		}
//		
//		if(choice==3)
//		{
//			AdminData.userInfo();
//		}
//		if(choice==5) {
//				AdminData.userWisePurchaseHistory();
//		}
		
		
		switch(choice){  
	    //Case statements  
	    case 1: UserData.getUserData();
	            break;  
	    case 2: int userid=	UserData.checkRegisterUser();
	        	if(userid>0)
	        	{
			     ProductData.showProduct(userid);
		 	
	        	}else
	        	{
		        	System.out.println("Register please");
	        	}
			 
	             break;  
	    case 3: AdminData.userInfo(); 
	            break;  
	    case 4: AdminData.checkStock(); 
                break;
	    case 5: AdminData.userWisePurchaseHistory();

                break;

	    //Default case statement  
	    default:System.out.println(".......Invalid Choice......");  
		}
        System.out.println(" Do u want to go to Main Menu :");
        System.out.println(" Press Y to go to Main Menu");
        System.out.println(" Press N to go to Main Menu");
	    sc= new Scanner(System.in);
	    ch=Character.toUpperCase(sc.next().charAt(0));
		}while(ch!='N');

	}

}
