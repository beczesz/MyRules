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
         * Add Conditions
         */
        Entity condition = schema.addEntity("RuleCondition");
        condition.setSuperclass("Condition");
        condition.addIdProperty();
        condition.addIntProperty("type").notNull();
        condition.addIntProperty("operator").notNull();

        // A condition can also have a number of other conditions
        Property parentConditionProperty = condition.addLongProperty("parentCondition").getProperty();
        ToMany conditionToSubConditions = condition.addToMany(condition, parentConditionProperty);
        conditionToSubConditions.setName("childConditions");

        /*
         * ConditionProperties
         */
        Entity conditionProperties = schema.addEntity("RuleConditionProperty");
        conditionProperties.addIdProperty();
        conditionProperties.addStringProperty("key").notNull();
        conditionProperties.addStringProperty("value");

//        // A condition can have many ConditionProperties
//        Property conditionIdProperty = conditionProperties.addLongProperty("conditionId").notNull().getProperty();
//        ToMany conditionToConditionProperties = conditionProperties.addToMany(condition, conditionIdProperty);
//        conditionToConditionProperties.setName("properties");

        /*
         * Add rules
         */
        Entity rule = schema.addEntity("Rule");
        rule.addIdProperty();
        rule.addIntProperty("state").notNull();
        rule.addIntProperty("eventCode").notNull();

//        // A rule has one condition
//        Property ruleConditionIdProperty = rule.addLongProperty("conditionId").getProperty();
//        rule.addToOne(condition, ruleConditionIdProperty);

        return schema;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


}
