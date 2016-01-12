package com.exarlabs.android.myrules.model.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.exarlabs.android.myrules.model.dao.RuleConditionTree;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RULE_CONDITION_TREE".
*/
public class RuleConditionTreeDao extends AbstractDao<RuleConditionTree, Long> {

    public static final String TABLENAME = "RULE_CONDITION_TREE";

    /**
     * Properties of entity RuleConditionTree.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Operator = new Property(1, int.class, "operator", false, "OPERATOR");
        public final static Property State = new Property(2, int.class, "state", false, "STATE");
        public final static Property ParentCondition = new Property(3, Long.class, "parentCondition", false, "PARENT_CONDITION");
        public final static Property ConditionId = new Property(4, Long.class, "conditionId", false, "CONDITION_ID");
    };

    private DaoSession daoSession;

    private Query<RuleConditionTree> ruleConditionTree_ChildConditionsQuery;

    public RuleConditionTreeDao(DaoConfig config) {
        super(config);
    }
    
    public RuleConditionTreeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RULE_CONDITION_TREE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"OPERATOR\" INTEGER NOT NULL ," + // 1: operator
                "\"STATE\" INTEGER NOT NULL ," + // 2: state
                "\"PARENT_CONDITION\" INTEGER," + // 3: parentCondition
                "\"CONDITION_ID\" INTEGER);"); // 4: conditionId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RULE_CONDITION_TREE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RuleConditionTree entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getOperator());
        stmt.bindLong(3, entity.getState());
 
        Long parentCondition = entity.getParentCondition();
        if (parentCondition != null) {
            stmt.bindLong(4, parentCondition);
        }
 
        Long conditionId = entity.getConditionId();
        if (conditionId != null) {
            stmt.bindLong(5, conditionId);
        }
    }

    @Override
    protected void attachEntity(RuleConditionTree entity) {
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
    public RuleConditionTree readEntity(Cursor cursor, int offset) {
        RuleConditionTree entity = new RuleConditionTree( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // operator
            cursor.getInt(offset + 2), // state
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // parentCondition
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // conditionId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RuleConditionTree entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setOperator(cursor.getInt(offset + 1));
        entity.setState(cursor.getInt(offset + 2));
        entity.setParentCondition(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setConditionId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(RuleConditionTree entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(RuleConditionTree entity) {
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
    
    /** Internal query to resolve the "childConditions" to-many relationship of RuleConditionTree. */
    public List<RuleConditionTree> _queryRuleConditionTree_ChildConditions(Long parentCondition) {
        synchronized (this) {
            if (ruleConditionTree_ChildConditionsQuery == null) {
                QueryBuilder<RuleConditionTree> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ParentCondition.eq(null));
                ruleConditionTree_ChildConditionsQuery = queryBuilder.build();
            }
        }
        Query<RuleConditionTree> query = ruleConditionTree_ChildConditionsQuery.forCurrentThread();
        query.setParameter(0, parentCondition);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getRuleConditionDao().getAllColumns());
            builder.append(" FROM RULE_CONDITION_TREE T");
            builder.append(" LEFT JOIN RULE_CONDITION T0 ON T.\"CONDITION_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected RuleConditionTree loadCurrentDeep(Cursor cursor, boolean lock) {
        RuleConditionTree entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        RuleCondition ruleCondition = loadCurrentOther(daoSession.getRuleConditionDao(), cursor, offset);
        entity.setRuleCondition(ruleCondition);

        return entity;    
    }

    public RuleConditionTree loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<RuleConditionTree> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<RuleConditionTree> list = new ArrayList<RuleConditionTree>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<RuleConditionTree> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<RuleConditionTree> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
