package stockapp.data.repository;

import java.lang.reflect.Field;
import stockapp.data.entity.Stock;
import java.util.ArrayList;


public class StockRepository implements Repository<Stock>
{
    protected String fileWithData;
    protected DataSource dataSource = null;
    protected ArrayList<Stock> stock = null;
    
    public StockRepository(String fileWithData)
    {
        this.fileWithData = fileWithData;
        this.dataSource = new DataSource(this.fileWithData);
        this.stock = convertStringArrayToEntityArray(this.dataSource.readData());
    }
    
    private ArrayList<Stock> convertStringArrayToEntityArray(ArrayList<String> in)
    {
        ArrayList<Stock> out = new ArrayList<>();
        for(String line : in)
        {
            String[] lineParts = line.split(";");
            int neededCountElementsForEntity = Stock.class.getDeclaredFields().length + 1;
            if(neededCountElementsForEntity == lineParts.length)
            {
                Stock stockItem = new Stock();
                stockItem.setId(Integer.parseInt(lineParts[0]));
                stockItem.setItemName(lineParts[1]);
                stockItem.setItemPrice(Double.parseDouble(lineParts[2].replace(",", ".")));
                stockItem.setItemCount(Integer.parseInt(lineParts[3]));
                out.add(stockItem);
            }
        }
        return out;
    }
    
    private ArrayList<String> convertEntityArrayToString(ArrayList<Stock> in)
    {
        ArrayList<String> out = new ArrayList<>();
        for(Stock stockItem : in)
        {
            out.add(stockItem.toString());
        }
        return out;
    }
    
    private int getItemPositionById(int id)
    {
        int i = 0;
        for(Stock stockItem : this.stock)
        {
            if(stockItem.getId() == id)
            {
                return i;
            }
            i++;
        }
        return -1;
    }
    
    private Stock findById(int id)
    {
        int itemPosition = getItemPositionById(id);
        if(itemPosition >= 0)
        {
            return this.stock.get(itemPosition);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public ArrayList<Stock> getAll() 
    {
        return this.stock;
    }
    
    @Override
    public Stock getById(int id) 
    {
        return findById(id);
    }

    @Override
    public void persist(Stock stock) 
    {
        int itemPosition = getItemPositionById(stock.getId());
        if(itemPosition >= 0)
        {
            this.stock.set(itemPosition, stock);
            this.dataSource.writeData(convertEntityArrayToString(this.stock));
        }
        else
        {
            this.stock.add(stock);
            this.dataSource.writeData(convertEntityArrayToString(this.stock));
        }
    }

    @Override
    public void delete(Stock entity) 
    {
        this.stock.remove(entity);
        this.dataSource.writeData(convertEntityArrayToString(this.stock));
    }

    public void printStockTable()
    {
        String tableHeader = String.format("%2s|%5s|","#", "id");
        
        for(Field field : Stock.class.getDeclaredFields())
        {
           tableHeader += String.format("%20s|", field.getName());
        }
        System.out.println(tableHeader);
        
        for(int i = 0; i < tableHeader.length(); ++i)
        {
            System.out.print("-");
        }
        System.out.println();
        
        String tableBody = "";
        int numberInOrder = 0;
        
        for(Stock stock : this.stock)
        {
            numberInOrder++;
            tableBody += String.format("%2s|%5s|%20s|%20s|%20s|\n", numberInOrder, stock.getId(), stock.getItemName(), stock.getItemPrice(), stock.getItemCount());
        }
        System.out.println(tableBody);
    }
    
    public boolean enoughItemsInStock(int itemId, int countItems)
    {
        Stock stock = findById(itemId);
        if(stock != null)
        {
            if(stock.getItemCount() >= countItems)
                return true;
            else 
                return false;
        }
        else
        {
            return false;
        }
    }
 
    public void changeStockItemsCount(int itemId, int countToChange)
    {
        Stock stock = findById(itemId);
        int oldCount = stock.getItemCount();
        stock.setItemCount(oldCount + countToChange);
        persist(stock);
    }
}