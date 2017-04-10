package fr.saagie.smartlogger.db.pgsql

import java.sql.{PreparedStatement, ResultSet, Timestamp}
import javax.sql.rowset.serial.SerialClob

import fr.saagie.smartlogger.db.model.attributes.{Attribute, AttributeFactory}

/**
  * An attribute factory, used to build new attributes
  * for objects stored in PGSQL data tables.
  *
  * @author Franck CARON
  * @since SmartLogger 0.2
  * @version 1.0
  */
object AttrPGSQLFactory extends AttributeFactory {
  /**
    * Creates a new attribute to store CLOBs (Texts without size limits)
    */
  override def newCLOB(value: String): Attribute[String] = new Attribute[String](value) {
    // REQUESTS
    /**
      * @inheritdoc
      */
    override def getType(): String = "clob"

    /**
      * @inheritdoc
      */
    override def read(result: ResultSet, columnLabel: String): Unit = {
      obj = result.getClob(columnLabel).toString
    }

    // COMMAND
    /**
      * @inheritdoc
      */
    override def write(state: PreparedStatement, parameterIndex: Int): Unit = {
      state.setClob(parameterIndex, new SerialClob(obj.toCharArray))
    }
  }

  /**
    * Creates a new attribute to store Integers
    */
  override def newInt(value: Int): Attribute[Int] = new Attribute[Int](value) {
    // REQUESTS
    /**
      * @inheritdoc
      */
    override def getType(): String = "integer"

    /**
      * @inheritdoc
      */
    override def read(result: ResultSet, columnLabel: String): Unit = {
      obj = result.getInt(columnLabel)
    }

    // COMMAND
    /**
      * @inheritdoc
      */
    override def write(state: PreparedStatement, parameterIndex: Int): Unit = {
      state.setInt(parameterIndex, obj)
    }
  }

  /**
    * Creates a new attribute to store Doubles
    */
  override def newDouble(value: Double): Attribute[Double] = new Attribute[Double](value) {
    // REQUESTS
    /**
      * @inheritdoc
      */
    override def getType(): String = "double precision"

    /**
      * @inheritdoc
      */
    override def read(result: ResultSet, columnLabel: String): Unit = {
      obj = result.getDouble(columnLabel)
    }

    // COMMAND
    /**
      * @inheritdoc
      */
    override def write(state: PreparedStatement, parameterIndex: Int): Unit = {
      state.setDouble(parameterIndex, obj)
    }
  }

  /**
    * Creates a new attribute to store Strings (n chars max.)
    *
    * @param n The size of the string
    */
  override def newString(value: String, n: Int): Attribute[String] = new Attribute[String](value) {
    // REQUESTS
    /**
      * @inheritdoc
      */
    override def getType(): String = "varchar(" + n + ')'

    /**
      * @inheritdoc
      */
    override def read(result: ResultSet, columnLabel: String): Unit = {
      obj = result.getString(columnLabel)
    }

    // COMMAND
    /**
      * @inheritdoc
      */
    override def write(state: PreparedStatement, parameterIndex: Int): Unit = {
      state.setString(parameterIndex, obj)
    }
  }

  /**
    * Creates a new attribute to store Dates
    */
  override def newDate(value: Timestamp): Attribute[Timestamp] = new Attribute[Timestamp](value) {
    // REQUESTS
    /**
      * @inheritdoc
      */
    override def getType(): String = "timestamp"

    /**
      * @inheritdoc
      */
    override def read(result: ResultSet, columnLabel: String): Unit = {
      obj = result.getTimestamp(columnLabel)
    }

    // COMMAND
    /**
      * @inheritdoc
      */
    override def write(state: PreparedStatement, parameterIndex: Int): Unit = {
      state.setTimestamp(parameterIndex, obj)
    }
  }
}
