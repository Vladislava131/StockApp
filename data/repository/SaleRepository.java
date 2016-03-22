package stockapp.data.repository;

import java.lang.reflect.Field;
import stockapp.data.entity.Sale;
import java.util.ArrayList;


public class SaleRepository implements Repository<Sale>
{
    protected String fileWithData;
    protected DataSource dataSource = null;
    protected ArrayList<Sale> sales = null;
    
    public SaleRepository(String fileWithData)
    {
        this.fileWithData = fileWithData;
        this.dataSource = new DataSource(this.fileWithData);
        this.sales = convertStringArrayToEntityArray(this.dataSource.readData());
    }
    
    private ArrayList<Sale> convertStringArrayToEntityArray(ArrayList<String> in)
    {
        ArrayList<Sale> out = new ArrayList<>();
        for(String line : in)
        {
            String[] lineParts = line.split(";");
            int neededCountElementsForEntity = Sale.class.getDeclaredFields().length + 1;//+1 потому что getDeclaredFields невозвращает унаследованных полей
            if(neededCountElementsForEntity == lineParts.length)
            {
                Sale sale = new Sale();
                sale.setId(Integer.parseInt(lineParts[0]));
                sale.setSaleDate(lineParts[1]);
                sale.setItemId(Integer.parseInt(lineParts[2]));
                sale.setItemCount(Integer.parseInt(lineParts[3]));
                out.add(sale);
            }
        }
        return out;
    }
    
    private ArrayList<String> convertEntityArrayToString(ArrayList<Sale> in)
    {
        ArrayList<String> out = new ArrayList<>();
        for(Sale sale : in)
        {
            out.add(sale.toString());
        }
        return out;
    }
    
    private int getItemPositionById(int id)
    {
        int i = 0;
        for(Sale sale : this.sales)
        {
            if(sale.getId() == id)
            {
                return i;
            }
            i++;
        }
        return -1;
    }
    
    private Sale findById(int id)
    {
        int itemPosition = getItemPositionById(id);
        if(itemPosition >= 0)
        {
            return this.sales.get(itemPosition);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public ArrayList<Sale> getAll() 
    {
        return this.sales;
    }
    
    @Override
    public Sale getById(int id) 
    {
        return findById(id);
    }

    @Override
    public void persist(Sale sale) 
    {
        int itemPosition = getItemPositionById(sale.getId());
        if(itemPosition >= 0)
        {
            this.sales.set(itemPosition, sale);
            this.dataSource.writeData(convertEntityArrayToString(this.sales));
        }
        else
        {
            this.sales.add(sale);
            this.dataSource.writeData(convertEntityArrayToString(this.sales));
        }
    }

    @Override
    public void delete(Sale entity) 
    {
        this.sales.remove(entity);
        this.dataSource.writeData(convertEntityArrayToString(this.sales));
    }
    
    public void printSalesTable()
    {
        String tableHeader = String.format("%2s|%5s|","#", "id");
        
        for(Field field : Sale.class.getDeclaredFields())
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
        
        for(Sale sale : this.sales)
        {
            numberInOrder++;
            tableBody += String.format("%2s|%5s|%20s|%20s|%20s|\n", numberInOrder, sale.getId(), sale.getSaleDate(), sale.getItemId(), sale.getItemCount());
        }
        System.out.println(tableBody);
    }
}