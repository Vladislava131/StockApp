package stockapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import stockapp.data.entity.Sale;
import stockapp.data.entity.Stock;
import stockapp.data.repository.SaleRepository;
import stockapp.data.repository.StockRepository;
import stockapp.view.Menu;


public class StockApp 
{
    private static StockRepository stockRepository = new StockRepository("./stock.csv");
    private static SaleRepository saleRepository = new SaleRepository("./sale.csv");
    private static Menu mainMenu = new Menu("Главное меню");
    
    public static int sale()
    {
        try 
        {
            stockRepository.printStockTable();
            
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
            
            while(true)
            {
                System.out.print("Введите id товара:");
                int stockItemId = Integer.parseInt(input.readLine().trim());
                System.out.print("Введите количество товара:");
                int stockItemCnt = Integer.parseInt(input.readLine().trim());

                if(stockRepository.enoughItemsInStock(stockItemId, stockItemCnt))
                {
                    Sale newSale = new Sale();
                    newSale.setId(stockRepository.getAll().size() + 1);
                    newSale.setItemCount(stockItemCnt);
                    newSale.setItemId(stockItemId);
                    newSale.setSaleDate(dateFormatter.format(new Date()));
                    saleRepository.persist(newSale);
                    stockRepository.changeStockItemsCount(stockItemId, -stockItemCnt);
                    
                    saleRepository.printSalesTable();
                }
                else
                {
                    System.out.println("Такого товара нет на складе, или введите меньшее количество");
                }
                
                System.out.print("Продолжить?[y - выйти в главное меню, или любую кнопку что продолжить]:");
                String userChoice = input.readLine().trim();
                if(userChoice.equals("y"))
                    break;
            }
        } 
        catch (Exception e) 
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        return 1;
    }
    
    public static int delivery()
    {
        try 
        {
            stockRepository.printStockTable();
            
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "cp1251"));
            
            while(true)
            {
                System.out.print("Введите id товара:");
                int stockItemId = Integer.parseInt(input.readLine().trim());
                
                Stock stockItem = stockRepository.getById(stockItemId);
                if(stockItem != null)
                {
                    System.out.print("Введите количество товара:");
                    int stockItemCnt = Integer.parseInt(input.readLine().trim());
                    stockRepository.changeStockItemsCount(stockItemId, stockItemCnt);
                }
                else
                {
                    System.out.print("Введите наименование товара:");
                    String stockItemName = input.readLine().trim();
                    
                    System.out.print("Введите количество товара:");
                    int stockItemCnt = Integer.parseInt(input.readLine().trim());
                    
                    System.out.print("Введите цену товара:");
                    double stockItemPrice = Double.parseDouble(input.readLine().trim().replace(",", "."));
                    
                    stockItem = new Stock();
                    stockItem.setId(stockRepository.getAll().size() + 1);
                    stockItem.setItemName(stockItemName);
                    stockItem.setItemPrice(stockItemPrice);
                    stockItem.setItemCount(stockItemCnt);

                    stockRepository.persist(stockItem);
                }
                
                System.out.print("Продолжить?[y - выйти в главное меню, или любую кнопку что продолжить]:");
                String userChoice = input.readLine().trim();
                if(userChoice.equals("y"))
                    break;
            }
        } 
        catch (Exception e) 
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return 2;
    }
    
    
    public static int report()
    {
        try
        {
            System.out.println("Текущие продажи:");
            String tableHeader = String.format("%2s|%5s|%10s|","#", "id", "summ");
            System.out.println(tableHeader);
            for(int i = 0; i < tableHeader.length(); ++i)
            {
                System.out.print("-");
            }
            System.out.println();
            
            String tableBody = "";
            int numberInOrder = 0;
            double itemPrice = 0.0;
            
            for(Sale sale : saleRepository.getAll())
            {
                numberInOrder++;
                itemPrice = stockRepository.getById(sale.getItemId()).getItemPrice();
                tableBody += String.format("%2s|%5s|%10s|\n", numberInOrder, sale.getId(), sale.getItemCount() * itemPrice);
            }
            System.out.println(tableBody);
            
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            
            while(true)
            {
                System.out.print("Введите id продажи для информации:");
                int saleId = Integer.parseInt(input.readLine().trim());
                
                Sale sale = saleRepository.getById(saleId);
                if(sale != null)
                {
                    Stock stockItem = stockRepository.getById(sale.getItemId());
                    System.out.println(" Дата сделки: " + sale.getSaleDate());
                    System.out.println(" Наименование товара: " + stockItem.getItemName());
                    System.out.println(" Кол-во товара в сделки: " + sale.getItemCount());
                    System.out.println(" Цена ед. товара: " + stockItem.getItemPrice());
                    System.out.println(" Осталось на складе: " + stockItem.getItemCount());
                }
                else
                {
                    System.out.println("Такой продажи нет");
                }
                
                System.out.print("Продолжить?[y - выйти в главное меню, или любую кнопку что продолжить]:");
                String userChoice = input.readLine().trim();
                if(userChoice.equals("y"))
                    break;
            }
            
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        return 3;
    }
    
    public static void main(String[] args) 
    {
        try 
        {
            mainMenu.addOption("Продажа товара", StockApp.class.getMethod("sale"));
            mainMenu.addOption("Поставка товара", StockApp.class.getMethod("delivery"));
            mainMenu.addOption("Данные по продажам", StockApp.class.getMethod("report"));
            
            int stopKey = -1;
            while(stopKey != 0)
            {
                stopKey = mainMenu.show();
            }
        } 
        catch (Exception e) 
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
}
