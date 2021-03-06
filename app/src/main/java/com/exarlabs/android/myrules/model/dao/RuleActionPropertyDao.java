package com.exarlabs.android.myrules.model.dao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.exarlabs.android.myrules.model.dao.RuleActionProperty;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RULE_ACTION_PROPERTY".
*/
public class RuleActionPropertyDao extends AbstractDao<RuleActionProperty, Long> {

    public static final String TABLENAME = "RULE_ACTION_PROPERTY";

    /**
     * Properties of entity RuleActionProperty.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Key = new Property(1, String.class, "key", false, "KEY");
        public final static Property Value = new Property(2, String.class, "value", false, "VALUE");
        public final static Property ActionId = new Property(3, Long.class, "actionId", false, "ACTION_ID");
    };

    private Query<RuleActionProperty> ruleAction_PropertiesQuery;

    public RuleActionPropertyDao(DaoConfig config) {
        super(config);
    }
    
    public RuleActionPropertyDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RULE_ACTION_PROPERTY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"KEY\" TEXT NOT NULL ," + // 1: key
                "\"VALUE\" TEXT," + // 2: value
                "\"ACTION_ID\" INTEGER);"); // 3: actionId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RULE_ACTION_PROPERTY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RuleActionProperty entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getKey());
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(3, value);
        }
 
        Long actionId = entity.getActionId();
        if (actionId != null) {
            stmt.bindLong(4, actionId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public RuleActionProperty readEntity(Cursor cursor, int offset) {
        RuleActionProperty entity = new RuleActionProperty( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // key
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // value
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // actionId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RuleActionProperty entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setKey(cursor.getString(offset + 1));
        entity.setValue(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setActionId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(RuleActionProperty entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(RuleActionProperty entity) {
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
    
    /** Internal query to resolve the "properties" to-many relationship of RuleAction. */
    public List<RuleActionProperty> _queryRuleAction_Properties(Long actionId) {
        synchronized (this) {
            if (ruleAction_PropertiesQuery == null) {
                QueryBuilder<RuleActionProperty> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ActionId.eq(null));
                ruleAction_PropertiesQuery = queryBuilder.build();
            }
        }
        Query<RuleActionProperty> query = ruleAction_PropertiesQuery.forCurrentThread();
        query.setParameter(0, actionId);
        return query.list();
    }

}
