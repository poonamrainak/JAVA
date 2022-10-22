import java.util.LinkedList;

public class OrderModel {
	
	private int orderNo ;
	private float grandTotal;
	private String orderDate;
	private LinkedList<InventoryModel> itemModel;
	
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public float getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(float grandTotal) {
		this.grandTotal = grandTotal;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public LinkedList<InventoryModel> getItemModel() {
		return itemModel;
	}
	public void setItemModel(LinkedList<InventoryModel> itemModel) {
		this.itemModel = itemModel;
	}
	

}
