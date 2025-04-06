package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static int createId = 1;
    private static HashMap<Integer, Validator> validators = new HashMap<>();

    private Database() {
        throw new AssertionError(" امکان ساخت instance وجود ندارد ");
    }

    public static void add(Entity e) throws InvalidEntityException {
        e.id = createId;
        entities.add(e.copy());
        createId++;

        Validator validator = validators.get(e.getEntityCode());
        validator.validate(e);
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

    public static void update(Entity e) throws EntityNotFoundException, InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        validator.validate(e);

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                entities.set(i, e.copy());
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator for this entity code already exists");
        }
        validators.put(entityCode, validator);
    }
}
