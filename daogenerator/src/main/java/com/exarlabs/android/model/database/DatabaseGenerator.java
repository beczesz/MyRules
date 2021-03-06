package com.exarlabs.android.model.database;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;


/**
 * GreenDao generator class which generates the schema for the main application
 */
public class DatabaseGenerator {


    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------
    public static final int VERSION = 1000;
    public static final String PACKAGE = "com.exarlabs.android.myrules.model.dao";
    public static final String OUT_DIR = "./app/src/main/java";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        // Create a new schema
        Schema schema = new DatabaseGenerator().generateSchema();
        new DaoGenerator().generateAll(schema, OUT_DIR);
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    private Schema generateSchema() {

        // Create a new schema
        Schema schema = new Schema(VERSION, PACKAGE);
        schema.enableKeepSectionsByDefault();

        /*
         * Add RuleCondition, RuleConditionProperty and ConditionTree
         */
        Entity ruleCondition = schema.addEntity("RuleCondition");
        ruleCondition.setSuperclass("Condition");
        ruleCondition.addIdProperty();
        ruleCondition.addStringProperty("conditionName").notNull();
        ruleCondition.addIntProperty("type").notNull();

        Entity ruleConditionProperty = schema.addEntity("RuleConditionProperty");
        ruleConditionProperty.addIdProperty();
        ruleConditionProperty.setSuperclass("RuleComponentProperty");
        ruleConditionProperty.addStringProperty("key").notNull();
        ruleConditionProperty.addStringProperty("value");

        Entity ruleConditionTree = schema.addEntity("RuleConditionTree");
        ruleConditionTree.setSuperclass("ConditionTree");
        ruleConditionTree.addIdProperty();
        ruleConditionTree.addIntProperty("operator").notNull();
        ruleConditionTree.addIntProperty("state").notNull();

        /*
         * Add RuleAction and RuleActionProperty
         */
        Entity ruleAction = schema.addEntity("RuleAction");
        ruleAction.setSuperclass("Action");
        ruleAction.addIdProperty();
        ruleAction.addIntProperty("type").notNull();

        Entity ruleActionProperty = schema.addEntity("RuleActionProperty");
        ruleActionProperty.addIdProperty();
        ruleActionProperty.setSuperclass("RuleComponentProperty");
        ruleAction.addStringProperty("actionName").notNull();
        ruleActionProperty.addStringProperty("key").notNull();
        ruleActionProperty.addStringProperty("value");



        /*
         * Add RuleRecord, RuleConditionLink, RuleActionLink
         */
        Entity ruleRecord = schema.addEntity("RuleRecord");
        ruleRecord.setSuperclass("Rule");
        ruleRecord.addIdProperty();
        ruleRecord.addStringProperty("ruleName").notNull();
        ruleRecord.addIntProperty("state").notNull();
        ruleRecord.addIntProperty("eventCode").notNull();

        Entity ruleActionLink = schema.addEntity("RuleActionLink");
        ruleActionLink.addIdProperty();



        /*
         * Create the links between the entities
         */

        //Link 1:N RuleCondition to RuleCondition (recursive tree like connection)
        connectOneToMany(ruleConditionTree, "childConditions", ruleConditionTree, "parentCondition");

        // Link 1:1 RuleConditionTree and RuleCondition
        connectToOne(ruleConditionTree, "conditionId", ruleCondition);

        //Link 1:N RuleCondition to RuleConditionProperty
        connectOneToMany(ruleCondition, "properties", ruleConditionProperty, "conditionId");

        // Link 1:N RuleAction to RuleActionProperty
        connectOneToMany(ruleAction, "properties", ruleActionProperty, "actionId");

        // 1:1 link from RuleRecord to RuleConditionTree
        connectToOne(ruleRecord, "ruleConditionTreeId", ruleConditionTree);

        // Connect tge RuleRecord RuleAction via RuleActionLink as n:M
        connectOneToMany(ruleRecord, "ruleActionLinks", ruleActionLink, "ruleId");
        connectToOne(ruleActionLink, "ruleRecordId", ruleRecord);

        connectOneToMany(ruleAction, "ruleActionLinks", ruleActionLink, "actionId");
        connectToOne(ruleActionLink, "ruleActionId", ruleAction);

        return schema;
    }

    /**
     * Connects two entities with 1:N relation
     *
     * @param entityOne - the entity which holds multiple entityMany references
     * @param linkToManyName - the link name which holds the toMany reference
     * @param entityMany - the entity name which is referenced by entityOne
     * @param linkToOneName - the name of the link to the entityOne
     */
    private void connectOneToMany(Entity entityOne, String linkToManyName, Entity entityMany, String linkToOneName) {
        // Connect tge RuleRecord RuleAction via RuleActionLink as n:M
        Property entityManyLinkIdProperty = entityMany.addLongProperty(linkToOneName).getProperty();
        ToMany toMany = entityOne.addToMany(entityMany, entityManyLinkIdProperty);
        toMany.setName(linkToManyName);
    }

    /**
     * Connects two entities with 1:1 relationship
     *
     * @param entity1
     * @param connectionName
     * @param entity2
     */
    private void connectToOne(Entity entity1, String connectionName, Entity entity2) {
        Property connectionProperty = entity1.addLongProperty(connectionName).getProperty();
        entity1.addToOne(entity2, connectionProperty);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


}
