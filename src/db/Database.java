package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.util.ArrayList;
import java.util.Date;
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
        createId++;

        validateEntity(e);

        if (e instanceof Trackable) {
            Trackable trackable = (Trackable) e;
            Date now = new Date();
            trackable.setCreationDate(now);
            trackable.setLastModificationDate(now);
        }

        entities.add(e.copy());
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
        validateEntity(e);

        if (e instanceof Trackable) {
            ((Trackable) e).setLastModificationDate(new Date());
        }

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

    protected static void validateEntity(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }
    }

    public static ArrayList<Entity> getAll(int entityCode) {
        return new ArrayList<>(entities);
    }
}
