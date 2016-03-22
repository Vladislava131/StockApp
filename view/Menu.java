package stockapp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;


public class Menu 
{
    private Menu parentMenu;
    private final String menuTitle;
    private HashMap<String, Method> options;
    
    public Menu(String menuTitle)
    {
        this.menuTitle = menuTitle;
        this.parentMenu = null;
        this.options = new HashMap();
    }
    public Menu(String menuTitle, Menu parentMenu)
    {
        this.menuTitle = menuTitle;
        this.parentMenu = parentMenu;
        this.options = new HashMap();
    }
    
    public void addOption(String title, Method methodToInvoke)
    {
        this.options.put(title, methodToInvoke);
    }
    public void removeOption(String title)
    {
        this.options.remove(title);
    }
    
    public int show()
    {
        for(int i = 0; i < this.menuTitle.length() + 4; ++i)
        {
            System.out.print("-");
        }
        System.out.println();
        System.out.println("| " + this.menuTitle + " |");
        for(int i = 0; i < this.menuTitle.length() + 4; ++i)
        {
            System.out.print("-");
        }
        System.out.println();
        
        int i = 0;
        for(String key: this.options.keySet())
        {
            System.out.println((++i) + " - " + key);
        }
        System.out.println();
        System.out.print("Выберите пункт меню(введите 0 для выхода):");
        
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            String userChoice = input.readLine().trim();
            if(Integer.parseInt(userChoice) != 0)
            {
                int userChoiceValue = Integer.parseInt(userChoice);
                int countMenuOptions = this.options.size();
                if(userChoiceValue <= countMenuOptions)
                {
                    if(this.options.values().toArray()[userChoiceValue - 1] instanceof Method)
                    {
                        Method methodToInvoke = (Method)this.options.values().toArray()[userChoiceValue - 1];
                        int returnValue = (int)methodToInvoke.invoke(null);
                        return returnValue;
                    }
                }
                return 0;
            }
            else
            {
                return 0;
            }
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return 0;
        }
    }
}