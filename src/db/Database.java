package db;

import db.exception.EntityNotFoundException;

import java.util.ArrayList;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static int createId = 1;

    private Database() {
        throw new AssertionError(" امکان ساخت instance وجود ندارد ");
    }

    public static void add(Entity e){
        e.id = createId;
        entities.add(e);
        createId++ ;
    }

    public static Entity get(int id) throws EntityNotFoundException{
        for (Entity e : entities){
            if (e.id == id)
                return e;
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException {
        for (Entity e : entities){
            if (e.id == id) {
                entities.remove(e);
                return;
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void update(Entity e) throws EntityNotFoundException{
        for (Entity entity : entities){
            if (e.id == entity.id){
                entity = e;
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }
}
