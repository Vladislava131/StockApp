package stockapp.data.entity;

/**
 * Описание Склад, где хранятся различные товары
 */

public class Stock extends AEntity
{
    protected String itemName;
    protected double itemPrice;
    protected int itemCount;
    
    public String getItemName()
    {
        return this.itemName;
    }
    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }
    
    public double getItemPrice()
    {
        return this.itemPrice;
    }
    public void setItemPrice(double itemPrice)
    {
        this.itemPrice = itemPrice;
    }
    
    public int getItemCount()
    {
        return this.itemCount;
    }
    public void setItemCount(int itemCount)
    {
        this.itemCount = itemCount;
    }
    
    @Override
    public String toString()
    {
        return String.format("%d;%s;%f;%d", this.id, this.itemName, this.itemPrice, this.itemCount);
    }
    
}