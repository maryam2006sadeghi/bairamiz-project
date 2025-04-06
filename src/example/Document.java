package example;

import db.Entity;
import db.Trackable;

import java.util.Date;

public class Document extends Entity implements Trackable {
    public static final int DOCUMENT_ENTITY_CODE = 15;
    public String content;
    private Date creationDate;
    private Date lastModificationDate;

    public Document(String content){
        this.content = content;
    }

    @Override
    public Document copy() {
        Document copyDocument = new Document(content);
        copyDocument.id = id;

        if (this.creationDate != null) {
            copyDocument.creationDate = new Date(this.creationDate.getTime());
        }

        if (this.lastModificationDate != null) {
            copyDocument.lastModificationDate = new Date(this.lastModificationDate.getTime());
        }

        return copyDocument;
    }

    @Override
    public int getEntityCode() {
        return DOCUMENT_ENTITY_CODE;
    }

    @Override
    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setLastModificationDate(Date date) {
        this.lastModificationDate = date;
    }

    @Override
    public Date getLastModificationDate() {
        return lastModificationDate;
    }
}
