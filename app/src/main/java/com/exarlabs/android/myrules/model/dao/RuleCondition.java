package com.exarlabs.android.myrules.model.dao;

import java.util.List;
import com.exarlabs.android.myrules.model.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import com.exarlabs.android.myrules.business.condition.Condition;
// KEEP INCLUDES END
/**
 * Entity mapped to table "RULE_CONDITION".
 */
public class RuleCondition extends Condition  {

    private Long id;
    private String conditionName;
    private int type;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RuleConditionDao myDao;

    private List<RuleConditionProperty> properties;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public RuleCondition() {
    }

    public RuleCondition(Long id) {
        this.id = id;
    }

    public RuleCondition(Long id, String conditionName, int type) {
        this.id = id;
        this.conditionName = conditionName;
        this.type = type;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRuleConditionDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<RuleConditionProperty> getProperties() {
        if (properties == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RuleConditionPropertyDao targetDao = daoSession.getRuleConditionPropertyDao();
            List<RuleConditionProperty> propertiesNew = targetDao._queryRuleCondition_Properties(id);
            synchronized (this) {
                if(properties == null) {
                    properties = propertiesNew;
                }
            }
        }
        return properties;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetProperties() {
        properties = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
