package com.exarlabs.android.model.database;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;


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
        Schema schema = new Schema(VERSION, PACKAGE);

        schema.enableKeepSectionsByDefault();
        addRule(schema);

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

    private static void addRule(Schema schema) {
        Entity note = schema.addEntity("Rule");
        note.addIdProperty();
        note.addStringProperty("name").notNull();
        note.addDateProperty("date");
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


}
