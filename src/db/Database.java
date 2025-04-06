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
        entities.add(e.copy());
        createId++;
    }

    public static Entity get(int id) throws EntityNotFoundException{
        for (Entity e : entities){
            if (e.id == id)
                return e.copy();
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
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                entities.set(i, e.copy());
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }
}
