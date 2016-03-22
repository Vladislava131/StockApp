package stockapp.data.repository;

import java.util.ArrayList;

public interface Repository<E> 
{
    public E getById(int id);
    public ArrayList<E> getAll();
    public void persist(E entity);
    public void delete(E entity);
}