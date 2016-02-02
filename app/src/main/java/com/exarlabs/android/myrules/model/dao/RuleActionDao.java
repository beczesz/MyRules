package com.exarlabs.android.myrules.model.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.exarlabs.android.myrules.model.dao.RuleAction;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RULE_ACTION".
*/
public class RuleActionDao extends AbstractDao<RuleAction, Long> {

    public static final String TABLENAME = "RULE_ACTION";

    /**
     * Properties of entity RuleAction.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Type = new Property(1, int.class, "type", false, "TYPE");
        public final static Property ActionName = new Property(2, String.class, "actionName", false, "ACTION_NAME");
    };

    private DaoSession daoSession;


    public RuleActionDao(DaoConfig config) {
        super(config);
    }
    
    public RuleActionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RULE_ACTION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TYPE\" INTEGER NOT NULL ," + // 1: type
                "\"ACTION_NAME\" TEXT NOT NULL );"); // 2: actionName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RULE_ACTION\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RuleAction entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getType());
        stmt.bindString(3, entity.getActionName());
    }

    @Override
    protected void attachEntity(RuleAction entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public RuleAction readEntity(Cursor cursor, int offset) {
        RuleAction entity = new RuleAction( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // type
            cursor.getString(offset + 2) // actionName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RuleAction entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setType(cursor.getInt(offset + 1));
        entity.setActionName(cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(RuleAction entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(RuleAction entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
